package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.EnumSettingBuilder
import io.arkosammy12.monkeyconfig.types.EnumType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class EnumSetting<E : Enum<E>>(
    settingBuilder: EnumSettingBuilder<E>
) : AbstractSetting<E, EnumType<E>, EnumSetting<E>>(settingBuilder) {

    override val value: SettingValue<E, EnumType<E>> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

    override fun onInitialized() {
        if (onInitializedFunction != null) {
            this.onInitializedFunction(this)
        }
    }

    override fun onUpdated() {
        if (onUpdatedFunction != null) {
            this.onUpdatedFunction(this)
        }
    }

    override fun onSaved() {
        if (onSavedFunction != null) {
            this.onSavedFunction(this)
        }
    }

    val enumClass: Class<E> = value.default.declaringJavaClass

}