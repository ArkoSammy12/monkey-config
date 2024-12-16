package xd.arkosammy.monkeyconfig.sections.maps

import xd.arkosammy.monkeyconfig.builders.MapSectionBuilder
import xd.arkosammy.monkeyconfig.builders.StringSettingBuilder
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.settings.StringSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.ElementPath

class StringMapSection(
    sectionBuilder: MapSectionBuilder<String, StringType>
) : AbstractMapSection<String, StringType>(sectionBuilder) {

    override fun getEntryFromSerialized(path: ElementPath, serializedEntry: SerializableType<*>): Setting<String, StringType>? {
        if (serializedEntry !is StringType) {
            return null
        }
        val stringSettingBuilder = StringSettingBuilder(path.asList.last(), path)
        stringSettingBuilder.defaultValue = serializedEntry.value
        stringSettingBuilder.serializer = this.serializer
        stringSettingBuilder.deserializer = this.deserializer
        return StringSetting(stringSettingBuilder)
    }

    override fun getEntryFromValue(path: ElementPath, defaultValue: String, value: String): Setting<String, StringType> {
        val stringSettingBuilder = StringSettingBuilder(path.asList.last(), path)
        stringSettingBuilder.defaultValue = value
        stringSettingBuilder.serializer = this.serializer
        stringSettingBuilder.deserializer = this.deserializer
        return StringSetting(stringSettingBuilder)
    }

}