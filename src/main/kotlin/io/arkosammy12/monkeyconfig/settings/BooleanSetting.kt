package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.BooleanType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class BooleanSetting(
    settingBuilder: SettingBuilder<Boolean, BooleanType>
) : AbstractSetting<Boolean, BooleanType>(settingBuilder) {

    override val value: SettingValue<Boolean, BooleanType> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}