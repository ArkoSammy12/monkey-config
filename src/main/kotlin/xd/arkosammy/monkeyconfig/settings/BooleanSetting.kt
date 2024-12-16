package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.BooleanSettingBuilder
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.values.SettingValue

open class BooleanSetting(
    settingBuilder: BooleanSettingBuilder
) : AbstractSetting<Boolean, BooleanType>(settingBuilder) {

    override val value: SettingValue<Boolean, BooleanType> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}