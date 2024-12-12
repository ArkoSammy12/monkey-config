package xd.arkosammy.monkeyconfig.values

import xd.arkosammy.monkeyconfig.types.SerializableType

class SettingValue<T : Any, S : SerializableType<*>>(
    val default: T,
    var raw: T = default,
    private val serializer: (T) -> S,
    private val deserializer: (S) -> T
) {

    val serialized: S
        get() = this.serializer(this.raw)

    val defaultSerialized: S
        get() = this.serializer(this.default)

    fun setFromSerialized(serialized: S) {
        this.raw = this.deserializer(serialized)
    }

    fun reset() {
        this.raw = default
    }

    override fun toString(): String =
        "${this::class.simpleName}{defaultValue=$default, value=$raw}"

}