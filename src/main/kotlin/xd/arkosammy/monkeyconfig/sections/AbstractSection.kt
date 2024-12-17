package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.builders.SectionBuilder
import xd.arkosammy.monkeyconfig.util.ElementPath

abstract class AbstractSection(
    sectionBuilder: SectionBuilder,
) : Section {

    override val name: String = sectionBuilder.name

    override val path: ElementPath = sectionBuilder.path

    override val comment: String? = sectionBuilder.comment

    override val loadBeforeSave: Boolean = sectionBuilder.loadBeforeSave

    protected var internalIsRegistered: Boolean = false

    override val isRegistered: Boolean
        get() = this.internalIsRegistered

    override fun setRegistered() {
        this.internalIsRegistered = true
    }

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.settings.size}, subSectionAmount=${this.subSections.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave}"

}