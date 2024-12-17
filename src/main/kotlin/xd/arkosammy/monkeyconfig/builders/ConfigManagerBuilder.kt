@file:JvmName("ConfigManagerBuilder")

package xd.arkosammy.monkeyconfig.builders

import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.hocon.HoconFormat
import com.electronwill.nightconfig.json.JsonFormat
import com.electronwill.nightconfig.toml.TomlFormat
import com.electronwill.nightconfig.yaml.YamlFormat
import org.slf4j.Logger
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.managers.DefaultConfigManager
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath
import java.nio.file.Path

open class ConfigManagerBuilder(
    val fileName: String,
    val fileFormat: ConfigFormat<*>,
    val filePath: Path
) {

    var async: Boolean = false

    var autoReload: Boolean = false

    var logger: Logger? = null

    internal val sections: MutableList<SectionBuilder> = mutableListOf()

    internal val settings: MutableList<SettingBuilder<*, *>> = mutableListOf()

    internal val mapSections: MutableList<MapSectionBuilder<*, *>> = mutableListOf()

    fun <T : Any, S : SerializableType<*>, B : SettingBuilder<T, S>> setting(settingName: String, defaultValue: T, builderInstanceProvider: (String, T, ElementPath) -> B, builder: B.() -> Unit): ElementPath {
        val path = ElementPath(settingName)
        val settingBuilder = builderInstanceProvider(settingName, defaultValue, path)
        builder(settingBuilder)
        this.settings.add(settingBuilder)
        return path
    }

    fun booleanSetting(settingName: String, defaultValue: Boolean, builder: BooleanSettingBuilder.() -> Unit): ElementPath {
        return this.setting(settingName, defaultValue, ::BooleanSettingBuilder, builder)
    }

    fun <T : Number> numberSetting(settingName: String, defaultValue: T, builder: NumberSettingBuilder<T>.() -> Unit): ElementPath {
        return this.setting(settingName, defaultValue, ::NumberSettingBuilder, builder)
    }

    fun stringSetting(settingName: String, defaultValue: String, builder: StringSettingBuilder.() -> Unit): ElementPath {
        return this.setting(settingName, defaultValue, ::StringSettingBuilder, builder)
    }

    fun <E : Any, S : SerializableType<*>> listSetting(settingName: String, defaultValue: List<E>, builder: ListSettingBuilder<E, S>.() -> Unit): ElementPath {
        return this.setting(settingName, defaultValue, ::ListSettingBuilder, builder)
    }

    fun <E : Enum<E>> enumSetting(settingName: String, defaultValue: E, builder: EnumSettingBuilder<E>.() -> Unit): ElementPath {
        return this.setting(settingName, defaultValue, ::EnumSettingBuilder, builder)
    }

    fun section(sectionName: String, builder: SectionBuilder.() -> Unit): ElementPath {
        val sectionBuilder = SectionBuilder(sectionName, this)
        builder(sectionBuilder)
        this.sections.add(sectionBuilder)
        return sectionBuilder.path
    }

    fun <V : Any, S : SerializableType<*>, B : MapSectionBuilder<V, S>> mapSection(sectionName: String, builderInstanceProvider: (String) -> B, builder: B.() -> Unit): ElementPath {
        val mapSectionBuilder = builderInstanceProvider(sectionName)
        builder(mapSectionBuilder)
        this.mapSections.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath {
        return this.mapSection(sectionName, ::StringMapSectionBuilder, builder)
    }

    fun build(configManagerProvider: ((ConfigManagerBuilder, Logger?) -> ConfigManager)? = null): ConfigManager {
        return if (configManagerProvider == null) {
            DefaultConfigManager(this, logger)
        } else {
            configManagerProvider(this, logger)
        }
    }

}

// TODO: Add some way to add the file extension or atleast warn the user to use the correct file extension in the file path
fun configManager(fileName: String, fileFormat: ConfigFormat<*>, filePath: Path, builder: ConfigManagerBuilder.() -> Unit): ConfigManagerBuilder {
    if (fileName.contains(".")) {
        throw IllegalArgumentException("\".\" are not allowed on config file names!")
    }
    val configManagerBuilder = ConfigManagerBuilder(fileName, fileFormat, filePath)
    builder(configManagerBuilder)
    return configManagerBuilder
}

fun jsonConfigManager(fileName: String, filePath: Path, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    val configManagerBuilder: ConfigManagerBuilder = configManager(fileName, JsonFormat.fancyInstance(), filePath, builder)
    return configManagerBuilder.build()
}

fun yamlConfigManager(fileName: String, filePath: Path, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    val configManagerBuilder: ConfigManagerBuilder = configManager(fileName, YamlFormat.defaultInstance(), filePath, builder)
    return configManagerBuilder.build()
}

fun tomlConfigManager(fileName: String, filePath: Path, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    val configManagerBuilder: ConfigManagerBuilder = configManager(fileName, TomlFormat.instance(), filePath, builder)
    return configManagerBuilder.build()
}

fun hoconConfigManager(fileName: String, filePath: Path, builder: ConfigManagerBuilder.() -> Unit): ConfigManager {
    val configManagerBuilder: ConfigManagerBuilder = configManager(fileName, HoconFormat.instance(), filePath, builder)
    return configManagerBuilder.build()
}


