package io.github.arkosammy12.monkeyconfig.builders

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.hocon.HoconFormat
import com.electronwill.nightconfig.json.JsonFormat
import com.electronwill.nightconfig.toml.TomlFormat
import com.electronwill.nightconfig.yaml.YamlFormat
import org.slf4j.Logger
import io.github.arkosammy12.monkeyconfig.base.ConfigManager
import io.github.arkosammy12.monkeyconfig.base.Section
import io.github.arkosammy12.monkeyconfig.base.Setting
import io.github.arkosammy12.monkeyconfig.managers.DefaultConfigManager
import io.github.arkosammy12.monkeyconfig.types.SerializableType
import io.github.arkosammy12.monkeyconfig.types.StringType
import io.github.arkosammy12.monkeyconfig.util.ElementPath
import java.nio.file.Path
import kotlin.collections.toList

/**
 * A class which builds [ConfigManager]s.
 *
 * @property fileName An identifier for this [ConfigManager].
 * @property fileFormat The [ConfigFormat] corresponding to the configuration file managed by the resulting [ConfigManager].
 * @property filePath The [Path] of the configuration file managed by the resulting [ConfigManager]. Note that the last node of the path must include a file extension corresponding to the [fileFormat].
 */
open class ConfigManagerBuilder(
    val fileName: String,
    val fileFormat: ConfigFormat<*>,
    val filePath: Path
) {

    /**
     * Whether write operations to the associated file configuration should be "write-asynchronous", meaning
     * write operations will not wait until it completes.
     */
    open var async: Boolean = false

    /**
     * Whether the resulting [ConfigManager] should be auto updated whenever the configuration file is edited manually.
     */
    open var autoReload: Boolean = false

    /**
     * An optional [Logger] instance accessible to the resulting [ConfigManager] and its [io.github.arkosammy12.monkeyconfig.base.ConfigElement]s.
     */
    open var logger: Logger? = null

    /**
     * A function which allows for different [ConfigManager] implementations to be used when building this [io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder].
     */
    open var implementation: (ConfigManagerBuilder) -> ConfigManager = ::DefaultConfigManager

    /**
     * An immutable view of the [ConfigElementBuilder]s currently held by this [io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder].
     * Does not include nested instances.
     */
    val configElementBuilders: List<ConfigElementBuilder<*, *>>
        get() = this.internalConfigElementBuilders.toList()

    protected val internalConfigElementBuilders: MutableList<ConfigElementBuilder<*, *>> = mutableListOf()

    /**
     * Adds a new [Setting] to this [io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder] via its [SettingBuilder].
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
     * @return The [ElementPath] associated to the resulting [Setting] when built and added to a [io.github.arkosammy12.monkeyconfig.base.ConfigManager],
     * which can be used to access it later.
     */
    fun <V : Any, S : SerializableType<*>, I : Setting<V, S>, T : SettingBuilder<V, S, I, T>> setting(settingName: String, defaultValue: V, builderInstanceProvider: (String, V, ElementPath) -> T, builder: T.() -> Unit): ElementPath {
        val path = ElementPath(settingName)
        val settingBuilder: T = builderInstanceProvider(settingName, defaultValue, path)
        builder(settingBuilder)
        settingBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(settingBuilder)
        return path
    }

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.settings.BooleanSetting] using a [BooleanSettingBuilder].
     *
     * @param settingName The name of the built [io.github.arkosammy12.monkeyconfig.settings.BooleanSetting].
     * @param defaultValue The default value for the built [io.github.arkosammy12.monkeyconfig.settings.BooleanSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [BooleanSettingBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.settings.BooleanSetting].
     */
    fun booleanSetting(settingName: String, defaultValue: Boolean, builder: BooleanSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::BooleanSettingBuilder, builder)

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.settings.NumberSetting] using a [NumberSettingBuilder].
     *
     * @param settingName The name of the built [io.github.arkosammy12.monkeyconfig.settings.NumberSetting].
     * @param defaultValue The default value for the built [io.github.arkosammy12.monkeyconfig.settings.NumberSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [NumberSettingBuilder].
     * @param T The specific numerical type of the resulting [io.github.arkosammy12.monkeyconfig.settings.NumberSetting].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.settings.NumberSetting].
     */
    fun <T : Number> numberSetting(settingName: String, defaultValue: T, builder: NumberSettingBuilder<T>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::NumberSettingBuilder, builder)

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.settings.StringSetting] using a [StringSettingBuilder].
     *
     * @param settingName The name of the built [io.github.arkosammy12.monkeyconfig.settings.StringSetting].
     * @param defaultValue The default value for the built [io.github.arkosammy12.monkeyconfig.settings.StringSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [StringSettingBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.settings.StringSetting].
     */
    fun stringSetting(settingName: String, defaultValue: String, builder: StringSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::StringSettingBuilder, builder)

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.settings.ListSetting] using a [ListSettingBuilder].
     *
     * @param settingName The name of the built [io.github.arkosammy12.monkeyconfig.settings.ListSetting].
     * @param defaultValue The default value for the built [io.github.arkosammy12.monkeyconfig.settings.ListSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [ListSettingBuilder].
     * @param E The type of the elements held by the resulting [io.github.arkosammy12.monkeyconfig.settings.ListSetting].
     * @param S The [SerializableType] of the element when serialized to be written and read from a configuration file.
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.settings.ListSetting].
     */
    fun <E : Any, S : SerializableType<*>> listSetting(settingName: String, defaultValue: List<E>, builder: ListSettingBuilder<E, S>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::ListSettingBuilder, builder)

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.settings.StringListSetting] using a [StringListSettingBuilder].
     *
     * @param settingName The name of the built [io.github.arkosammy12.monkeyconfig.settings.StringListSetting].
     * @param defaultValue The default value for the built [io.github.arkosammy12.monkeyconfig.settings.StringListSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [StringListSettingBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.settings.StringListSetting].
     */
    fun stringListSetting(settingName: String, defaultValue: List<String>, builder: ListSettingBuilder<String, StringType>.() -> Unit): ElementPath  =
        this.setting(settingName, defaultValue, ::StringListSettingBuilder, builder)

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.settings.EnumSetting] using a [EnumSettingBuilder].
     *
     * @param settingName The name of the built [io.github.arkosammy12.monkeyconfig.settings.EnumSetting].
     * @param defaultValue The default value for the built [io.github.arkosammy12.monkeyconfig.settings.EnumSetting], which acts as the setting's starting value.
     * @param builder The builder function used to configure the [EnumSettingBuilder].
     * @param E The type of the enum instance held by the resulting [io.github.arkosammy12.monkeyconfig.settings.EnumSetting].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.settings.EnumSetting].
     */
    fun <E : Enum<E>> enumSetting(settingName: String, defaultValue: E, builder: EnumSettingBuilder<E>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::EnumSettingBuilder, builder)

    /**
     * Adds new a [Section] to this [io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder] via its [io.github.arkosammy12.monkeyconfig.builders.SectionBuilder].
     *
     * @param sectionName The name of the section being added. It cannot contain "**.**".
     * @param builderInstanceProvider A provider that returns the specific implementation of [io.github.arkosammy12.monkeyconfig.builders.SectionBuilder] to be used to build this [Section]. By default, this returns a regular [io.github.arkosammy12.monkeyconfig.builders.SectionBuilder].
     * @param builder The builder function used to configure the [io.github.arkosammy12.monkeyconfig.builders.SectionBuilder].
     *
     * @return The [ElementPath] associated to the resulting [Section].
     */
    @JvmOverloads
    fun section(sectionName: String, builderInstanceProvider: (String, ConfigManagerBuilder) -> SectionBuilder = ::SectionBuilder, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder: SectionBuilder = builderInstanceProvider(sectionName, this)
        builder(sectionBuilder)
        sectionBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(sectionBuilder)
    }

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.sections.maps.MapSection] to this [io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder] via its [MapSectionBuilder].
     *
     * @param sectionName The name of the map section being added. It cannot contain "**.**".
     * @param builderInstanceProvider A provider that returns the specific implementation of [io.github.arkosammy12.monkeyconfig.builders.MapSectionBuilder] to be used to build this [io.github.arkosammy12.monkeyconfig.sections.maps.MapSection].
     * @param builder The builder function used to configure the [io.github.arkosammy12.monkeyconfig.builders.MapSectionBuilder].
     * @param V The type of the values held by the resulting [io.github.arkosammy12.monkeyconfig.sections.maps.MapSection].
     * @param S The [SerializableType] used for serializing the values held by the resulting [io.github.arkosammy12.monkeyconfig.sections.maps.MapSection].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.sections.maps.MapSection].
     */
    fun <V : Any, S : SerializableType<*>, T : MapSectionBuilder<V, S>> mapSection(sectionName: String, builderInstanceProvider: (String) -> T, builder: T.() -> Unit): ElementPath {
        val mapSectionBuilder = builderInstanceProvider(sectionName)
        builder(mapSectionBuilder)
        mapSectionBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    /**
     * Adds a new [io.github.arkosammy12.monkeyconfig.sections.maps.StringMapSection] to this [io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilder] via its [StringMapSectionBuilder].
     *
     * @param sectionName The name of the map section being added. It cannot contain "**.**".
     * @param builder The builder function used to configure the [io.github.arkosammy12.monkeyconfig.builders.StringMapSectionBuilder].
     *
     * @return The [ElementPath] associated to the resulting [io.github.arkosammy12.monkeyconfig.sections.maps.StringMapSection].
     */
    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath =
        this.mapSection(sectionName, ::StringMapSectionBuilder, builder)

}

