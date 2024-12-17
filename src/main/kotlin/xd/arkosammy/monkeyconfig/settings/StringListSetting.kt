package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.values.SettingValue

open class StringListSetting(
    settingBuilder: SettingBuilder<List<String>, ListType<StringType>>
) : ListSetting<String, StringType>(settingBuilder) {

    override val value: SettingValue<List<String>, ListType<StringType>> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}