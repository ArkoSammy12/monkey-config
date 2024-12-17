package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.NumberSettingBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.values.NumberSettingValue
import xd.arkosammy.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    settingBuilder: NumberSettingBuilder<T>
) : AbstractSetting<T, NumberType<T>>(settingBuilder) {

    constructor(settingBuilder: SettingBuilder<T, NumberType<T>>, minValue: T? = null, maxValue: T? = null) : this(
        NumberSettingBuilder<T>(settingBuilder.name, settingBuilder.defaultValue, settingBuilder.path).also { builder ->
            builder.minValue = minValue
            builder.maxValue = maxValue
        }
    )

    override val value: SettingValue<T, NumberType<T>> =
        NumberSettingValue(settingBuilder.defaultValue, lowerBound = settingBuilder.minValue, upperBound = settingBuilder.maxValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}