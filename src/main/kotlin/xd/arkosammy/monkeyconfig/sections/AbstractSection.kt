package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.util.SettingPath

abstract class AbstractSection @JvmOverloads constructor(
    override val name: String,
    override val path: SettingPath,
    override val comment: String? = null,
    override val loadBeforeSave: Boolean = false,
) : Section {

    protected var internalIsRegistered: Boolean = false

    override val isRegistered: Boolean
        get() = this.internalIsRegistered

    override fun setRegistered() {
        this.internalIsRegistered = true
    }

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.settings.size}, subjectionAmount=${this.subsections.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave}"

}