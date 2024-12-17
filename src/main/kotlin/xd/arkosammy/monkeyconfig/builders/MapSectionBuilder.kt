package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.sections.maps.MapSection
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class MapSectionBuilder<V : Any, S : SerializableType<*>>(
    val name: String,
    val parent: SectionBuilder? = null
) {

    open var comment: String? = null

    open lateinit var serializer: (V) -> S

    open lateinit var deserializer: (S) -> V

    open lateinit var implementation: (MapSectionBuilder<V, S>) -> MapSection<V, S>

    internal val path: ElementPath = this.getPath()

    internal val defaultEntries: MutableMap<String, V> = mutableMapOf()

    fun addDefaultEntry(entry: Pair<String, V>) {
        val key: String = entry.first
        val value: V = entry.second
        if (key.contains(".")) {
            throw IllegalArgumentException("Map section keys cannot contain \".\"!")
        }
        this.defaultEntries.put(key, value)
    }

    private fun getPath(): ElementPath {
        var path = ElementPath(this.name)
        this.traverseToParent { sectionBuilder -> path = path.withPrependedNode(sectionBuilder.name) }
        return path
    }

    private fun traverseToParent(section: SectionBuilder? = null, consumer: (SectionBuilder) -> Unit) {
        val parent: SectionBuilder? = section?.parent ?: this.parent
        if (parent == null) {
            return
        }
        consumer(parent)
        traverseToParent(parent, consumer)
    }

    fun build(): MapSection<V, S> =
        this.implementation(this)

}