package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.SettingPath
import java.nio.file.Path

interface ConfigManager<C : Config> {

    val fileName: String

    val configFormat: ConfigFormat<C>

    val filePath: Path

    val sections: List<Section>

    fun reloadFromFile(): Boolean

    fun saveToFile(): Boolean

    fun <V, T : Setting<V, *>> getTypedSetting(settingPath: SettingPath, settingClass: Class<T>) : T?

    fun getSection(settingPath: SettingPath) : Section?

    // TODO: Write default implementation
    fun containsSection(sectionPath: SettingPath): Boolean

}