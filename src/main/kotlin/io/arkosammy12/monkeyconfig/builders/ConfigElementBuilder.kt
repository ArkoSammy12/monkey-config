package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.base.ConfigElement
import io.arkosammy12.monkeyconfig.util.ElementPath

sealed class ConfigElementBuilder<I : ConfigElement, T : ConfigElementBuilder<I, T>>(
    val name: String,
) {

    internal abstract val path: ElementPath

    open var comment: String? = null

    open var onInitialized: ((I) -> Unit)? = null

    open var onSaved: ((I) -> Unit)? = null

    open var onUpdated: ((I) -> Unit)? = null

    open lateinit var implementation: (T) -> I

    internal abstract fun build(): I

}