package io.arkosammy12.monkeyconfig.types

@JvmInline
value class StringType(override val value: String) : SerializableType<String> {

    override fun toString(): String =
        "SerializedString(value=$value)"

}