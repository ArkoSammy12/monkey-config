package xd.arkosammy.monkeyconfig.types

class EnumType<E : Enum<*>>(override val value: E) : SerializableType<E> {
}