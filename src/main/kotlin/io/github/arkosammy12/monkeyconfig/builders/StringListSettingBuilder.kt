package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.settings.ListSetting
import io.github.arkosammy12.monkeyconfig.settings.StringListSetting
import io.github.arkosammy12.monkeyconfig.types.ListType
import io.github.arkosammy12.monkeyconfig.types.StringType
import io.github.arkosammy12.monkeyconfig.util.ElementPath

/**
 * An implementation of [ListSettingBuilder] that builds [StringListSetting] instances. Includes default implementations of
 * the setting's [serializer], [deserializer] and the [implementation], all of which can be changed.
 */
open class StringListSettingBuilder(
    name: String,
    defaultValue: List<String>,
    path: ElementPath
) : ListSettingBuilder<String, StringType>(name, defaultValue, path) {

    override var serializer: (List<String>) -> ListType<StringType> = { value -> ListType(value.map(::StringType).toList()) }

    override var deserializer: (ListType<StringType>) -> List<String> = { serializedValue -> serializedValue.rawList.map { e -> e.toString() }.toList() }

    override var implementation: (ListSettingBuilder<String, StringType>) -> ListSetting<String, StringType> = ::StringListSetting
}