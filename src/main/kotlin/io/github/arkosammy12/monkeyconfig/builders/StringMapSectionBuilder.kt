package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.sections.maps.MapSection
import io.github.arkosammy12.monkeyconfig.sections.maps.StringMapSection
import io.github.arkosammy12.monkeyconfig.types.StringType

/**
 * An implementation of [MapSectionBuilder] that builds [StringMapSection] instances. Includes default implementations of
 * the setting's [serializer], [deserializer] and the [implementation], all of which can be changed.
 */
open class StringMapSectionBuilder(
    name: String,
    parent: SectionBuilder? = null
) : MapSectionBuilder<String, StringType>(name, parent) {

    override var serializer: (String) -> StringType = ::StringType

    override var deserializer: (StringType) -> String = StringType::value

    override var implementation: (MapSectionBuilder<String, StringType>) -> MapSection<String, StringType> = ::StringMapSection

}