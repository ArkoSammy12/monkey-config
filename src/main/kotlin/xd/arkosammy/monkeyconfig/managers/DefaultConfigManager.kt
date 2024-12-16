package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.builders.ConfigManagerBuilder
import xd.arkosammy.monkeyconfig.builders.SectionBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.ElementPath
import java.nio.file.Files
import java.nio.file.Path

open class DefaultConfigManager(configManagerBuilder: ConfigManagerBuilder) : ConfigManager {

    final override val fileName: String = configManagerBuilder.fileName

    final override val configFormat: ConfigFormat<*> = configManagerBuilder.fileFormat

    final override val filePath: Path = configManagerBuilder.filePath

    protected val configBuilder: GenericBuilder<out Config, out FileConfig> = FileConfig.builder(this.filePath, this.configFormat)
        .sync()
        .preserveInsertionOrder()

    final override val configElements: List<ConfigElement>

    init {
        System.setProperty("nightconfig.preserveInsertionOrder", "true")
        val newConfigElements: MutableList<ConfigElement> = mutableListOf()
        for (section: SectionBuilder in configManagerBuilder.sections) {
            val section: Section = section.build()
            newConfigElements.add(section)
        }
        for (setting: SettingBuilder<*, *> in configManagerBuilder.settings) {
            val setting: Setting<*, *> = setting.build()
            newConfigElements.add(setting)
        }
        this.configElements = newConfigElements.toList()
        this.traverseSections { section ->
            section.setRegistered()
            section.onRegistered()
        }
        this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.traverseSections { section ->
                section.loadValues(fileConfig)
                section.onLoaded()
            }
            this.saveToFile()
            // TODO: LOG
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
    override fun <V : Any, T : Setting<V, *>> getSetting(elementPath: ElementPath, settingClass: Class<T>): Setting<V, *>? {
        var returnedSetting: Setting<V, *>? = null
        this.traverseSettings { setting ->
            if (settingClass.isInstance(setting) && setting.path == elementPath) {
                returnedSetting = setting as T
                return@traverseSettings
            }
        }
        return returnedSetting
    }

    protected fun ifConfigPresent(fileConfigFunction: (FileConfig) -> Boolean): Boolean {
        val fileExists: Boolean = Files.exists(this.filePath)
        return this.configBuilder.build().use { fileConfig ->
            if (fileExists) {
                return@use fileConfigFunction(fileConfig ?: return@use false)
            } else {
                // TODO: LOG
                this.createNewConfigFile(fileConfig)
                false
            }
        }
    }

    // TODO: private fun checkForSettingPathUniqueness()

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        this.traverseSections { section ->
            section.setDefaultValues(fileConfig)
        }
        fileConfig.save()
    }

    override fun toString(): String =
        "${this::class.simpleName}{fileName=$fileName, format=$configFormat, path=$filePath, settingAmount=${this.configElements.filter { configElement -> configElement is Setting<*, *> }.size} , sectionAmount=${this.configElements.filter { configElement -> configElement is Section }.size}}"

}