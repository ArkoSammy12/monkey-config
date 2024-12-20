package io.arkosammy12.monkeyconfig.managers

import com.electronwill.nightconfig.core.ConfigFormat
import io.arkosammy12.monkeyconfig.ConfigElement
import io.arkosammy12.monkeyconfig.ConfigElementContainer
import io.arkosammy12.monkeyconfig.forEachElement
import io.arkosammy12.monkeyconfig.sections.Section
import io.arkosammy12.monkeyconfig.sections.traverseSections
import io.arkosammy12.monkeyconfig.settings.Setting
import io.arkosammy12.monkeyconfig.traverseElements
import io.arkosammy12.monkeyconfig.util.ElementPath
import java.nio.file.Path

interface ConfigManager : ConfigElementContainer {

    val fileName: String

    val configFormat: ConfigFormat<*>

    val filePath: Path

    override val configElements: List<ConfigElement>

    fun loadFromFile(): Boolean

    fun saveToFile(): Boolean

    fun <V : Any, T : Setting<V, *>> getSetting(settingPath: ElementPath, settingClass: Class<T>): Setting<V, *>? =
        this.getConfigElement(settingPath, settingClass)

}

val ConfigManager.sections: List<Section>
    get() {
        val sections: MutableList<Section> = mutableListOf()
        this.traverseElements<Section> { section -> sections.add(section) }
        return sections.toList()
    }

val ConfigManager.settings: List<Setting<*, *>>
    get() {
        val settings: MutableList<Setting<*, *>> = mutableListOf()
        this.traverseElements<Setting<*, *>> { setting -> settings.add(setting) }
        return settings.toList()
    }

inline fun <V : Any, reified T : Setting<V, *>> ConfigManager.getSetting(settingPath: ElementPath): Setting<V, *>? {
    return this.getSetting(settingPath, T::class.java)
}


fun ConfigManager.traverseSections(action: (Section) -> Unit) {
    this.forEachElement<Section> { section ->
        section.traverseSections(action)
    }
}
