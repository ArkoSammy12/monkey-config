package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.values.SettingValue

interface Setting<T : Any, S : SerializableType<*>> : ConfigElement {

    val value: SettingValue<T, S>

}