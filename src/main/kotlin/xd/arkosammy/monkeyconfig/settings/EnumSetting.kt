package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.EnumSettingBuilder
import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.values.SettingValue

open class EnumSetting<E : Enum<E>>(
    settingBuilder: EnumSettingBuilder<E>
) : AbstractSetting<E, EnumType<E>>(settingBuilder) {

    override val value: SettingValue<E, EnumType<E>> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

    val enumClass: Class<E> = value.default.declaringJavaClass

}