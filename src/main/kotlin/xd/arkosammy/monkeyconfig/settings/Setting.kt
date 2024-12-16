package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.values.SettingValue

interface Setting<T : Any, S : SerializableType<*>> : ConfigElement {

    val value: SettingValue<T, S>

}