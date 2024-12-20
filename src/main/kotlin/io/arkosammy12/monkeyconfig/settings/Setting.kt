package io.arkosammy12.monkeyconfig.settings

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.setValueSafely
import io.arkosammy12.monkeyconfig.types.toSerializedType
import io.arkosammy12.monkeyconfig.util.ElementPath
import io.arkosammy12.monkeyconfig.values.SettingValue

interface Setting<T : Any, S : SerializableType<*>> : ConfigElement {

    val value: SettingValue<T, S>

    override fun saveValue(fileConfig: FileConfig) {
        val settingPath: ElementPath = this.path
        val serializedSettingValue: SerializableType<*> = this.value.serialized
        fileConfig.set<Any>(settingPath.string, if (serializedSettingValue is ListType<*>) serializedSettingValue.rawList else serializedSettingValue.value)
        this.comment?.let { comment ->
            if (fileConfig is CommentedFileConfig) fileConfig.setComment(settingPath.string, comment)
        }
    }

    override fun updateValue(fileConfig: FileConfig) {
        val settingPath: ElementPath = this.path
        val defaultRawValue: Any = this.value.defaultSerialized.value
        val rawValue: Any = if (this is EnumSetting<*>) {
            fileConfig.getEnum(settingPath.string, this.enumClass)
        } else {
            fileConfig.getOrElse(settingPath.string, defaultRawValue)
        } ?: defaultRawValue
        val serializedRawValue: SerializableType<*> = toSerializedType(rawValue)
        setValueSafely(this, serializedRawValue)
    }

}