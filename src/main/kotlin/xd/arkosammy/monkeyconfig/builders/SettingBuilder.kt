package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

abstract class SettingBuilder<T : Any, S : SerializableType<*>> internal constructor(
    val name: String,
    val path: ElementPath
) {

    var comment: String? = null

    lateinit var defaultValue: T

    open lateinit var serializer: (T) -> S

    open lateinit var deserializer: (S) -> T

    abstract fun build(): Setting<T, S>

}