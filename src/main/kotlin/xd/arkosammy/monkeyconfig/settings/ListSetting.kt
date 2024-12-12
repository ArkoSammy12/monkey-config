package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

open class ListSetting<E : Any, S : SerializableType<*>>(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<List<E>, ListType<S>>
) : AbstractSetting<List<E>, ListType<S>>(name, path, comment, value) {
}