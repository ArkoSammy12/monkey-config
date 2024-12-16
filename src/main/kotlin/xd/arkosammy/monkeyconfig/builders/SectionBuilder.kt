package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.sections.DefaultSection
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class SectionBuilder internal constructor(
    val name: String,
    val manager: ConfigManagerBuilder,
    var parent: SectionBuilder? = null
) {

    var comment: String? = null

    var loadBeforeSave: Boolean = false

    internal val subSections: MutableList<SectionBuilder> = mutableListOf()

    internal val mapSections: MutableList<MapSectionBuilder<*, *>> = mutableListOf()

    internal val settings: MutableList<SettingBuilder<*, *>> = mutableListOf()

    internal val path: ElementPath = this.getPath()

    fun booleanSetting(settingName: String, builder: BooleanSettingBuilder.() -> Unit): ElementPath {
        return this.setting(settingName, builder, ::BooleanSettingBuilder)
    }

    fun <E : Enum<E>> enumSetting(settingName: String, builder: EnumSettingBuilder<E>.() -> Unit): ElementPath {
        return this.setting(settingName, builder, ::EnumSettingBuilder)
    }

    fun <E : Any, S : SerializableType<*>> listSetting(settingName: String, builder: ListSettingBuilder<E, S>.() -> Unit): ElementPath {
        return this.setting(settingName, builder, ::ListSettingBuilder)
    }

    fun <T : Number> numberSetting(settingName: String, builder: NumberSettingBuilder<T>.() -> Unit): ElementPath {
        return this.setting(settingName, builder, ::NumberSettingBuilder)
    }

    fun stringSetting(settingName: String, builder: StringSettingBuilder.() -> Unit): ElementPath {
        return this.setting(settingName, builder, ::StringSettingBuilder)
    }

    fun <T : Any, S : SerializableType<*>, B : SettingBuilder<T, S>> setting(settingName: String, builder: B.() -> Unit, builderInstanceProvider: (String, ElementPath) -> B): ElementPath {
        val path: ElementPath = this.path.withAppendedNode(settingName)
        val settingBuilder = builderInstanceProvider(settingName, path)
        builder(settingBuilder)
        this.settings.add(settingBuilder)
        return path
    }

    fun section(sectionName: String, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder = SectionBuilder(sectionName, this.manager, this)
        builder(sectionBuilder)
        this.subSections.add(sectionBuilder)
    }

    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath {
        val mapSectionBuilder = StringMapSectionBuilder(sectionName, this.manager, this)
        builder(mapSectionBuilder)
        this.mapSections.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    private fun getPath(): ElementPath {
        var path = ElementPath()
        this.traverseToParent { sectionBuilder -> path = path.withPrependedNode(sectionBuilder.name) }
        return path
    }

    private fun traverseToParent(section: SectionBuilder? = null, consumer: (SectionBuilder) -> Unit) {
        val startingSection: SectionBuilder = section ?: this.also { currentSectionBuilder ->
            consumer(currentSectionBuilder)
        }
        val parent: SectionBuilder? = startingSection.parent
        if (parent == null) {
            return
        }
        consumer(parent)
        traverseToParent(parent, consumer)
    }

    //TODO: Implement section building
    internal fun build(): Section {
        return DefaultSection(this, this.path)
    }

}