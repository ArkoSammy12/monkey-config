package io.arkosammy12.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import org.slf4j.Logger
import io.arkosammy12.monkeyconfig.base.ConfigElement
import io.arkosammy12.monkeyconfig.base.ConfigElementContainer
import io.arkosammy12.monkeyconfig.base.ConfigManager
import io.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder
import io.arkosammy12.monkeyconfig.base.forEachElement
import io.arkosammy12.monkeyconfig.base.sections
import io.arkosammy12.monkeyconfig.base.settings
import io.arkosammy12.monkeyconfig.base.traverseSections
import io.arkosammy12.monkeyconfig.builders.ConfigElementBuilder
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
        for (configElementBuilder: ConfigElementBuilder<*, *> in configManagerBuilder.configElementBuilders) {
            val configElement: ConfigElement = configElementBuilder.build()
            newConfigElements.add(configElement)
        }
        this.configElements = newConfigElements.toList()
        this.checkForElementNameUniqueness()
        this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.forEachElement<ConfigElement> { element ->
                element.updateValue(fileConfig)
                element.onInitialized()
            }
            this.forEachElement<ConfigElement> { element ->
                element.saveValue(fileConfig)
            }
            fileConfig.entrySet().removeIf { entry -> this.configElements.none { element -> element.path.asList.last() == entry.key  } }
            fileConfig.save()
            this.logger?.info("Loaded setting values for $this from config file at: ${this.filePath}")
            return@ifConfigPresent true
        }

    }

    override fun loadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.forEachElement<ConfigElement> { element ->
                element.updateValue(fileConfig)
                element.onUpdated()
            }
            return@ifConfigPresent true
        }
    }

    override fun saveToFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.forEachElement<ConfigElement> { element ->
                element.saveValue(fileConfig)
                element.onSaved()
            }
            fileConfig.entrySet().removeIf { entry -> this.configElements.none { element -> element.path.asList.last() == entry.key  } }
            fileConfig.save()
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
    this.forEachElement<ConfigElement> { element ->
        if (element is ConfigElementContainer) {
            element.checkForElementNameUniqueness()
        }
    }
}