package io.arkosammy12.monkeyconfig.sections

import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.builders.MapSectionBuilder
import io.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.arkosammy12.monkeyconfig.builders.SettingBuilder

class DefaultSection(
    sectionBuilder: SectionBuilder,
) : AbstractSection(sectionBuilder) {

    override val configElements: List<ConfigElement>

    init {
        val configElements: MutableList<ConfigElement> = mutableListOf()
        for (settingBuilder: SettingBuilder<*, *> in sectionBuilder.settingBuilders) {
            configElements.add(settingBuilder.build())
        }
        for (sectionBuilder: SectionBuilder in sectionBuilder.sectionBuilders) {
             configElements.add(sectionBuilder.build())
        }
        for (mapSectionBuilder: MapSectionBuilder<*, *> in sectionBuilder.mapSectionBuilders) {
            configElements.add(mapSectionBuilder.build())
        }
        this.configElements = configElements.toList()

        // Prevent accidental duplicates
        // TODO: TEST THIS
        //sectionBuilder.internalSettingBuilders.clear()
        //sectionBuilder.subSections.clear()
    }

}