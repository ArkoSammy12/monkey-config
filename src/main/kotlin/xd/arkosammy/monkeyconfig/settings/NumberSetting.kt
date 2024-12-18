package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.NumberSettingBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.values.NumberSettingValue
import xd.arkosammy.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    settingBuilder: NumberSettingBuilder<T>
) : AbstractSetting<T, NumberType<T>>(settingBuilder) {

    // TODO: Find a way to avoid having to do this..., or at lest not for extending classes
    constructor(settingBuilder: SettingBuilder<T, NumberType<T>>, minValue: T? = null, maxValue: T? = null) : this(
        NumberSettingBuilder<T>(settingBuilder.name, settingBuilder.defaultValue, settingBuilder.path).also { builder ->
            builder.minValue = if (settingBuilder is NumberSettingBuilder<T>) settingBuilder.minValue else minValue
            builder.maxValue = if (settingBuilder is NumberSettingBuilder<T>) settingBuilder.maxValue else maxValue
            builder.comment = settingBuilder.comment
            builder.implementation = settingBuilder.implementation
            builder.serializer = settingBuilder.serializer
            builder.deserializer = settingBuilder.deserializer
        }
    )

    override val value: SettingValue<T, NumberType<T>> =
        NumberSettingValue(settingBuilder.defaultValue, lowerBound = settingBuilder.minValue, upperBound = settingBuilder.maxValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}