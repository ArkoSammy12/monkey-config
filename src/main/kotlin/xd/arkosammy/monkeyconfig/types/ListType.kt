package xd.arkosammy.monkeyconfig.types

class ListType<E : SerializableType<*>>(override val value: List<E>) : SerializableType<List<E>> {

    val rawList: List<*>
        get() = this.value.toList().map { e -> e.value }

}