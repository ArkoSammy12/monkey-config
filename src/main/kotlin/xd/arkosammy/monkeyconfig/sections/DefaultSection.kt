package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.SettingPath

class DefaultSection @JvmOverloads constructor(
    name: String,
    path: SettingPath,
    comment: String? = null,
    override val settings: List<Setting<*, *>>,
    override val subsections: List<Section>,
    loadBeforeSave: Boolean = false,
) : AbstractSection(name, path, comment, loadBeforeSave) {
}