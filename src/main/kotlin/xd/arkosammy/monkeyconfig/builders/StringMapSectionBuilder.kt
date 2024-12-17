package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.sections.maps.MapSection
import xd.arkosammy.monkeyconfig.sections.maps.StringMapSection
import xd.arkosammy.monkeyconfig.types.StringType

open class StringMapSectionBuilder(
    name: String,
    parent: SectionBuilder? = null
) : MapSectionBuilder<String, StringType>(name, parent) {

    override var serializer: (String) -> StringType = { stringValue -> StringType(stringValue) }

    override var deserializer: (StringType) -> String = { serializedStringValue -> serializedStringValue.value }

    override var implementation: (MapSectionBuilder<String, StringType>) -> MapSection<String, StringType> = ::StringMapSection

}