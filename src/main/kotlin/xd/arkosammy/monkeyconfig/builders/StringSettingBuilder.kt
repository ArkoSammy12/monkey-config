package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.settings.StringSetting
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class StringSettingBuilder(
    name: String,
    defaultValue: String,
    path: ElementPath
) : SettingBuilder<String, StringType>(name, defaultValue, path) {

    override var serializer: (String) -> StringType = ::StringType

    override var deserializer: (StringType) -> String = StringType::value

    override var implementation: (SettingBuilder<String, StringType>) -> Setting<String, StringType> = ::StringSetting

}