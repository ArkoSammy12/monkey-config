package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.values.SettingValue

class StringSetting(
    settingBuilder: SettingBuilder<String, StringType>
) : AbstractSetting<String, StringType>(settingBuilder) {

    override val value: SettingValue<String, StringType> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}