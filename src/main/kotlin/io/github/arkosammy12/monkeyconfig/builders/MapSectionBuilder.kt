package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.sections.maps.MapSection
import io.github.arkosammy12.monkeyconfig.types.SerializableType
import io.github.arkosammy12.monkeyconfig.util.ElementPath

/**
 * An implementation of [ConfigElementBuilder] that builds [MapSection] instances.
 *
 * @param V The type of the values of the entries held by the resulting [MapSection].
 * @param S The [SerializableType] used when writing the values of the entries held by the resulting [MapSection] to a configuration file.
 */
open class MapSectionBuilder<V : Any, S : SerializableType<*>>(
    name: String,
    val parent: SectionBuilder? = null
) : ConfigElementBuilder<MapSection<V, S>, MapSectionBuilder<V, S>>(name) {

    open lateinit var serializer: (V) -> S

    open lateinit var deserializer: (S) -> V

    override lateinit var implementation: (MapSectionBuilder<V, S>) -> MapSection<V, S>

    override val path: ElementPath = this.getPath()

    internal val defaultEntries: MutableMap<String, V> = mutableMapOf()

    init {
        this.logger = parent?.logger
    }

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

    override fun build(): MapSection<V, S> =
        this.implementation(this)

}