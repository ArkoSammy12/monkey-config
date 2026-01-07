package io.github.arkosammy12.monkeyconfig.types

@OptIn(ExperimentalStdlibApi::class)
@JvmInline
@JvmExposeBoxed
value class NumberType<V : Number>(override val value: V) : SerializableType<V> {

    override fun toString(): String =
        "SerializedNumber(value=$value)"

}