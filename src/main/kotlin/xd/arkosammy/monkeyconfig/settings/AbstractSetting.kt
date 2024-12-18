package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

abstract class AbstractSetting<T : Any, S : SerializableType<*>>(
    settingBuilder: SettingBuilder<T, S>
) : Setting<T, S> {

    override val name: String = settingBuilder.name

    override val comment: String? = settingBuilder.comment

    override val path: ElementPath = settingBuilder.path

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, path=$path, value=$value, comment=$comment}"

}