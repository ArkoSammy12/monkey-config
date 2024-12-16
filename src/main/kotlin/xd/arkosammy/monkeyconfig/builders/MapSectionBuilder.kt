package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.sections.maps.MapSection
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

abstract class MapSectionBuilder<V : Any, S : SerializableType<*>> internal constructor(
    val name: String,
    val manager: ConfigManagerBuilder,
    val parent: SectionBuilder? = null
) {

    var comment: String? = null

    internal val path: ElementPath = this.getPath()

    internal val defaultEntries: MutableMap<String, V> = mutableMapOf()

    open lateinit var serializer: (V) -> S

    open lateinit var deserializer: (S) -> V

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

    abstract fun build(): MapSection<V, S>

}