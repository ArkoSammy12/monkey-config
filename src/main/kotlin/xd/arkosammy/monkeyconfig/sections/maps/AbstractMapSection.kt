package xd.arkosammy.monkeyconfig.sections.maps

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.builders.MapSectionBuilder
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.toSerializedType
import xd.arkosammy.monkeyconfig.util.ElementPath

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

    protected var internalIsRegistered: Boolean = false

    init {
        val mapEntries: MutableList<Setting<V, S>> = mutableListOf()
        val defaultMapEntries: MutableList<Setting<V, S>> = mutableListOf()
        for ((key, value) in mapSectionBuilder.defaultEntries) {
            val entry: Setting<V, S> = this.getEntryFromValue(this.path, value)
            defaultMapEntries.add(entry)
            mapEntries.add(entry)
        }
        this.entries = mapEntries
        this.defaultEntries = defaultMapEntries.toList()
    }

    override val isRegistered: Boolean
        get() = this.internalIsRegistered

    override fun setRegistered() {
        this.internalIsRegistered = true
    }

    final override val loadBeforeSave: Boolean
        get() = true

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

    override fun setDefaultValues(fileConfig: FileConfig) {
        this.entries.clear()
        this.entries.addAll(this.defaultEntries)
        this.setValues(fileConfig)
    }

    override fun setValues(fileConfig: FileConfig) {
        for (entry: Setting<V, S> in this.entries) {
            val serialized: S = entry.value.serialized
            fileConfig.set<Any>(entry.path.string, if (serialized is ListType<*>) serialized.rawList else serialized.value)
        }
        this.comment?.let { comment ->
            if (fileConfig is CommentedFileConfig) fileConfig.setComment(this.path.string, comment)
        }
    }

    override fun loadValues(fileConfig: FileConfig) {
        val config: Config = fileConfig.get(this.path.string) ?: run {
            // TODO: LOG
            return
        }
        val tempEntries: MutableList<Setting<V, S>> = mutableListOf()
        for (entry: Config.Entry in config.entrySet()) {
            val rawEntryValue: Any = entry.getValue()
            val serializedEntryValue: SerializableType<*> = toSerializedType(rawEntryValue)
            val newMapEntry: Setting<V, S>? = this.getEntryFromSerialized(ElementPath(*this.path.asArray, entry.key), serializedEntryValue)
            if (newMapEntry == null) {
                // TODO: LOG
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
        "${this::class.simpleName}{name=$name, comment=$comment, entryAmount=${this.entries.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave}"

}