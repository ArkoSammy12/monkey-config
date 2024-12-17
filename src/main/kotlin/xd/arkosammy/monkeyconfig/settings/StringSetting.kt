package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.values.SettingValue

class StringSetting(
    settingBuilder: SettingBuilder<String, StringType>
) : AbstractSetting<String, StringType>(settingBuilder) {

    override val value: SettingValue<String, StringType> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}