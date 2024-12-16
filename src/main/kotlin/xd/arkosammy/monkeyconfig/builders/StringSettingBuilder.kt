package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.settings.StringSetting
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class StringSettingBuilder(
    name: String,
    path: ElementPath
) : SettingBuilder<String, StringType>(name, path) {

    override var serializer: (String) -> StringType = { stringValue -> StringType(stringValue) }

    override var deserializer: (StringType) -> String = { serializedStringValue -> serializedStringValue.value }

    override fun build(): Setting<String, StringType> {
        return StringSetting(this)
    }

}