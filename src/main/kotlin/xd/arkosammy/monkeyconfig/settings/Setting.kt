package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

interface Setting<T : Any, S : SerializableType<*>> {

    val name: String

    val path: SettingPath

    val comment: String?

    val value: SettingValue<T, S>

}