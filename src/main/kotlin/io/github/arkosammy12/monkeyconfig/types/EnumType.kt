package io.github.arkosammy12.monkeyconfig.types

@OptIn(ExperimentalStdlibApi::class)
@JvmInline
@JvmExposeBoxed
value class EnumType<E : Enum<*>>(override val value: E) : SerializableType<E> {

    override fun toString(): String =
        "SerializedEnum(value=$value)"

}