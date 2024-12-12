package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

class StringSetting(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<String, StringType>
) : AbstractSetting<String, StringType>(name, path, comment, value) {
}