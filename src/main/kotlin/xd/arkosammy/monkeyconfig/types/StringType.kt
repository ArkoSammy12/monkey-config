package xd.arkosammy.monkeyconfig.types

@JvmInline
value class StringType(override val value: String) : SerializableType<String> {

    override fun toString(): String =
        "SerializedString(value=$value)"

}