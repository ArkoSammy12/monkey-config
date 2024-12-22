package io.arkosammy12.monkeyconfig.base

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import io.arkosammy12.monkeyconfig.settings.EnumSetting
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.setValueSafely
import io.arkosammy12.monkeyconfig.types.toSerializedType
import io.arkosammy12.monkeyconfig.util.ElementPath
import io.arkosammy12.monkeyconfig.values.SettingValue

/**
 * A [ConfigElement] that associates a key to a [SettingValue].
 *
 * @param V The type of the value that is stored by this instance.
 * @param S The [SerializableType] that will be used to serialize and deserialize the value [V] to and from a configuration file.
 */
interface Setting<V : Any, S : SerializableType<*>> : ConfigElement {

    /**
     * Stores the value [V] of the associated [Setting] and contains a function to transform the value [V] to a serialized value [S]
     * and a function to do the opposite. Allows to access of the setting value itself, as well as its serialized version. Permits the value to be
     * set with an instance of type [V] or with a serialized instance of type [S].
     */
    val value: SettingValue<V, S>

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