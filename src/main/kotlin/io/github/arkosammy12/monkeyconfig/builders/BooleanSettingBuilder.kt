package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.settings.BooleanSetting
import io.github.arkosammy12.monkeyconfig.types.BooleanType
import io.github.arkosammy12.monkeyconfig.util.ElementPath

/**
 * An implementation of [SettingBuilder] that builds [BooleanSetting] instances. Includes default implementations of
 * the setting's [serializer], [deserializer] and the [implementation], all of which can be changed.
 */
open class BooleanSettingBuilder(
    name: String,
    defaultValue: Boolean,
    path: ElementPath
) : SettingBuilder<Boolean, BooleanType, BooleanSetting, BooleanSettingBuilder>(name, defaultValue, path) {

    override var serializer: (Boolean) -> BooleanType = ::BooleanType

    override var deserializer: (BooleanType) -> Boolean = BooleanType::value

    override var implementation: (BooleanSettingBuilder) -> BooleanSetting = ::BooleanSetting

    override fun build(): BooleanSetting =
        this.implementation(this)

}