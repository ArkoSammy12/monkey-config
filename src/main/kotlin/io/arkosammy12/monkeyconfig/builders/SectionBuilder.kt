package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.sections.DefaultSection
import io.arkosammy12.monkeyconfig.base.Section
import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class SectionBuilder(
    name: String,
    val manager: ConfigManagerBuilder,
    var parent: SectionBuilder? = null
) : ConfigElementBuilder<Section, SectionBuilder>(name) {

    override val path: ElementPath = this.getPath()

    open var loadBeforeSave: Boolean = false

    val configElementBuilders: List<ConfigElementBuilder<*, *>>
        get() = this.internalConfigElementBuilders.toList()

    protected val internalConfigElementBuilders: MutableList<ConfigElementBuilder<*, *>> = mutableListOf()

    override var implementation: (SectionBuilder) -> Section = ::DefaultSection

    fun <V : Any, S : SerializableType<*>, I : Setting<V, S>, T : SettingBuilder<V, S, I, T>> setting(settingName: String, defaultValue: V, builderInstanceProvider: (String, V, ElementPath) -> T, builder: T.() -> Unit): ElementPath {
        val path: ElementPath = this.path.withAppendedNode(settingName)
        val settingBuilder = builderInstanceProvider(settingName, defaultValue, path)
        builder(settingBuilder)
        this.internalConfigElementBuilders.add(settingBuilder)
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
        this.internalConfigElementBuilders.add(sectionBuilder)
    }

    fun <V : Any, S : SerializableType<*>, T : MapSectionBuilder<V, S>> mapSection(sectionName: String, builderInstanceProvider: (String, SectionBuilder) -> T, builder: T.() -> Unit): ElementPath {
        val mapSectionBuilder = builderInstanceProvider(sectionName, this)
        builder(mapSectionBuilder)
        this.internalConfigElementBuilders.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath =
        this.mapSection(sectionName, ::StringMapSectionBuilder, builder)

    private fun getPath(): ElementPath {
        var path = ElementPath(this.name)
        this.traverseToParent { sectionBuilder ->
            val sectionBuilderName = sectionBuilder.name
            if (!path.contains(sectionBuilderName)) {
                path = path.withPrependedNode(sectionBuilderName)
            }
        }
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

    override fun build(): Section =
        this.implementation(this)

}