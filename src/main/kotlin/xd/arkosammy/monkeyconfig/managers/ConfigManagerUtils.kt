@file:JvmName("ConfigManagerUtils")

package xd.arkosammy.monkeyconfig.managers

import xd.arkosammy.monkeyconfig.sections.maps.MapSection
import xd.arkosammy.monkeyconfig.sections.maps.StringMapSection
import xd.arkosammy.monkeyconfig.settings.BooleanSetting
import xd.arkosammy.monkeyconfig.settings.EnumSetting
import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.settings.NumberSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.settings.StringListSetting
import xd.arkosammy.monkeyconfig.settings.StringSetting
import xd.arkosammy.monkeyconfig.util.ElementPath

fun ConfigManager.getBooleanSetting(settingPath: ElementPath): Setting<Boolean, *>? =
    this.getSetting<Boolean, BooleanSetting>(settingPath)

fun <E : Enum<E>> ConfigManager.getEnumSetting(settingPath: ElementPath): Setting<E, *>? =
    this.getSetting<E, EnumSetting<E>>(settingPath)

fun <T : Any> ConfigManager.getListSetting(settingPath: ElementPath): Setting<List<T>, *>? =
    this.getSetting<List<T>, ListSetting<T, *>>(settingPath)

fun ConfigManager.getStringListSetting(settingPath: ElementPath): StringListSetting? =
    this.getSetting<List<String>, StringListSetting>(settingPath) as StringListSetting?

fun <T : Number> ConfigManager.getNumberSetting(settingPath: ElementPath): Setting<T, *>? =
    this.getSetting<T, NumberSetting<T>>(settingPath)

fun ConfigManager.getStringSetting(settingPath: ElementPath): Setting<String, *>? =
    this.getSetting<String, StringSetting>(settingPath)

@Suppress("UNCHECKED_CAST")
fun <V : Any, M : MapSection<V, *>> ConfigManager.getMapSection(mapSectionPath: ElementPath, mapSectionClass: Class<out M>): M? {
    var returnedSection: M? = null
    this.traverseSections { section ->
        if (section is MapSection<*, *> && mapSectionClass.isInstance(section) && section.path == mapSectionPath) {
            returnedSection = section as M
            return@traverseSections
        }
    }
    return returnedSection
}

fun ConfigManager.getStringMapSection(mapSection: ElementPath): StringMapSection? =
    this.getMapSection<String, StringMapSection>(mapSection)

inline fun <V : Any, reified M : MapSection<V, *>> ConfigManager.getMapSection(mapSectionPath: ElementPath) =
    this.getMapSection(mapSectionPath, M::class.java)

@JvmOverloads
fun <V : Any, T : Setting<V, *>> ConfigManager.getRawSettingValue(settingPath: ElementPath, settingClass: Class<T>, orElse: V? = null): V? {
    val setting: Setting<V, *>? = this.getSetting(settingPath, settingClass)
    return setting?.value?.raw ?: orElse
}

@JvmOverloads
inline fun <V : Any, reified T : Setting<V, *>> ConfigManager.getRawSettingValue(settingPath: ElementPath, orElse: V? = null): V? {
    return this.getRawSettingValue(settingPath, T::class.java, orElse)
}

@JvmOverloads
fun ConfigManager.getRawBooleanSettingValue(settingPath: ElementPath, orElse: Boolean? = null): Boolean? =
    this.getRawSettingValue<Boolean, Setting<Boolean, *>>(settingPath, orElse)

@JvmOverloads
fun <E : Enum<E>> ConfigManager.getRawEnumSettingValue(settingPath: ElementPath, orElse: E? = null): E? =
    this.getRawSettingValue<E, Setting<E, *>>(settingPath, orElse)

@JvmOverloads
fun <T : Any> ConfigManager.getRawListSettingValue(settingPath: ElementPath, orElse: List<T>? = null): List<T>? =
    this.getRawSettingValue<List<T>, Setting<List<T>, *>>(settingPath, orElse)

@JvmOverloads
fun ConfigManager.getRawStringListSettingValue(settingPath: ElementPath, orElse: List<String>? = null): List<String>? =
    this.getRawListSettingValue(settingPath, orElse)

@JvmOverloads
fun <T : Number> ConfigManager.getRawNumberSettingValue(settingPath: ElementPath, orElse: T? = null): T? =
    this.getRawSettingValue<T, Setting<T, *>>(settingPath, orElse)

@JvmOverloads
fun ConfigManager.getRawStringSettingValue(settingPath: ElementPath, orElse: String? = null): String? =
    this.getRawSettingValue<String, Setting<String, *>>(settingPath, orElse)