/**
 * Constructs a new [ConfigManager] instance configured via the provided [builder].
 *
 * @param fileName An identifier for this [ConfigManager].
 * @param fileFormat The [ConfigFormat] corresponding to the configuration file managed by the resulting [ConfigManager].
 * @param filePath The [Path] of the configuration file managed by the resulting [ConfigManager]. Note that the last node of the path must include a file extension corresponding to the [fileFormat].
 * @param builderInstanceProvider A provider that returns the specific [ConfigManagerBuilder] implementation to be used when configuring the resulting [ConfigManager]. By default, it uses a regular [ConfigManagerBuilder].
 * @param builder A function to configure the [ConfigManagerBuilder] used to build the resulting [ConfigManager].
 * @param C The [Config] implementation used for the [ConfigFormat].
 * @param T The specific [ConfigFormat] implementation used for the [ConfigManager].
 */
@JvmOverloads
fun <C : Config, T : ConfigFormat<C>> configManager(fileName: String, fileFormat: T, filePath: Path, builderInstanceProvider: (String, T, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    if (fileName.contains(".")) {
        throw IllegalArgumentException("Dots (\".\") are not allowed in config file names!")
    }
    val configManagerBuilder = builderInstanceProvider(fileName, fileFormat, filePath)
    builder(configManagerBuilder)
    return configManagerBuilder.implementation(configManagerBuilder)
}

/**
 * Constructs a new [ConfigManager] instance configured via the provided [builder] using a [JsonFormat]
 *
 * @param fileName An identifier for this [ConfigManager].
 * @param filePath The [Path] of the configuration file managed by the resulting [ConfigManager]. Note that the last node of the path must include a file extension corresponding to the [JsonFormat] format.
 * @param builderInstanceProvider A provider that returns the specific [ConfigManagerBuilder] implementation to be used when configuring the resulting [ConfigManager]. By default, it uses a regular [ConfigManagerBuilder].
 * @param builder A function to configure the [ConfigManagerBuilder] used to build the resulting [ConfigManager].
 */
fun jsonConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, JsonFormat<*>, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".json", ".json5")
    return configManager(fileName, JsonFormat.fancyInstance(), filePath, builderInstanceProvider, builder)
}

