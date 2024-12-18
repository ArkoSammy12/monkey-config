package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.builders.MapSectionBuilder
import xd.arkosammy.monkeyconfig.builders.SectionBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder

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