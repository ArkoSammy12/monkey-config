package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.sections.DefaultSection
import xd.arkosammy.monkeyconfig.sections.Section
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class SectionBuilder(
    val name: String,
    val manager: ConfigManagerBuilder,
    var parent: SectionBuilder? = null
) {

    open var comment: String? = null

    open var loadBeforeSave: Boolean = false

    open var implementation: (SectionBuilder) -> Section = ::DefaultSection

    internal val subSections: MutableList<SectionBuilder> = mutableListOf()

    internal val mapSections: MutableList<MapSectionBuilder<*, *>> = mutableListOf()

    internal val settings: MutableList<SettingBuilder<*, *>> = mutableListOf()

    internal val path: ElementPath = this.getPath()

    fun <T : Any, S : SerializableType<*>, B : SettingBuilder<T, S>> setting(settingName: String, defaultValue: T, builderInstanceProvider: (String, T, ElementPath) -> B, builder: B.() -> Unit): ElementPath {
        val path: ElementPath = this.path.withAppendedNode(settingName)
        val settingBuilder = builderInstanceProvider(settingName, defaultValue, path)
        builder(settingBuilder)
        this.settings.add(settingBuilder)
        return path
    }

    fun booleanSetting(settingName: String, defaultValue: Boolean, builder: BooleanSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::BooleanSettingBuilder, builder)

    fun stringSetting(settingName: String, defaultValue: String, builder: StringSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::StringSettingBuilder, builder)

    fun <T : Number> numberSetting(settingName: String, defaultValue: T, builder: NumberSettingBuilder<T>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::NumberSettingBuilder, builder)

    fun <E : Any, S : SerializableType<*>> listSetting(settingName: String, defaultValue: List<E>, builder: ListSettingBuilder<E, S>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::ListSettingBuilder, builder)

    fun stringListSetting(settingName: String, defaultValue: List<String>, builder: StringListSettingBuilder.() -> Unit): ElementPath  =
        this.listSetting<String, StringType>(settingName, defaultValue) {
            builder(this as StringListSettingBuilder)
        }

    fun <E : Enum<E>> enumSetting(settingName: String, defaultValue: E, builder: EnumSettingBuilder<E>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::EnumSettingBuilder, builder)

    @JvmOverloads
    fun section(sectionName: String, builderInstanceProvider: (String, ConfigManagerBuilder, SectionBuilder?) -> SectionBuilder = ::SectionBuilder, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder: SectionBuilder = builderInstanceProvider(sectionName, this.manager, this)
        builder(sectionBuilder)
        this.subSections.add(sectionBuilder)
    }

    fun <V : Any, S : SerializableType<*>, B : MapSectionBuilder<V, S>> mapSection(sectionName: String, builderInstanceProvider: (String, SectionBuilder) -> B, builder: B.() -> Unit): ElementPath {
        val mapSectionBuilder = builderInstanceProvider(sectionName, this)
        builder(mapSectionBuilder)
        this.mapSections.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath =
        this.mapSection(sectionName, ::StringMapSectionBuilder, builder)

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

    internal open fun build(): Section =
        this.implementation(this)

}