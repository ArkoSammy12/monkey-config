package io.arkosammy12.monkeyconfig.values

import io.arkosammy12.monkeyconfig.types.SerializableType

open class SettingValue<V : Any, S : SerializableType<*>>(
    val default: V,
    open var raw: V = default,
    private val serializer: (V) -> S,
    private val deserializer: (S) -> V
) {

    open val serialized: S
        get() = this.serializer(this.raw)

    open val defaultSerialized: S
        get() = this.serializer(this.default)

    open fun setFromSerialized(serialized: S) {
        this.raw = this.deserializer(serialized)
    }

    open fun reset() {
        this.raw = default
    }

    override fun toString(): String =
        "${this::class.simpleName}{defaultValue=$default, value=$raw, defaultSerializedValue=$defaultSerialized, serializedValue=$serialized}"

}