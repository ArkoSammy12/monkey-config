package xd.arkosammy.monkeyconfig.sections

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.settings.EnumSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.types.toSerializedType
import xd.arkosammy.monkeyconfig.util.SettingPath

interface Section {

    val name: String

    val path: SettingPath

    val comment: String?

    val settings: List<Setting<*, *>>

    val subsections: List<Section>

    val loadBeforeSave: Boolean

    val isRegistered: Boolean

    fun setRegistered()

    fun onRegistered() {}

    fun onLoaded() {}

    fun onSavedToFile() {}

    fun setDefaultValue(fileConfig: FileConfig) {
        for (setting: Setting<*, *> in this.settings) {
            setting.value.reset()
        }
        this.setValues(fileConfig)
    }

    fun setValues(fileConfig: FileConfig) {
        for (setting: Setting<*, *> in this.settings) {
            val settingPath: String = setting.path.getString()
            val serializedValue: SerializableType<*> = setting.value.serialized
            fileConfig.set<Any>(settingPath, if (serializedValue is ListType<*>) serializedValue.rawList else serializedValue.value)
            setting.comment?.let { comment ->
                if (fileConfig is CommentedFileConfig) {
                    fileConfig.setComment(settingPath, comment)
                }
            }
        }
        this.comment?.let { comment ->
            if (fileConfig is CommentedFileConfig) {
                fileConfig.setComment(this.path.getString(), comment)
            }
        }
        val configSection: Config = fileConfig.get(this.path.getString()) ?: return
        configSection.entrySet().removeIf { entry -> !this.containsSetting(SettingPath.ofDotted(entry.key)) }
    }

    fun loadValues(fileConfig: FileConfig) {

        for (setting: Setting<*, *> in this.settings) {

            val settingPath = setting.path.getString()
            val defaultSerializedValue: Any = setting.value.defaultSerialized.value
            val value: Any = if (setting is EnumSetting<*>) {
                fileConfig.getEnum(settingPath, setting.enumClass)
            } else {
                fileConfig.getOrElse(settingPath, defaultSerializedValue)
            } ?: defaultSerializedValue
            val serializedValue: SerializableType<*> = toSerializedType(value)
            setValueSafely(setting, serializedValue)
        }

    }

    fun containsSetting(settingPath: SettingPath): Boolean = this.settings.any { setting -> setting.path == settingPath }

}

@Suppress("UNCHECKED_CAST")
fun <T : Any, V : SerializableType<*>> setValueSafely(setting: Setting<T, V>, value : SerializableType<*>) {
    when (value) {
        is NumberType<*> -> {
            if(setting.value.defaultSerialized is NumberType<*>) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is BooleanType -> {
            if(setting.value.defaultSerialized is BooleanType) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is EnumType<*> -> {
            if(setting.value.defaultSerialized is EnumType<*>) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is ListType<*> -> {
            if(setting.value.defaultSerialized is ListType<*>) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is StringType -> {
            if(setting.value.defaultSerialized is StringType) {
                setting.value.setFromSerialized(value as V)
            }
        }
    }
}