package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.EnumSetting
import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.types.EnumType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class EnumSettingBuilder<E : Enum<E>>(
    name: String,
    defaultValue: E,
    path: ElementPath
) : SettingBuilder<E, EnumType<E>>(name, defaultValue, path) {

    override var serializer: (E) -> EnumType<E> = { value -> EnumType(value) }

    override var deserializer: (EnumType<E>) -> E = EnumType<E>::value

    override var implementation: (SettingBuilder<E, EnumType<E>>) -> Setting<E, EnumType<E>> = ::EnumSetting

}