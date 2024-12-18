@file:JvmName("SectionUtils")

package io.arkosammy12.monkeyconfig.sections

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.settings.EnumSetting
import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.setValueSafely
import io.arkosammy12.monkeyconfig.types.toSerializedType
import io.arkosammy12.monkeyconfig.util.ElementPath

interface Section : ConfigElement {

    val configElements: List<ConfigElement>

    val loadBeforeSave: Boolean

    val isRegistered: Boolean

    fun setRegistered()

    fun onRegistered() {}

    fun onLoaded() {}

    fun onSavedToFile() {}

    fun setDefaultValues(fileConfig: FileConfig) {
        this.forEachSetting { setting -> setting.value.reset() }
        this.setValues(fileConfig)
    }

    fun setValues(fileConfig: FileConfig) {
        this.forEachSetting { setting ->
            val settingPath: ElementPath = setting.path
            val serializedSettingValue: SerializableType<*> = setting.value.serialized
            fileConfig.set<Any>(settingPath.string, if (serializedSettingValue is ListType<*>) serializedSettingValue.rawList else serializedSettingValue.value)
            setting.comment?.let { comment ->
                if (fileConfig is CommentedFileConfig) fileConfig.setComment(settingPath.string, comment)
            }
        }
        this.comment?.let { comment ->
            if (fileConfig is CommentedFileConfig) fileConfig.setComment(this.path.string, comment)
        }
        val config: Config = fileConfig.get(this.path.string) ?: return
        // TODO: Add check to make sure we are not accidentally deleting a section
        // TODO: In general, remodel our API in terms of config elements, not sections or map sections or settings
        // TODO: Actually, I don't think this is needed anymore due to the default config manager code. Still it might be a good idea to keep this
        //  in case someone has another config manager implementation
        config.entrySet().removeIf { entry -> !this.containsSetting(entry.key) }
    }

    fun loadValues(fileConfig: FileConfig) {
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

    fun containsSetting(settingName: String): Boolean = this.settings.any { setting -> setting.path.asList.last() == settingName }

}

val Section.subSections: List<Section>
    get() {
        val subSections: MutableList<Section> = mutableListOf()
        this.forEachSection { section -> subSections.add(section) }
        return subSections.toList()
    }

val Section.settings: List<Setting<*, *>>
    get() {
        val settings: MutableList<Setting<*, *>> = mutableListOf()
        this.forEachSetting { setting -> settings.add(setting) }
        return settings.toList()
    }

fun Section.forEachSection(consumer: (Section) -> Unit) {
    for (element: ConfigElement in this.configElements) {
        if (element !is Section) {
            continue
        }
        consumer(element)
    }
}

fun Section.forEachSetting(consumer: (Setting<*, *>) -> Unit) {
    for (element: ConfigElement in this.configElements) {
        if (element !is Setting<*, *>) {
            continue
        }
        consumer(element)
    }
}

fun Section.traverseSections(action: (Section) -> Unit) {
    action(this)
    this.forEachSection { section ->
        section.traverseSections(action)
    }
}