package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.settings.StringListSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class StringListSettingBuilder(
    name: String,
    defaultValue: List<String>,
    path: ElementPath
) : ListSettingBuilder<String, StringType>(name, defaultValue, path) {

    override var serializer: (List<String>) -> ListType<StringType> = { value -> ListType(value.map(::StringType).toList()) }

    override var deserializer: (ListType<StringType>) -> List<String> = { serializedValue -> serializedValue.rawList.map { e -> e.toString() }.toList() }

    override var implementation: (SettingBuilder<List<String>, ListType<StringType>>) -> Setting<List<String>, ListType<StringType>> = ::StringListSetting
}