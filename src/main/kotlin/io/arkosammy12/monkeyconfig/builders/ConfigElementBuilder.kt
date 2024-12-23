package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.base.ConfigElement
import io.arkosammy12.monkeyconfig.util.ElementPath
import org.slf4j.Logger

/**
 * A builder class for [ConfigElement]s.
 *
 * @param I The specific implementation of [ConfigElement] that this builder instance builds.
 * @param T The specific implementation class when extended and represents the builder type used for the custom [implementation].
 */
sealed class ConfigElementBuilder<I : ConfigElement, T : ConfigElementBuilder<I, T>>(
    val name: String,
) {

    internal abstract val path: ElementPath

    internal open var logger: Logger? = null

    /**
     * An optional comment attached to the resulting [ConfigElement].
     */
    open var comment: String? = null

    /**
     * An optional listener triggered whenever the [io.arkosammy12.monkeyconfig.base.ConfigManager] associated with the
     * resulting [ConfigElement] calls [ConfigElement.onInitialized] on it.
     */
    open var onInitialized: ((I) -> Unit)? = null

    /**
     * An optional listener triggered whenever the [io.arkosammy12.monkeyconfig.base.ConfigManager] associated with the
     * resulting [ConfigElement] calls [ConfigElement.onSaved] on it.
     */
    open var onSaved: ((I) -> Unit)? = null

    /**
     * An optional listener triggered whenever the [io.arkosammy12.monkeyconfig.base.ConfigManager] associated with the
     * resulting [ConfigElement] calls [ConfigElement.onUpdated] on it.
     */
    open var onUpdated: ((I) -> Unit)? = null

    /**
     * Use this property to specify the actual [ConfigElement] implementation that will be used to build this [io.arkosammy12.monkeyconfig.builders.ConfigElementBuilder].
     * The builder used will be of type [T] and the resulting [ConfigElement] is of type [I].
     */
    open lateinit var implementation: (T) -> I

    internal abstract fun build(): I

}