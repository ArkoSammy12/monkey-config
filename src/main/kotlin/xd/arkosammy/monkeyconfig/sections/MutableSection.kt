package xd.arkosammy.monkeyconfig.sections

import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingPath

interface MutableSection : Section {

    fun <T : Any, S : SerializableType<*>> addSetting(setting: Setting<T, S>)

    fun removeSetting(settingPath: SettingPath): Boolean = false

    fun toImmutable(setting: List<Setting<*, *>>? = null): Section

}