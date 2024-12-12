package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingPath
import xd.arkosammy.monkeyconfig.values.SettingValue

abstract class AbstractSetting<T : Any, S : SerializableType<*>> internal constructor(
    override val name: String,
    override val path: SettingPath,
    override val comment: String?,
    override val value: SettingValue<T, S>
) : Setting<T, S> {

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, path=$path, value=$value, comment$comment}"

}