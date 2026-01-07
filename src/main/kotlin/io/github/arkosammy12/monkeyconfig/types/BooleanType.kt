package io.github.arkosammy12.monkeyconfig.types

@OptIn(ExperimentalStdlibApi::class)
@JvmInline
@JvmExposeBoxed
value class BooleanType(override val value: Boolean) : SerializableType<Boolean> {

    override fun toString(): String =
        "SerializedBoolean(value=$value)"

}