package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

open class EnumSetting<E : Enum<E>>(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<E, EnumType<E>>
) : AbstractSetting<E, EnumType<E>>(name, path, comment, value) {

    val enumClass: Class<E> = value.default.declaringJavaClass

}