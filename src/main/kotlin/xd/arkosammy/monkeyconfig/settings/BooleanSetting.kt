package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

open class BooleanSetting(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<Boolean, BooleanType>
) : AbstractSetting<Boolean, BooleanType>(name, path, comment, value) {
}