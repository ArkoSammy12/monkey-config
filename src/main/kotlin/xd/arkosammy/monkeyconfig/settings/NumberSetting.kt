package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<T, NumberType<T>>
) : AbstractSetting<T, NumberType<T>>(name, path, comment, value) {
}