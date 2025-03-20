@file:JvmName("SectionUtils")

package io.github.arkosammy12.monkeyconfig.base

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig

interface Section : ConfigElement, ConfigElementContainer {

    override val configElements: List<ConfigElement>

    val loadBeforeSave: Boolean

    val isInitialized: Boolean

    fun saveWithDefaultValues(fileConfig: FileConfig) {
        this.forEachElement<Setting<*, *>> { setting -> setting.value.reset() }
        this.saveValue(fileConfig)
    }

    override fun saveValue(fileConfig: FileConfig) {
        if (this.loadBeforeSave) {
            this.updateValue(fileConfig)
        }
        this.forEachElement<ConfigElement> { element ->
            element.saveValue(fileConfig)
            element.onSaved()
        }
        this.comment?.let { comment ->
            if (fileConfig is CommentedFileConfig) fileConfig.setComment(this.path.string, comment)
        }
    }

    override fun updateValue(fileConfig: FileConfig) {
        this.forEachElement<ConfigElement> { element ->
            element.updateValue(fileConfig)
            element.onUpdated()
        }
    }

}

val Section.sections: List<Section>
    get() {
        val sections: MutableList<Section> = mutableListOf()
        this.traverseElements<Section> { section -> sections.add(section) }
        return sections.toList()
    }

val Section.settings: List<Setting<*, *>>
    get() {
        val settings: MutableList<Setting<*, *>> = mutableListOf()
        this.traverseElements<Setting<*, *>> { setting -> settings.add(setting) }
        return settings.toList()
    }

fun Section.traverseSections(action: (Section) -> Unit) {
    this.traverseElements<Section>(action = action)
}