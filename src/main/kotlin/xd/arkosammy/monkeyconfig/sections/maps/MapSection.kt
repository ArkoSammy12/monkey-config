package xd.arkosammy.monkeyconfig.sections.maps

import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.SerializableType

interface MapSection<V : Any, S : SerializableType<*>> : Section {

    fun get(key: String): Setting<V, S>?

    fun contains(key: String): Boolean

    override val loadBeforeSave: Boolean
        get() = true

}