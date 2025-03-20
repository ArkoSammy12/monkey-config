package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.settings.StringSetting
import io.github.arkosammy12.monkeyconfig.types.StringType
import io.github.arkosammy12.monkeyconfig.util.ElementPath

/**
 * An implementation of [SettingBuilder] that builds [StringSetting] instances. Includes default implementations of
 * the setting's [serializer], [deserializer] and the [implementation], all of which can be changed.
 */
open class StringSettingBuilder(
    name: String,
    defaultValue: String,
    path: ElementPath
) : SettingBuilder<String, StringType, StringSetting, StringSettingBuilder>(name, defaultValue, path) {

    override var serializer: (String) -> StringType = ::StringType

    override var deserializer: (StringType) -> String = StringType::value

    override var implementation: (StringSettingBuilder) -> StringSetting = ::StringSetting

    override fun build(): StringSetting =
        this.implementation(this)

}