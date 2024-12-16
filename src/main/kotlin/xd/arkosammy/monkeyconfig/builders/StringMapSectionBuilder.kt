package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.sections.maps.MapSection
import xd.arkosammy.monkeyconfig.sections.maps.StringMapSection
import xd.arkosammy.monkeyconfig.types.StringType

open class StringMapSectionBuilder internal constructor(
    name: String,
    manager: ConfigManagerBuilder,
    parent: SectionBuilder? = null
) : MapSectionBuilder<String, StringType>(name, manager, parent) {

    override var serializer: (String) -> StringType = { stringValue -> StringType(stringValue) }

    override var deserializer: (StringType) -> String = { serializedStringValue -> serializedStringValue.value }

    override fun build(): MapSection<String, StringType> {
        return StringMapSection(this)
    }

}