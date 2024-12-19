package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.EnumType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class EnumSetting<E : Enum<E>>(
    settingBuilder: SettingBuilder<E, EnumType<E>>
) : AbstractSetting<E, EnumType<E>>(settingBuilder) {

    override val value: SettingValue<E, EnumType<E>> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

    val enumClass: Class<E> = value.default.declaringJavaClass

}