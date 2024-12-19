package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath

abstract class AbstractSetting<T : Any, S : SerializableType<*>>(
    settingBuilder: SettingBuilder<T, S>
) : Setting<T, S> {

    override val name: String = settingBuilder.name

    override val comment: String? = settingBuilder.comment

    override val path: ElementPath = settingBuilder.path

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, path=$path, value=$value, comment=$comment}"

}