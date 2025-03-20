package io.github.arkosammy12.monkeyconfig.sections.maps

import io.github.arkosammy12.monkeyconfig.builders.MapSectionBuilder
import io.github.arkosammy12.monkeyconfig.builders.StringSettingBuilder
import io.github.arkosammy12.monkeyconfig.base.Setting
import io.github.arkosammy12.monkeyconfig.settings.StringSetting
import io.github.arkosammy12.monkeyconfig.types.SerializableType
import io.github.arkosammy12.monkeyconfig.types.StringType
import io.github.arkosammy12.monkeyconfig.util.ElementPath

class StringMapSection(
    sectionBuilder: MapSectionBuilder<String, StringType>
) : AbstractMapSection<String, StringType>(sectionBuilder) {

    override fun getEntryFromSerialized(entryPath: ElementPath, serializedEntry: SerializableType<*>): Setting<String, StringType>? {
        if (serializedEntry !is StringType) {
            return null
        }
        val stringSettingBuilder = StringSettingBuilder(entryPath.asList.last(), serializedEntry.value, entryPath)
        stringSettingBuilder.serializer = this.serializer
        stringSettingBuilder.deserializer = this.deserializer
        return StringSetting(stringSettingBuilder)
    }

    override fun getEntryFromValue(entryPath: ElementPath, defaultValue: String, value: String): Setting<String, StringType> {
        val stringSettingBuilder = StringSettingBuilder(entryPath.asList.last(), defaultValue, entryPath)
        stringSettingBuilder.serializer = this.serializer
        stringSettingBuilder.deserializer = this.deserializer
        return StringSetting(stringSettingBuilder)
    }

}