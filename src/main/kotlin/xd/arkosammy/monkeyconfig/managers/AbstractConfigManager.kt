package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.SettingPath
import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractConfigManager<C : Config> : ConfigManager<C> {


    final override val sections: List<Section>
        get() = TODO()

    final override val configFormat: ConfigFormat<C>
        get() = TODO()

    final override val filePath: Path
        get() = TODO()

    protected val configBuilder: GenericBuilder<out Config, out FileConfig>
        get() = TODO()

    override fun reloadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            for (section: Section in this.sections) {
                section.loadValues(fileConfig)
                section.onLoaded()
            }
            return@ifConfigPresent true
        }
    }

    override fun saveToFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            for (settingGroup: Section in this.sections) {
                if (settingGroup.loadBeforeSave) {
                    settingGroup.loadValues(fileConfig)
                }
                settingGroup.setValues(fileConfig)
            }
            // Remove unused setting groups and their settings
            fileConfig.entrySet().removeIf { settingGroupEntry ->
                if (!this.containsSection(SettingPath.ofDotted(settingGroupEntry.key))) {
                    fileConfig.get<Config>(settingGroupEntry.key).entrySet().clear()
                    true
                } else {
                    false
                }
            }
            fileConfig.save()
            for(settingGroup: Section in this.sections) {
                settingGroup.onSavedToFile()
            }
            return@ifConfigPresent true
        }
    }

    override fun <V, T : Setting<V, *>> getTypedSetting(settingPath: SettingPath, settingClass: Class<T>): T? {
        TODO()
    }

    protected fun ifConfigPresent(fileConfigFunction: (FileConfig) -> Boolean): Boolean {

        val fileExists: Boolean = Files.exists(this.filePath)
        return this.configBuilder.build().use { fileConfig ->
            if (fileExists) {
                return@use fileConfigFunction(fileConfig ?: return@use false)
            } else {
                // TODO: IMPLEMENT LOGGER
                this.createNewConfigFile(fileConfig)
                false
            }
        }

    }

    // TODO: private fun checkForSettingPathUniqueness()

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        for (section: Section in this.sections) {
            section.setDefaultValue(fileConfig)
        }
        fileConfig.save()
    }

    override fun toString(): String =
        "${this::class.simpleName}{fileName=$fileName, format=$configFormat, path=$filePath, sectionAmount=${this.sections.size}}"

}