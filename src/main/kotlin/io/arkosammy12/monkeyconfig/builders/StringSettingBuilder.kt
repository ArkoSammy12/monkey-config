package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.settings.StringSetting
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class StringSettingBuilder(
    name: String,
    defaultValue: String,
    path: ElementPath
) : SettingBuilder<String, StringType>(name, defaultValue, path) {

    override var serializer: (String) -> StringType = ::StringType

    override var deserializer: (StringType) -> String = StringType::value

    override var implementation: (SettingBuilder<String, StringType>) -> Setting<String, StringType> = ::StringSetting

}