package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.builders.SectionBuilder
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.ElementPath

abstract class AbstractSection @JvmOverloads constructor(
    sectionBuilder: SectionBuilder,
    override val path: ElementPath
) : Section {

    override val name: String = sectionBuilder.name

    override val comment: String? = sectionBuilder.comment

    override val loadBeforeSave: Boolean = sectionBuilder.loadBeforeSave

    protected var internalIsRegistered: Boolean = false

    override val isRegistered: Boolean
        get() = this.internalIsRegistered

    override fun setRegistered() {
        this.internalIsRegistered = true
    }

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.configElements.filter { element -> element is Setting<*, *> }.size}, subjectionAmount=${this.configElements.filter { element -> element is Section }.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave}"

}