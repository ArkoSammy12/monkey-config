package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.StringSetting
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.util.ElementPath

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