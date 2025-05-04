package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.sections.maps.MapSection
import io.github.arkosammy12.monkeyconfig.types.SerializableType

/**
 * An implementation of [ConfigElementBuilder] that builds [MapSection] instances.
 *
 * @param V The type of the values of the entries held by the resulting [MapSection].
 * @param S The [SerializableType] used when writing the values of the entries held by the resulting [MapSection] to a configuration file.
 */
open class MapSectionBuilder<V : Any, S : SerializableType<*>>(
    name: String,
    manager: ConfigManagerBuilder,
    parent: SectionBuilder<*, *>? = null
) : SectionBuilder<MapSection<V, S>, MapSectionBuilder<V, S>>(name, manager, parent) {

    open lateinit var serializer: (V) -> S

    open lateinit var deserializer: (S) -> V

    override lateinit var implementation: (MapSectionBuilder<V, S>) -> MapSection<V, S>

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

    override fun build(): MapSection<V, S> =
        this.implementation(this)

}