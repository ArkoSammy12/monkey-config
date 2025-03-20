package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.settings.EnumSetting
import io.github.arkosammy12.monkeyconfig.types.EnumType
import io.github.arkosammy12.monkeyconfig.util.ElementPath

/**
 * An implementation of [SettingBuilder] that builds [EnumSetting] instances. Includes default implementations of
 * the setting's [serializer], [deserializer] and the [implementation], all of which can be changed.
 *
 * @param E The type of the Enum instance held by the resulting [EnumSetting].
 */
open class EnumSettingBuilder<E : Enum<E>>(
    name: String,
    defaultValue: E,
    path: ElementPath
) : SettingBuilder<E, EnumType<E>, EnumSetting<E>, EnumSettingBuilder<E>>(name, defaultValue, path) {

    override var serializer: (E) -> EnumType<E> = { value -> EnumType(value) }

    override var deserializer: (EnumType<E>) -> E = EnumType<E>::value

    override var implementation: (EnumSettingBuilder<E>) -> EnumSetting<E> = ::EnumSetting

    override fun build(): EnumSetting<E> =
        this.implementation(this)

}