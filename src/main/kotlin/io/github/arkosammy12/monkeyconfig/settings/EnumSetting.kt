package io.github.arkosammy12.monkeyconfig.settings

import io.github.arkosammy12.monkeyconfig.builders.EnumSettingBuilder
import io.github.arkosammy12.monkeyconfig.types.EnumType
import io.github.arkosammy12.monkeyconfig.values.SettingValue

open class EnumSetting<E : Enum<E>>(
    settingBuilder: EnumSettingBuilder<E>
) : AbstractSetting<E, EnumType<E>, EnumSetting<E>>(settingBuilder) {

    override val value: SettingValue<E, EnumType<E>> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

    override fun onInitialized() {
        this.onInitializedFunction?.let { it(this) }
    }

    override fun onUpdated() {
        this.onUpdatedFunction?.let { it(this) }

    }

    override fun onSaved() {
        this.onSavedFunction?.let { it(this) }
    }

    val enumClass: Class<E> = value.default.declaringJavaClass

}