package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.NumberSettingBuilder
import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.NumberType
import io.arkosammy12.monkeyconfig.values.NumberSettingValue
import io.arkosammy12.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    settingBuilder: SettingBuilder<T, NumberType<T>>,
    minValue: T? = if (settingBuilder is NumberSettingBuilder<T>) settingBuilder.minValue else null,
    maxValue: T? = if (settingBuilder is NumberSettingBuilder<T>) settingBuilder.maxValue else null
) : AbstractSetting<T, NumberType<T>>(settingBuilder) {

    constructor(settingBuilder: NumberSettingBuilder<T>) : this(settingBuilder, settingBuilder.minValue, settingBuilder.maxValue)

    override val value: SettingValue<T, NumberType<T>> =
        NumberSettingValue(settingBuilder.defaultValue, minValue = minValue, maxValue = maxValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}