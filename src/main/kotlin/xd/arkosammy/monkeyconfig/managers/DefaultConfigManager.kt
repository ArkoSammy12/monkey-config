package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import org.slf4j.Logger
import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.builders.ConfigManagerBuilder
import xd.arkosammy.monkeyconfig.builders.MapSectionBuilder
import xd.arkosammy.monkeyconfig.builders.SectionBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.sections.forEachSection
import xd.arkosammy.monkeyconfig.sections.maps.MapSection
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.ElementPath
import java.nio.file.Files
import java.nio.file.Path

open class DefaultConfigManager(
    configManagerBuilder: ConfigManagerBuilder
) : ConfigManager {

    final override val fileName: String = configManagerBuilder.fileName

    final override val configFormat: ConfigFormat<*> = configManagerBuilder.fileFormat

    final override val filePath: Path = configManagerBuilder.filePath

    val logger: Logger? = configManagerBuilder.logger

    protected val configBuilder: GenericBuilder<out Config, out FileConfig> = FileConfig.builder(this.filePath, this.configFormat)
        .preserveInsertionOrder().also { builder ->
            if (configManagerBuilder.async) {
                builder.async()
            } else {
                builder.sync()
            }
            if (configManagerBuilder.autoReload) {
                builder.autoreload()
                builder.onAutoReload {
                    this.reloadFromFile()
                }
            }
        }

    protected val fileConfig: FileConfig? by lazy {
        try {
            configBuilder.build().apply {
                if (!Files.exists(filePath)) {
                    createNewConfigFile(this)
                }
            }
        } catch (e: Exception) {
            this.logger?.error("Failed to create FileConfig instance for $this: $e")
            null
        }
    }

    final override val configElements: List<ConfigElement>

    init {
        System.setProperty("nightconfig.preserveInsertionOrder", "true")
        val newConfigElements: MutableList<ConfigElement> = mutableListOf()
        for (sectionBuilder: SectionBuilder in configManagerBuilder.sections) {
            val section: Section = sectionBuilder.build()
            newConfigElements.add(section)
        }
        for (mapSectionBuilder: MapSectionBuilder<*, *> in configManagerBuilder.mapSections) {
            val mapSection: MapSection<*, *> = mapSectionBuilder.build()
            newConfigElements.add(mapSection)

        }
        for (settingBuilder: SettingBuilder<*, *> in configManagerBuilder.settings) {
            val setting: Setting<*, *> = settingBuilder.build()
            newConfigElements.add(setting)
        }
        this.configElements = newConfigElements.toList()
        this.traverseSections { section ->
            section.setRegistered()
            section.onRegistered()
        }
        this.checkForElementNameUniqueness()
        this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.traverseSections { section ->
                section.loadValues(fileConfig)
                section.onLoaded()
            }
            this.saveToFile()
            this.logger?.info("Loaded setting values for $this from config file at: ${this.filePath}")
            return@ifConfigPresent true
        }

    }

    override fun reloadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.traverseSections { section ->
                section.loadValues(fileConfig)
                section.onLoaded()
            }
            return@ifConfigPresent true
        }
    }

    override fun saveToFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.traverseSections { section ->
                if (section.loadBeforeSave) {
                    section.loadValues(fileConfig)
                }
                section.setValues(fileConfig)
            }
            this.removedUnusedSections(fileConfig)
            fileConfig.save()
            this.traverseSections { section ->
                section.onSavedToFile()
            }
            return@ifConfigPresent true
        }
    }

    // TODO: TEST
    private fun removedUnusedSections(fileConfig: FileConfig, config: Config? = null) {
        val currentConfig: Config = config ?: fileConfig
        currentConfig.entrySet().removeIf { entry ->
            if (!this.containsSection(ElementPath(entry.key))) {
                fileConfig.get<Config>(entry.key).entrySet().clear()
                true
            } else {
                false
            }
        }
        fileConfig.entrySet().forEach { entry ->
            this.removedUnusedSections(fileConfig, fileConfig.get<Config>(entry.key))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <V : Any, T : Setting<V, *>> getSetting(settingPath: ElementPath, settingClass: Class<T>): Setting<V, *>? {
        var returnedSetting: Setting<V, *>? = null
        this.traverseSettings { setting ->
            if (settingClass.isInstance(setting) && setting.path == settingPath) {
                returnedSetting = setting as T
                return@traverseSettings
            }
        }
        return returnedSetting
    }

    protected fun ifConfigPresent(action: (FileConfig) -> Boolean): Boolean {
        return action(this.fileConfig ?: return false)
    }

    private fun checkForElementNameUniqueness() {
        val foundNames: MutableSet<String> = mutableSetOf()
        for (configElement: ConfigElement in this.configElements) {
            val configElementName = configElement.name
            if (foundNames.contains(configElementName)) {
                throw IllegalArgumentException("Config element with name $configElementName is not unique in config element $configElement!")
            }
            foundNames.add(configElementName)
        }
         this.forEachSection { section -> section.checkForElementNameUniqueness() }
    }

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        this.logger?.warn("Found no preexisting configuration file for ConfigManager: $this. Creating new one at: ${this.filePath}")
        this.traverseSections { section ->
            section.setDefaultValues(fileConfig)
        }
        fileConfig.save()
    }

    override fun toString(): String =
        "${this::class.simpleName}{fileName=$fileName, format=$configFormat, path=$filePath, settingAmount=${this.settings.size} , sectionAmount=${this.sections.size}}"

}

private fun Section.checkForElementNameUniqueness() {
    val foundNames: MutableSet<String> = mutableSetOf()
    for (configElement: ConfigElement in this.configElements) {
        val configElementName = configElement.name
        if (foundNames.contains(configElementName)) {
            throw IllegalArgumentException("Config element with name $configElementName is not unique in config element $configElement!")
        }
        foundNames.add(configElementName)
    }
    this.forEachSection { section -> section.checkForElementNameUniqueness() }
}