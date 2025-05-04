package io.github.arkosammy12.monkeyconfig.sections.maps

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import io.github.arkosammy12.monkeyconfig.base.ConfigElement
import io.github.arkosammy12.monkeyconfig.builders.MapSectionBuilder
import io.github.arkosammy12.monkeyconfig.base.Setting
import io.github.arkosammy12.monkeyconfig.types.ListType
import io.github.arkosammy12.monkeyconfig.types.SerializableType
import io.github.arkosammy12.monkeyconfig.types.toSerializedType
import io.github.arkosammy12.monkeyconfig.util.ElementPath
import org.slf4j.Logger

abstract class AbstractMapSection<V : Any, S : SerializableType<*>>(
    mapSectionBuilder: MapSectionBuilder<V, S>
) : MapSection<V, S> {

    override val name: String = mapSectionBuilder.name

    override val comment: String? = mapSectionBuilder.comment

    override val path: ElementPath = mapSectionBuilder.path

    val serializer: (V) -> S = mapSectionBuilder.serializer

    val deserializer: (S) -> V = mapSectionBuilder.deserializer

    override val configElements: List<ConfigElement>
        get() = this.entries.toList()

    protected var entries: MutableList<Setting<V, S>>

    protected var defaultEntries: List<Setting<V, S>>

    final override val loadBeforeSave: Boolean
        get() = true

    protected var internalIsInitialized: Boolean = false

    override val isInitialized: Boolean
        get() = this.internalIsInitialized

    protected val onInitializedFunction: ((MapSection<V, S>) -> Unit)? = mapSectionBuilder.onInitialized

    protected val onUpdatedFunction: ((MapSection<V, S>) ->  Unit)? = mapSectionBuilder.onUpdated

    protected val onSavedFunction: ((MapSection<V, S>) -> Unit)? = mapSectionBuilder.onSaved

    protected val logger: Logger? = mapSectionBuilder.logger

    init {
        val mapEntries: MutableList<Setting<V, S>> = mutableListOf()
        val defaultMapEntries: MutableList<Setting<V, S>> = mutableListOf()
        for ((key, value) in mapSectionBuilder.defaultEntries) {
            val entry: Setting<V, S> = this.getEntryFromValue(this.path.withAppendedNode(key), value)
            defaultMapEntries.add(entry)
            mapEntries.add(entry)
        }
        this.entries = mapEntries
        this.defaultEntries = defaultMapEntries.toList()
    }

    override fun onInitialized() {
        this.internalIsInitialized =  true
        this.onInitializedFunction?.let { it(this) }

    }

    override fun onUpdated() {
        this.onUpdatedFunction?.let { it(this) }
    }

    override fun onSaved() {
        this.onSavedFunction?.let { it(this) }
    }


    override fun get(key: String): Setting<V, S>? {
        for (entry: Setting<V, S> in this.entries) {
            if (entry.path.asList.last() == key) {
                return entry
            }
        }
        return null
    }

    override fun contains(key: String): Boolean {
        for (entry: Setting<V, S> in this.entries) {
            if (entry.path.asList.last() == key) {
                return true
            }
        }
        return false
    }

    override fun saveWithDefaultValues(fileConfig: FileConfig) {
        this.entries.clear()
        this.entries.addAll(this.defaultEntries)
        this.saveValue(fileConfig)
    }

    override fun saveValue(fileConfig: FileConfig) {
        this.updateValue(fileConfig)
        for (entry: Setting<V, S> in this.entries) {
            val serialized: S = entry.value.serialized
            fileConfig.set<Any>(entry.path.string, if (serialized is ListType<*>) serialized.rawList else serialized.value)
        }
        this.comment?.let { comment ->
            if (fileConfig is CommentedFileConfig) fileConfig.setComment(this.path.string, comment)
        }
    }

    override fun updateValue(fileConfig: FileConfig) {
        val config: Config = fileConfig.get(this.path.string) ?: run {
            this.logger?.error("Found no Section with name ${this.name} to load values from!")
            return@updateValue
        }
        val tempEntries: MutableList<Setting<V, S>> = mutableListOf()
        for (entry: Config.Entry in config.entrySet()) {
            val rawEntryValue: Any = entry.getValue()
            val serializedEntryValue: SerializableType<*> = toSerializedType(rawEntryValue)
            val newMapEntry: Setting<V, S>? = this.getEntryFromSerialized(ElementPath(*this.path.asArray, entry.key), serializedEntryValue)
            if (newMapEntry == null) {
                this.logger?.error("Unable to read value of entry ${entry.key}, from MapSection ${this.name}! This entry will be skipped.")
                continue
            }
            tempEntries.add(newMapEntry)
        }
        this.entries.clear()
        this.entries.addAll(tempEntries)
    }

    protected abstract fun getEntryFromSerialized(entryPath: ElementPath, serializedEntry: SerializableType<*>): Setting<V, S>?

    protected abstract fun getEntryFromValue(entryPath: ElementPath, defaultValue: V, value: V = defaultValue): Setting<V, S>

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, entryAmount=${this.entries.size}, initialized=$isInitialized, loadBeforeSave=$loadBeforeSave}"

}