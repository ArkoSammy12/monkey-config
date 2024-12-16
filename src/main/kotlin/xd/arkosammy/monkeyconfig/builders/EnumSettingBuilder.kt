package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.EnumSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class EnumSettingBuilder<E : Enum<E>>(
    name: String,
    path: ElementPath
) : SettingBuilder<E, EnumType<E>>(name, path) {

    override var serializer: (E) -> EnumType<E> = { enumValue -> EnumType(enumValue) }

    override var deserializer: (EnumType<E>) -> E = { serializedEnumValue -> serializedEnumValue.value }

    override fun build(): Setting<E, EnumType<E>> {
        return EnumSetting(this)
    }

}