/**
 * Constructs a new [ConfigManager] instance configured via the provided [builder] using a [YamlFormat]
 *
 * @param fileName An identifier for this [ConfigManager].
 * @param filePath The [Path] of the configuration file managed by the resulting [ConfigManager]. Note that the last node of the path must include a file extension corresponding to the [YamlFormat] format.
 * @param builderInstanceProvider A provider that returns the specific [ConfigManagerBuilder] implementation to be used when configuring the resulting [ConfigManager]. By default, it uses a regular [ConfigManagerBuilder].
 * @param builder A function to configure the [ConfigManagerBuilder] used to build the resulting [ConfigManager].
 */
fun yamlConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, YamlFormat, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".yaml", ".yml")
    return configManager(fileName, YamlFormat.defaultInstance(), filePath, builderInstanceProvider, builder)
}

/**
 * Constructs a new [ConfigManager] instance configured via the provided [builder] using a [TomlFormat]
 *
 * @param fileName An identifier for this [ConfigManager].
 * @param filePath The [Path] of the configuration file managed by the resulting [ConfigManager]. Note that the last node of the path must include a file extension corresponding to the [TomlFormat] format.
 * @param builderInstanceProvider A provider that returns the specific [ConfigManagerBuilder] implementation to be used when configuring the resulting [ConfigManager]. By default, it uses a regular [ConfigManagerBuilder].
 * @param builder A function to configure the [ConfigManagerBuilder] used to build the resulting [ConfigManager].
 */
fun tomlConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, TomlFormat, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".toml")
    return configManager(fileName, TomlFormat.instance(), filePath, builderInstanceProvider, builder)
}

/**
 * Constructs a new [ConfigManager] instance configured via the provided [builder] using a [HoconFormat]
 *
 * @param fileName An identifier for this [ConfigManager].
 * @param filePath The [Path] of the configuration file managed by the resulting [ConfigManager]. Note that the last node of the path must include a file extension corresponding to the [HoconFormat] format.
 * @param builderInstanceProvider A provider that returns the specific [ConfigManagerBuilder] implementation to be used when configuring the resulting [ConfigManager]. By default, it uses a regular [ConfigManagerBuilder].
 * @param builder A function to configure the [ConfigManagerBuilder] used to build the resulting [ConfigManager].
 */
fun hoconConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, HoconFormat, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".conf")
    return configManager(fileName, HoconFormat.instance(), filePath, builderInstanceProvider, builder)
}

private fun checkFileExtension(filePath: Path, vararg validExtensions: String) {
    if (validExtensions.none { extension -> filePath.toString().endsWith(extension) }) {
        throw IllegalArgumentException("File path must end with one of the following extensions: ${validExtensions.joinToString(", ")}")
    }
}

