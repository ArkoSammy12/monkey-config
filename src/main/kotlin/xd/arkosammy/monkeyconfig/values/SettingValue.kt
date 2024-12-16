package xd.arkosammy.monkeyconfig.values

import xd.arkosammy.monkeyconfig.types.SerializableType

open class SettingValue<T : Any, S : SerializableType<*>>(
    val default: T,
    open var raw: T = default,
    private val serializer: (T) -> S,
    private val deserializer: (S) -> T
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
        "${this::class.simpleName}{defaultValue=$default, value=$raw}"

}