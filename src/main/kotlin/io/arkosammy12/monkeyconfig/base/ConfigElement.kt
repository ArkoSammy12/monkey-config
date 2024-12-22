package io.arkosammy12.monkeyconfig.base

import com.electronwill.nightconfig.core.file.FileConfig
import io.arkosammy12.monkeyconfig.util.ElementPath

/**
 * Represents an arbitrary named element within a configuration file, which can be accessed by its [ElementPath], which can
 * have a comment. A [ConfigElement] performs a "save" or an "update" operation triggered by the [ConfigManager] that contains it, and can
 * optionally react to those operations
 */
sealed interface ConfigElement {

    /**
     * The name of the [ConfigElement]. It corresponds to the last node of this [ConfigElement]'s path.
     */
    val name: String

    /**
     * An optional comment associated with this [ConfigElement].
     * Note that this property will not matter if a [com.electronwill.nightconfig.core.file.CommentedFileConfig] is not being used.
     */
    val comment: String?

    /**
     * The path of the [ConfigElement], which used to access it given a [FileConfig].
     */
    val path: ElementPath

    /**
     * Uses the values currently stored in this [ConfigElement] to update the [fileConfig] with the current values.
     * This method is called prior to the [fileConfig] being written to an actual configuration file.
     * If [fileConfig] is a [com.electronwill.nightconfig.core.file.CommentedFileConfig], then it also
     * updates the section's [comment] in the configuration file.
     */
    fun saveValue(fileConfig: FileConfig)

    /**
     * Reads the values stored in the [fileConfig] to update the values stored in this [ConfigElement].
     * This method is called after the [fileConfig] is read from an actual configuration file.
     */
    fun updateValue(fileConfig: FileConfig)

    /**
     * Called whenever the [ConfigManager] associated with this [ConfigElement] builds and initially loads it.
     */
    fun onInitialized() {}

    /**
     * Called whenever the [ConfigManager] or [ConfigElementContainer] associated with this [ConfigElement] calls [ConfigElement.updateValue]
     * on it.
     */
    fun onUpdated() {}

    /**
     * Called whenever the [ConfigManager] or [ConfigElementContainer] associated with this [ConfigElement] calls [ConfigElement.saveValue]
     * on it.
     */
    fun onSaved() {}

}