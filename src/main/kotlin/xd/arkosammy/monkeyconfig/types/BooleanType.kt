package xd.arkosammy.monkeyconfig.types

@JvmInline
value class BooleanType(override val value: Boolean) : SerializableType<Boolean> {

    override fun toString(): String =
        "SerializedBoolean(value=$value)"

}