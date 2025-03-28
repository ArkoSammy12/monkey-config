package io.github.arkosammy12.monkeyconfig.sections.maps

import io.github.arkosammy12.monkeyconfig.base.Section
import io.github.arkosammy12.monkeyconfig.base.Setting
import io.github.arkosammy12.monkeyconfig.types.SerializableType

interface MapSection<V : Any, S : SerializableType<*>> : Section {

    fun get(key: String): Setting<V, S>?

    fun contains(key: String): Boolean

    override val loadBeforeSave: Boolean
        get() = true

}