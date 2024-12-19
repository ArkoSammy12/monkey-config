package io.arkosammy12.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import org.slf4j.Logger
import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder
import io.arkosammy12.monkeyconfig.builders.MapSectionBuilder
import io.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.sections.Section
import io.arkosammy12.monkeyconfig.sections.forEachSection
import io.arkosammy12.monkeyconfig.sections.maps.MapSection
import io.arkosammy12.monkeyconfig.settings.EnumSetting
import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.setValueSafely
import io.arkosammy12.monkeyconfig.types.toSerializedType
import io.arkosammy12.monkeyconfig.util.ElementPath
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
        for (sectionBuilder: SectionBuilder in configManagerBuilder.sectionBuilders) {
            val section: Section = sectionBuilder.build()
            newConfigElements.add(section)
        }
        for (mapSectionBuilder: MapSectionBuilder<*, *> in configManagerBuilder.mapSectionBuilders) {
            val mapSection: MapSection<*, *> = mapSectionBuilder.build()
            newConfigElements.add(mapSection)

        }
        for (settingBuilder: SettingBuilder<*, *> in configManagerBuilder.settingBuilders) {
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
            this.loadTopLevelSettingValues(fileConfig)
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
            this.loadTopLevelSettingValues(fileConfig)
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
            this.setTopLevelSettingValues(fileConfig)
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

    protected fun setTopLevelSettingValues(fileConfig: FileConfig) {
        this.forEachSetting { setting ->
            val settingPath: ElementPath = setting.path
            val serializedSettingValue: SerializableType<*> = setting.value.serialized
            fileConfig.set<Any>(settingPath.string, if (serializedSettingValue is ListType<*>) serializedSettingValue.rawList else serializedSettingValue.value)
            setting.comment?.let { comment ->
                if (fileConfig is CommentedFileConfig) fileConfig.setComment(settingPath.string, comment)
            }
        }
        // Clean up unused top level settings
        fileConfig.entrySet().removeIf { entry -> !this.containsSetting(ElementPath(entry.key)) && !this.containsSection(ElementPath(entry.key)) }
    }

    protected fun loadTopLevelSettingValues(fileConfig: FileConfig) {
        this.forEachSetting { setting ->
            val settingPath: ElementPath = setting.path
            val defaultRawValue: Any = setting.value.defaultSerialized.value
            val rawValue: Any = if (setting is EnumSetting<*>) {
                fileConfig.getEnum(settingPath.string, setting.enumClass)
            } else {
                fileConfig.getOrElse(settingPath.string, defaultRawValue)
            } ?: defaultRawValue
            val serializedRawValue: SerializableType<*> = toSerializedType(rawValue)
            setValueSafely(setting, serializedRawValue)
        }
    }

    // TODO: TEST
    private fun removedUnusedSections(fileConfig: FileConfig, config: Config? = null, currentPath: ElementPath? = null) {

        val currentConfig: Config = config ?: fileConfig
        currentConfig.entrySet().removeIf { entry ->
            val entryPath: ElementPath = currentPath?.withAppendedNode(entry.key) ?: ElementPath(entry.key)
            if (!this.containsSection(entryPath) && !this.containsSetting(entryPath)) {
                currentConfig.get<Config>(entry.key).entrySet().clear()
                true
            } else {
                false
            }
        }
        currentConfig.entrySet().forEach { entry ->
            val entryPath: ElementPath = currentPath?.withAppendedNode(entry.key) ?: ElementPath(entry.key)
            if (!this.containsSetting(entryPath)) {
                this.removedUnusedSections(fileConfig, currentConfig.get<Config>(entry.key), entryPath)
            }
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