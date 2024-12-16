package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.ListSettingBuilder
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.values.SettingValue

open class StringListSetting(
    settingBuilder: ListSettingBuilder<String, StringType>
) : ListSetting<String, StringType>(settingBuilder) {

    override val value: SettingValue<List<String>, ListType<StringType>> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}