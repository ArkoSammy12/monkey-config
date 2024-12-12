package xd.arkosammy.monkeyconfig.types

class NumberType<V : Number>(override val value: V) : SerializableType<V> {
}