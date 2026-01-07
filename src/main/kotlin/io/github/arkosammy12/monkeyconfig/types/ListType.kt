package io.github.arkosammy12.monkeyconfig.types

@OptIn(ExperimentalStdlibApi::class)
@JvmInline
@JvmExposeBoxed
value class ListType<E : SerializableType<*>>(override val value: List<E>) : SerializableType<List<E>> {

    val rawList: List<*>
        get() = this.value.toList().map { e -> e.value }

    override fun toString(): String =
        "SerializedList(size=${value.size})"

}