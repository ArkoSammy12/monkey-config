package io.github.arkosammy12.monkeyconfig.base

import com.electronwill.nightconfig.core.ConfigFormat
import io.github.arkosammy12.monkeyconfig.util.ElementPath
import java.nio.file.Path

/**
 * An object used to manage and access [ConfigElement]s. It is associated to a [fileName], a [ConfigFormat], and a [filePath], which must include the file extension that corresopnds to the
 * file format dictated by [configFormat].
 */
interface ConfigManager : ConfigElementContainer {

    /**
     * The name of this [ConfigManager].
     */
    val fileName: String

    /**
     * The [ConfigFormat] of the configuration managed by this [ConfigManager].
     */
    val configFormat: ConfigFormat<*>

    /**
     * The file path of the configuration file managed by this [ConfigManager].
     */
    val filePath: Path

    /**
     * A list of [ConfigElement]s stored by this [ConfigManager]. Implementors should return an immutable copy of the underlying list
     * to avoid accidental mutation of this instance. If the desired goal is to mutate them, prefer to use one of the iteration methods from the [ConfigElementContainer] interface.
     *
     * By default, this property only contains the top level [ConfigElement]s and not the nested ones which are stored in [ConfigElementContainer]s.
     */
    override val configElements: List<ConfigElement>

    /**
     * Updates the [configElements] with values from the configuration file associated to this [ConfigManager].
     */
    fun loadFromFile(): Boolean

    /**
     * Writes the values of the [configElements] to the configuration file associated to this [ConfigManager].
     */
    fun saveToFile(): Boolean

    /**
     * Used to access a [Setting] that stores a value of type [V], whose implementation matches [T].
     *
     *  @param settingPath The [ElementPath] of the [Setting] being accessed.
     *  @param settingClass The [Class] instance that represents the type [T] to match for.
     *  @param V The type of the value stored by the [Setting] being accessed.
     *  @param T The specific implementation of [Setting] being accessed, which stores a value of type [V].
     */
    fun <V : Any, T : Setting<V, *>> getSetting(settingPath: ElementPath, settingClass: Class<T>): Setting<V, *>? =
        this.getConfigElement(settingPath, settingClass)

}

/**
 * Utility property for accessing the [ConfigElement]s of type [Section].
 */
val ConfigManager.sections: List<Section>
    get() {
        val sections: MutableList<Section> = mutableListOf()
        this.traverseElements<Section> { section -> sections.add(section) }
        return sections.toList()
    }

/**
 * Utility property for accessing the [ConfigElement]s of type [Setting].
 */
val ConfigManager.settings: List<Setting<*, *>>
    get() {
        val settings: MutableList<Setting<*, *>> = mutableListOf()
        this.traverseElements<Setting<*, *>> { setting -> settings.add(setting) }
        return settings.toList()
    }

/**
 * Used to access a [Setting] that stores a value of type [V], whose implementation matches [T].
 *
 *  @param settingPath The [ElementPath] of the [Setting] being accessed.
 *  @param V The type of the value stored by the [Setting] being accessed.
 *  @param T The specific implementation of [Setting] being accessed, which stores a value of type [V].
 */
inline fun <V : Any, reified T : Setting<V, *>> ConfigManager.getSetting(settingPath: ElementPath): Setting<V, *>? {
    return this.getSetting(settingPath, T::class.java)
}

/**
 * Utility method to traverse through the top level and nested [ConfigElement]s of type [Section].
 */
fun ConfigManager.traverseSections(action: (Section) -> Unit) {
    this.forEachElement<Section> { section ->
        section.traverseSections(action)
    }
}
