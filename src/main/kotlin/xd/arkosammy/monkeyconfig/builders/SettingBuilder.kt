package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

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