package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.sections.maps.MapSection
import io.arkosammy12.monkeyconfig.sections.maps.StringMapSection
import io.arkosammy12.monkeyconfig.types.StringType

open class StringMapSectionBuilder(
    name: String,
    parent: SectionBuilder? = null
) : MapSectionBuilder<String, StringType>(name, parent) {

    override var serializer: (String) -> StringType = ::StringType

    override var deserializer: (StringType) -> String = StringType::value

    override var implementation: (MapSectionBuilder<String, StringType>) -> MapSection<String, StringType> = ::StringMapSection

}