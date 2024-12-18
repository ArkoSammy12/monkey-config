package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.EnumSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class EnumSettingBuilder<E : Enum<E>>(
    name: String,
    defaultValue: E,
    path: ElementPath
) : SettingBuilder<E, EnumType<E>>(name, defaultValue, path) {

    override var serializer: (E) -> EnumType<E> = { value -> EnumType(value) }

    override var deserializer: (EnumType<E>) -> E = EnumType<E>::value

    override var implementation: (SettingBuilder<E, EnumType<E>>) -> Setting<E, EnumType<E>> = ::EnumSetting

}