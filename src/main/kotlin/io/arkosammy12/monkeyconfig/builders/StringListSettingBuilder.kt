package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.settings.StringListSetting
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class StringListSettingBuilder(
    name: String,
    defaultValue: List<String>,
    path: ElementPath
) : ListSettingBuilder<String, StringType>(name, defaultValue, path) {

    override var serializer: (List<String>) -> ListType<StringType> = { value -> ListType(value.map(::StringType).toList()) }

    override var deserializer: (ListType<StringType>) -> List<String> = { serializedValue -> serializedValue.rawList.map { e -> e.toString() }.toList() }

    override var implementation: (SettingBuilder<List<String>, ListType<StringType>>) -> Setting<List<String>, ListType<StringType>> = ::StringListSetting
}