package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath
import org.slf4j.Logger

/**
 * Providers a default abstract implementation for [Setting] that serves as a common parent class for the more concrete
 * default [Setting] implementations corresponding to the basic serializable data types.
 *
 * @param V The type of the value that is stored by this instance.
 * @param S The [SerializableType] that will be used to serialize and deserialize the value [V] to and from a configuration file.
 * @param I The specific implementation of [io.arkosammy12.monkeyconfig.settings.AbstractSetting] that will be used for the callback functions.
 */
abstract class AbstractSetting<V : Any, S : SerializableType<*>, I : AbstractSetting<V, S, I>>(
    settingBuilder: SettingBuilder<V, S, I, *>
) : Setting<V, S> {

    override val name: String = settingBuilder.name

    override val comment: String? = settingBuilder.comment

    override val path: ElementPath = settingBuilder.path

    protected val onInitializedFunction: ((I) -> Unit)? = settingBuilder.onInitialized

    protected val onSavedFunction: ((I) -> Unit)? = settingBuilder.onSaved

    protected val onUpdatedFunction: ((I) -> Unit)? = settingBuilder.onUpdated

    protected val logger: Logger? =  settingBuilder.logger

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, path=$path, value=$value, comment=$comment}"

}