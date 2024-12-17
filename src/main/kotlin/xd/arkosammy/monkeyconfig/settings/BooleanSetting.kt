package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.values.SettingValue

open class BooleanSetting(
    settingBuilder: SettingBuilder<Boolean, BooleanType>
) : AbstractSetting<Boolean, BooleanType>(settingBuilder) {

    override val value: SettingValue<Boolean, BooleanType> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}