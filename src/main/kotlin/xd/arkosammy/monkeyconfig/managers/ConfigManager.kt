package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.ConfigFormat
import xd.arkosammy.monkeyconfig.ConfigElement
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.sections.forEachSetting
import xd.arkosammy.monkeyconfig.sections.traverseSections
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.util.ElementPath
import java.nio.file.Path

interface ConfigManager {

    val fileName: String

    val configFormat: ConfigFormat<*>

    val filePath: Path

    val configElements: List<ConfigElement>

    fun reloadFromFile(): Boolean

    fun saveToFile(): Boolean

    fun <V : Any, T : Setting<V, *>> getSetting(elementPath: ElementPath, settingClass: Class<T>): Setting<V, *>?

    fun getSection(elementPath: ElementPath) : Section? {
        var returnedSection: Section? = null
        this.traverseSections { section ->
            if (section.path == elementPath) {
                returnedSection = section
                return@traverseSections
            }
        }
        return returnedSection
    }

    fun containsSection(sectionPath: ElementPath): Boolean =
        this.sections.any { section -> section.path == sectionPath }

    fun containsSetting(settingPath: ElementPath): Boolean =
        this.settings.any { setting -> setting.path == settingPath }

}

val  ConfigManager.sections: List<Section>
    get() {
        val sections: MutableList<Section> = mutableListOf()
        this.traverseSections { section -> sections.add(section) }
        return sections.toList()
    }

val  ConfigManager.settings: List<Setting<*, *>>
    get() {
        val settings: MutableList<Setting<*, *>> = mutableListOf()
        this.traverseSettings { setting -> settings.add(setting) }
        return settings.toList()
    }

inline fun <V : Any, reified T : Setting<V, *>> ConfigManager.getSetting(elementPath: ElementPath): Setting<V, *>? {
    return this.getSetting(elementPath, T::class.java)
}

fun ConfigManager.forEachSection(consumer: (Section) -> Unit) {
    for (element: ConfigElement in this.configElements) {
        if (element !is Section) {
            continue
        }
        consumer(element)
    }
}

fun ConfigManager.forEachSetting(consumer: (Setting<*, *>) -> Unit) {
    for (element: ConfigElement in this.configElements) {
        if (element !is Setting<*, *>) {
            continue
        }
        consumer(element)
    }
}

fun ConfigManager.traverseSections(action: (Section) -> Unit) {
    this.forEachSection { section ->
        section.traverseSections(action)
    }
}

fun ConfigManager.traverseSettings(action: (Setting<*, *>) -> Unit) {
    this.traverseSections { section ->
        section.forEachSetting { setting ->
            action(setting)
        }
    }
}