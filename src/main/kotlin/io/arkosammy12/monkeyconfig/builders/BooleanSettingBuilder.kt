package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.BooleanSetting
import io.arkosammy12.monkeyconfig.types.BooleanType
import io.arkosammy12.monkeyconfig.util.ElementPath

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