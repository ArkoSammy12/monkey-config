package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class SettingBuilder<T : Any, S : SerializableType<*>>(
    val name: String,
    val defaultValue: T,
    val path: ElementPath
) {

    open var comment: String? = null

    open lateinit var serializer: (T) -> S

    open lateinit var deserializer: (S) -> T

    open lateinit var implementation: (SettingBuilder<T, S>) -> Setting<T, S>

    internal open fun build(): Setting<T, S> =
        this.implementation(this)

}