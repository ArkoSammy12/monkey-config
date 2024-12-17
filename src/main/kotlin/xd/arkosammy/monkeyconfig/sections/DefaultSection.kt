package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.builders.SectionBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder

class DefaultSection(
    sectionBuilder: SectionBuilder,
) : AbstractSection(sectionBuilder) {

    override val configElements: List<ConfigElement>

    init {
        val configElements: MutableList<ConfigElement> = mutableListOf()
        for (settingBuilder: SettingBuilder<*, *> in sectionBuilder.settings) {
            configElements.add(settingBuilder.build())
        }
        for (sectionBuilder: SectionBuilder in sectionBuilder.subSections) {
             configElements.add(sectionBuilder.build())
        }
        this.configElements = configElements.toList()

        // Prevent accidental duplicates
        // TODO: TEST THIS
        sectionBuilder.settings.clear()
        sectionBuilder.subSections.clear()
    }

}