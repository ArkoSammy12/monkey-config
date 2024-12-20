package io.arkosammy12.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import org.slf4j.Logger
import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.ConfigElementContainer
import io.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder
import io.arkosammy12.monkeyconfig.builders.MapSectionBuilder
import io.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.forEachElement
import io.arkosammy12.monkeyconfig.sections.Section
import io.arkosammy12.monkeyconfig.sections.maps.MapSection
import io.arkosammy12.monkeyconfig.settings.Setting
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
                    this.loadFromFile()
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
        /*
        this.traverseSections { section ->
            section.setRegistered()
            section.onRegistered()
        }

         */

        this.checkForElementNameUniqueness()
        this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            /*
            this.traverseSections { section ->
                section.loadValues(fileConfig)
                section.onLoaded()
            }

             */
            this.forEachElement { element -> element.updateValue(fileConfig) }
            this.saveToFile()
            this.logger?.info("Loaded setting values for $this from config file at: ${this.filePath}")
            return@ifConfigPresent true
        }

    }

    override fun loadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.forEachElement { element -> element.updateValue(fileConfig) }
            return@ifConfigPresent true
        }
    }

    override fun saveToFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()

            this.forEachElement { element -> element.updateValue(fileConfig) }
            fileConfig.entrySet().removeIf { entry -> this.configElements.none { element -> element.path.asList.last() == entry.key  } }
            fileConfig.save()
            /*
            this.traverseSections { section ->
                section.onSavedToFile()
            }

             */
            return@ifConfigPresent true
        }
    }

    protected fun ifConfigPresent(action: (FileConfig) -> Boolean): Boolean {
        return action(this.fileConfig ?: return false)
    }

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        this.logger?.warn("Found no preexisting configuration file for ConfigManager: $this. Creating new one at: ${this.filePath}")
        this.traverseSections { section ->
            section.saveWithDefaultValues(fileConfig)
        }
        fileConfig.save()
    }

    override fun toString(): String =
        "${this::class.simpleName}{fileName=$fileName, format=$configFormat, path=$filePath, settingAmount=${this.settings.size} , sectionAmount=${this.sections.size}}"

}

private fun ConfigElementContainer.checkForElementNameUniqueness() {
    val foundNames: MutableSet<String> = mutableSetOf()
    for (configElement: ConfigElement in this.configElements) {
        val configElementName = configElement.name
        if (foundNames.contains(configElementName)) {
            throw IllegalArgumentException("Config element with name $configElementName is not unique in config element $configElement!")
        }
        foundNames.add(configElementName)
    }
    this.forEachElement { element ->
        if (element is ConfigElementContainer) {
            element.checkForElementNameUniqueness()
        }
    }
}