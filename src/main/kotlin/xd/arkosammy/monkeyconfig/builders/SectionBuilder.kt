package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingPath
import java.util.Deque
import java.util.LinkedList

class SectionBuilder internal constructor(val name: String, val manager: ConfigManagerBuilder<*>, var parent: SectionBuilder? = null) {

    var comment: String? = null

    var loadBeforeSave: Boolean = false

    var registerSettingsAsCommands: Boolean = false

    private val subSections: MutableList<SectionBuilder> = mutableListOf()

    private val settings: MutableList<SettingBuilder<*, *>> = mutableListOf()

    fun <T : Any, S : SerializableType<*>> setting(settingName: String, builder: SettingBuilder<T, S>.() -> Unit): SettingPath {
        val settingBuilder: SettingBuilder<T, S> = SettingBuilder(settingName)
        builder(settingBuilder)
        this.settings.add(settingBuilder)
        val strings: Deque<String> = LinkedList()
        strings.addFirst(settingBuilder.name)
        val path: List<String> = this.getSettingPath(strings)
        val settingPath: SettingPath = SettingPath.of(*path.toTypedArray())
        settingBuilder.settingPath = settingPath
        return settingPath
    }

    fun section(sectionName: String, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder: SectionBuilder = SectionBuilder(sectionName, this.manager, this)
        builder(sectionBuilder)
        this.subSections.add(sectionBuilder)
    }

    private fun getSettingPath(strings: Deque<String>): List<String> {
        strings.addFirst(this.name)
        if (this.parent != null) {
            this.getSettingPath(strings)
        }
        return strings.toList()
    }

    //TODO: Implement section building
    internal fun build() {


    }

}