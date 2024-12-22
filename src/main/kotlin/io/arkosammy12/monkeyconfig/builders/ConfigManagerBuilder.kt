package io.arkosammy12.monkeyconfig.builders

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.hocon.HoconFormat
import com.electronwill.nightconfig.json.JsonFormat
import com.electronwill.nightconfig.toml.TomlFormat
import com.electronwill.nightconfig.yaml.YamlFormat
import org.slf4j.Logger
import io.arkosammy12.monkeyconfig.base.ConfigManager
import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.managers.DefaultConfigManager
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.util.ElementPath
import java.nio.file.Path
import kotlin.collections.toList

open class ConfigManagerBuilder(
    val fileName: String,
    val fileFormat: ConfigFormat<*>,
    val filePath: Path
) {

    open var async: Boolean = false

    open var autoReload: Boolean = false

    open var logger: Logger? = null

    open var implementation: (ConfigManagerBuilder) -> DefaultConfigManager = ::DefaultConfigManager

    val configElementBuilders: List<ConfigElementBuilder<*, *>>
        get() = this.internalConfigElementBuilders.toList()

    protected val internalConfigElementBuilders: MutableList<ConfigElementBuilder<*, *>> = mutableListOf()

    fun <V : Any, S : SerializableType<*>, I : Setting<V, S>, T : SettingBuilder<V, S, I, T>> setting(settingName: String, defaultValue: V, builderInstanceProvider: (String, V, ElementPath) -> T, builder: T.() -> Unit): ElementPath {
        val path = ElementPath(settingName)
        val settingBuilder: T = builderInstanceProvider(settingName, defaultValue, path)
        builder(settingBuilder)
        settingBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(settingBuilder)
        return path
    }

    fun booleanSetting(settingName: String, defaultValue: Boolean, builder: BooleanSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::BooleanSettingBuilder, builder)

    fun <T : Number> numberSetting(settingName: String, defaultValue: T, builder: NumberSettingBuilder<T>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::NumberSettingBuilder, builder)

    fun stringSetting(settingName: String, defaultValue: String, builder: StringSettingBuilder.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::StringSettingBuilder, builder)

    fun <E : Any, S : SerializableType<*>> listSetting(settingName: String, defaultValue: List<E>, builder: ListSettingBuilder<E, S>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::ListSettingBuilder, builder)

    // TODO: Fix this
    fun stringListSetting(settingName: String, defaultValue: List<String>, builder: StringListSettingBuilder.() -> Unit): ElementPath  =
        this.listSetting<String, StringType>(settingName, defaultValue) {
            builder(this as StringListSettingBuilder)
        }

    fun <E : Enum<E>> enumSetting(settingName: String, defaultValue: E, builder: EnumSettingBuilder<E>.() -> Unit): ElementPath =
        this.setting(settingName, defaultValue, ::EnumSettingBuilder, builder)

    @JvmOverloads
    fun section(sectionName: String, builderInstanceProvider: (String, ConfigManagerBuilder) -> SectionBuilder = ::SectionBuilder, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder: SectionBuilder = builderInstanceProvider(sectionName, this)
        builder(sectionBuilder)
        sectionBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(sectionBuilder)
    }

    fun <V : Any, S : SerializableType<*>, T : MapSectionBuilder<V, S>> mapSection(sectionName: String, builderInstanceProvider: (String) -> T, builder: T.() -> Unit): ElementPath {
        val mapSectionBuilder = builderInstanceProvider(sectionName)
        builder(mapSectionBuilder)
        mapSectionBuilder.logger = this.logger
        this.internalConfigElementBuilders.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath =
        this.mapSection(sectionName, ::StringMapSectionBuilder, builder)

}

@JvmOverloads
fun <C : Config, T : ConfigFormat<C>> configManager(fileName: String, fileFormat: T, filePath: Path, builderInstanceProvider: (String, T, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    if (fileName.contains(".")) {
        throw IllegalArgumentException("Dots (\".\") are not allowed in config file names!")
    }
    val configManagerBuilder = builderInstanceProvider(fileName, fileFormat, filePath)
    builder(configManagerBuilder)
    return configManagerBuilder.implementation(configManagerBuilder)
}

private fun checkFileExtension(filePath: Path, vararg validExtensions: String) {
    if (validExtensions.none { extension -> filePath.toString().endsWith(extension) }) {
        throw IllegalArgumentException("File path must end with one of the following extensions: ${validExtensions.joinToString(", ")}")
    }
}

fun jsonConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, JsonFormat<*>, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".json", ".json5")
    return configManager(fileName, JsonFormat.fancyInstance(), filePath, builderInstanceProvider, builder)
}

fun yamlConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, YamlFormat, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".yaml", ".yml")
    return configManager(fileName, YamlFormat.defaultInstance(), filePath, builderInstanceProvider, builder)
}

fun tomlConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, TomlFormat, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".toml")
    return configManager(fileName, TomlFormat.instance(), filePath, builderInstanceProvider, builder)
}

fun hoconConfigManager(fileName: String, filePath: Path, builderInstanceProvider: (String, HoconFormat, Path) -> ConfigManagerBuilder = ::ConfigManagerBuilder, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    checkFileExtension(filePath, ".conf")
    return configManager(fileName, HoconFormat.instance(), filePath, builderInstanceProvider, builder)
}

