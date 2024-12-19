package io.arkosammy12.monkeyconfig.types

@JvmInline
value class EnumType<E : Enum<*>>(override val value: E) : SerializableType<E> {

    override fun toString(): String =
        "SerializedEnum(value=$value)"

}