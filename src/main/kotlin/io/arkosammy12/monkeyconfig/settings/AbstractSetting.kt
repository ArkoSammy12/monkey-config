package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath

abstract class AbstractSetting<T : Any, S : SerializableType<*>, I : AbstractSetting<T, S, I>>(
    settingBuilder: SettingBuilder<T, S, I, *>
) : Setting<T, S> {

    override val name: String = settingBuilder.name

    override val comment: String? = settingBuilder.comment

    override val path: ElementPath = settingBuilder.path

    protected val onInitializedFunction: ((I) -> Unit)? = settingBuilder.onInitialized

    protected val onSavedFunction: ((I) -> Unit)? = settingBuilder.onSaved

    protected val onUpdatedFunction: ((I) -> Unit)? = settingBuilder.onUpdated

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, path=$path, value=$value, comment=$comment}"

}