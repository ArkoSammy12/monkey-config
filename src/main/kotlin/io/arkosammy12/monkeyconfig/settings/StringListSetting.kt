package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.ListSettingBuilder
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class StringListSetting(
    settingBuilder: ListSettingBuilder<String, StringType>
) : ListSetting<String, StringType>(settingBuilder) {

    override val value: SettingValue<List<String>, ListType<StringType>> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}