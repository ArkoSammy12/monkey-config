package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.sections.DefaultSection
import io.arkosammy12.monkeyconfig.base.Section
import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.util.ElementPath

/**
 * A class which builds [Section]s.
 */
open class SectionBuilder(
    name: String,
    internal val manager: ConfigManagerBuilder,
    internal var parent: SectionBuilder? = null
) : ConfigElementBuilder<Section, SectionBuilder>(name) {

    override val path: ElementPath = this.getPath()

    open var loadBeforeSave: Boolean = false

    /**
     * An immutable view of the [ConfigElementBuilder]s currently held by this [io.arkosammy12.monkeyconfig.builders.SectionBuilder].
     * Does not include nested instances.
     */
    val configElementBuilders: List<ConfigElementBuilder<*, *>>
        get() = this.internalConfigElementBuilders.toList()

    protected val internalConfigElementBuilders: MutableList<ConfigElementBuilder<*, *>> = mutableListOf()

    override var implementation: (SectionBuilder) -> Section = ::DefaultSection

    init {
        this.logger = parent?.logger
    }

    /**
     * Adds a new [Setting] to this [io.arkosammy12.monkeyconfig.builders.SectionBuilder] via its [SettingBuilder].
     *
     * @param settingName The name of the setting. Must not include "**.**".
     * @param defaultValue The default value of the setting, which will be used as the setting's starting value.
     * @param builderInstanceProvider A provider that returns the specific implementation [T] of [SettingBuilder] that should be used to build this [Setting].
     * @param builder The actual builder function for the [SettingBuilder].
     * @param V the type of the value held by the resulting [Setting].
     * @param S The [SerializableType] of the value used by this [Setting].
     * @param I The specific [Setting] implementation that the [SettingBuilder] returned by [builderInstanceProvider] builds to.
     * @param T The specific [SettingBuilder] implementation returned by [builderInstanceProvider].
     *
     * @return The [ElementPath] associated to the resulting [Setting] when built and added to a [io.arkosammy12.monkeyconfig.base.ConfigManager],
     * which can be used to access it later.
     */
    fun <V : Any, S : SerializableType<*>, I : Setting<V, S>, T : SettingBuilder<V, S, I, T>> setting(settingName: String, defaultValue: V, builderInstanceProvider: (String, V, ElementPath) -> T, builder: T.() -> Unit): ElementPath {
        val path: ElementPath = this.path.withAppendedNode(settingName)
        val settingBuilder = builderInstanceProvider(settingName, defaultValue, path)
        builder(settingBuilder)
        settingBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(settingBuilder)
        return path
    }

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.settings.BooleanSetting] using a [BooleanSettingBuilder].
     *
     * @param settingName The name of the built [io.arkosammy12.monkeyconfig.settings.BooleanSetting].
     * @param defaultValue The default value for the built [io.arkosammy12.monkeyconfig.settings.BooleanSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [BooleanSettingBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.settings.BooleanSetting].
     */
    fun booleanSetting(settingName: String, defaultValue: Boolean, builder: BooleanSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::BooleanSettingBuilder, builder)

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.settings.StringSetting] using a [StringSettingBuilder].
     *
     * @param settingName The name of the built [io.arkosammy12.monkeyconfig.settings.StringSetting].
     * @param defaultValue The default value for the built [io.arkosammy12.monkeyconfig.settings.StringSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [StringSettingBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.settings.StringSetting].
     */
    fun stringSetting(settingName: String, defaultValue: String, builder: StringSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::StringSettingBuilder, builder)

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.settings.NumberSetting] using a [NumberSettingBuilder].
     *
     * @param settingName The name of the built [io.arkosammy12.monkeyconfig.settings.NumberSetting].
     * @param defaultValue The default value for the built [io.arkosammy12.monkeyconfig.settings.NumberSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [NumberSettingBuilder].
     * @param T The specific numerical type of the resulting [io.arkosammy12.monkeyconfig.settings.NumberSetting].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.settings.NumberSetting].
     */
    fun <T : Number> numberSetting(settingName: String, defaultValue: T, builder: NumberSettingBuilder<T>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::NumberSettingBuilder, builder)

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.settings.ListSetting] using a [ListSettingBuilder].
     *
     * @param settingName The name of the built [io.arkosammy12.monkeyconfig.settings.ListSetting].
     * @param defaultValue The default value for the built [io.arkosammy12.monkeyconfig.settings.ListSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [ListSettingBuilder].
     * @param E The type of the elements held by the resulting [io.arkosammy12.monkeyconfig.settings.ListSetting].
     * @param S The [SerializableType] of the element when serialized to be written and read from a configuration file.
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.settings.ListSetting].
     */
    fun <E : Any, S : SerializableType<*>> listSetting(settingName: String, defaultValue: List<E>, builder: ListSettingBuilder<E, S>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::ListSettingBuilder, builder)

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.settings.StringListSetting] using a [StringListSettingBuilder].
     *
     * @param settingName The name of the built [io.arkosammy12.monkeyconfig.settings.StringListSetting].
     * @param defaultValue The default value for the built [io.arkosammy12.monkeyconfig.settings.StringListSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [StringListSettingBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.settings.StringListSetting].
     */
    fun stringListSetting(settingName: String, defaultValue: List<String>, builder: ListSettingBuilder<String, StringType>.() -> Unit): ElementPath  =
        this.setting(settingName, defaultValue, ::StringListSettingBuilder, builder)

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.settings.EnumSetting] using a [EnumSettingBuilder].
     *
     * @param settingName The name of the built [io.arkosammy12.monkeyconfig.settings.EnumSetting].
     * @param defaultValue The default value for the built [io.arkosammy12.monkeyconfig.settings.EnumSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [EnumSettingBuilder].
     * @param E The type of the enum instance held by the resulting [io.arkosammy12.monkeyconfig.settings.EnumSetting].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.settings.EnumSetting].
     */
    fun <E : Enum<E>> enumSetting(settingName: String, defaultValue: E, builder: EnumSettingBuilder<E>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::EnumSettingBuilder, builder)

    /**
     * Adds new a [Section] to this [io.arkosammy12.monkeyconfig.builders.SectionBuilder] via its [io.arkosammy12.monkeyconfig.builders.SectionBuilder].
     *
     * @param sectionName The name of the section being added. It cannot contain "**.**".
     * @param builderInstanceProvider A provider that returns the specific implementation of [io.arkosammy12.monkeyconfig.builders.SectionBuilder] to be used to build this [Section]. By default, this returns a regular [io.arkosammy12.monkeyconfig.builders.SectionBuilder].
     * @param builder The builder function used to configure the [io.arkosammy12.monkeyconfig.builders.SectionBuilder].
     *
     * @return The [ElementPath] associated to the resulting [Section].
     */
    @JvmOverloads
    fun section(sectionName: String, builderInstanceProvider: (String, ConfigManagerBuilder, SectionBuilder?) -> SectionBuilder = ::SectionBuilder, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder: SectionBuilder = builderInstanceProvider(sectionName, this.manager, this)
        builder(sectionBuilder)
        sectionBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(sectionBuilder)
    }

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.sections.maps.MapSection] to this [io.arkosammy12.monkeyconfig.builders.SectionBuilder] via its [MapSectionBuilder].
     *
     * @param sectionName The name of the map section being added. It cannot contain "**.**".
     * @param builderInstanceProvider A provider that returns the specific implementation of [io.arkosammy12.monkeyconfig.builders.MapSectionBuilder] to be used to build this [io.arkosammy12.monkeyconfig.sections.maps.MapSection].
     * @param builder The builder function used to configure the [io.arkosammy12.monkeyconfig.builders.MapSectionBuilder].
     * @param V The type of the values held by the resulting [io.arkosammy12.monkeyconfig.sections.maps.MapSection].
     * @param S The [SerializableType] used for serializing the values held by the resulting [io.arkosammy12.monkeyconfig.sections.maps.MapSection].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.sections.maps.MapSection].    
     */
    fun <V : Any, S : SerializableType<*>, T : MapSectionBuilder<V, S>> mapSection(sectionName: String, builderInstanceProvider: (String, SectionBuilder) -> T, builder: T.() -> Unit): ElementPath {
        val mapSectionBuilder = builderInstanceProvider(sectionName, this)
        builder(mapSectionBuilder)
        mapSectionBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    /**
     * Adds a new [io.arkosammy12.monkeyconfig.sections.maps.StringMapSection] to this [io.arkosammy12.monkeyconfig.builders.SectionBuilder] via its [StringMapSectionBuilder].
     *
     * @param sectionName The name of the map section being added. It cannot contain "**.**".
     * @param builder The builder function used to configure the [io.arkosammy12.monkeyconfig.builders.StringMapSectionBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.arkosammy12.monkeyconfig.sections.maps.StringMapSection].
     */
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