package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.NumberSettingBuilder
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.values.NumberSettingValue
import xd.arkosammy.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    settingBuilder: NumberSettingBuilder<T>
) : AbstractSetting<T, NumberType<T>>(settingBuilder) {

    override val value: SettingValue<T, NumberType<T>> =
        NumberSettingValue(settingBuilder.defaultValue, lowerBound = settingBuilder.minValue, upperBound = settingBuilder.maxValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}