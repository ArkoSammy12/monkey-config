package io.arkosammy12.monkeyconfig.types

@JvmInline
value class NumberType<V : Number>(override val value: V) : SerializableType<V> {

    override fun toString(): String =
        "SerializedNumber(value=$value)"

}