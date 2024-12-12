package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

open class StringListSetting(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<List<String>, ListType<StringType>>
) : ListSetting<String, StringType>(name, path, comment, value) {
}