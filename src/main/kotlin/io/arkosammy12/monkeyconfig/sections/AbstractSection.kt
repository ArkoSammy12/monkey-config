package io.arkosammy12.monkeyconfig.sections

import io.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.arkosammy12.monkeyconfig.util.ElementPath

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
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.settings.size}, subSectionAmount=${this.sections.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave}"

}