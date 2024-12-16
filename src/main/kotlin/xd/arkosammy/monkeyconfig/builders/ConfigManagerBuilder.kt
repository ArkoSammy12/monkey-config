package xd.arkosammy.monkeyconfig.builders

import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.hocon.HoconFormat
import com.electronwill.nightconfig.json.JsonFormat
import com.electronwill.nightconfig.toml.TomlFormat
import com.electronwill.nightconfig.yaml.YamlFormat
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.managers.DefaultConfigManager
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath
import java.nio.file.Path

open class ConfigManagerBuilder internal constructor(
    val fileName: String,
    val fileFormat: ConfigFormat<*>,
    val filePath: Path
) {

    internal val sections: MutableList<SectionBuilder> = mutableListOf()

    internal val settings: MutableList<SettingBuilder<*, *>> = mutableListOf()

    internal val mapSections: MutableList<MapSectionBuilder<*, *>> = mutableListOf()

    fun section(sectionName: String, builder: SectionBuilder.() -> Unit): ElementPath {
        val sectionBuilder = SectionBuilder(sectionName, this)
        builder(sectionBuilder)
        this.sections.add(sectionBuilder)
        return sectionBuilder.path
    }

    fun stringMapSection(sectionName: String, builder: StringMapSectionBuilder.() -> Unit): ElementPath {
        val mapSectionBuilder = StringMapSectionBuilder(sectionName, this)
        builder(mapSectionBuilder)
        this.mapSections.add(mapSectionBuilder)
        return mapSectionBuilder.path
    }

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
        val path = ElementPath(settingName)
        val settingBuilder = builderInstanceProvider(settingName, path)
        builder(settingBuilder)
        this.settings.add(settingBuilder)
        return path
    }

    fun build(configManagerProvider: ((ConfigManagerBuilder) -> ConfigManager)? = null): ConfigManager {
        return if (configManagerProvider == null) {
            DefaultConfigManager(this)
        } else {
            configManagerProvider(this)
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


