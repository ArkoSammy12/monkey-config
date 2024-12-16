package xd.arkosammy.monkeyconfig.types

@JvmInline
value class NumberType<V : Number>(override val value: V) : SerializableType<V> {
}