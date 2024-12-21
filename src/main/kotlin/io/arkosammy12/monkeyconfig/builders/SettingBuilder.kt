package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath

abstract class SettingBuilder<V : Any, S : SerializableType<*>, I : Setting<V, S>, T : SettingBuilder<V, S, I, T>>(
    name: String,
    val defaultValue: V,
    override val path: ElementPath
) : ConfigElementBuilder<I, T>(name) {

    open lateinit var serializer: (V) -> S

    open lateinit var deserializer: (S) -> V

    override lateinit var implementation: (T) -> I

}