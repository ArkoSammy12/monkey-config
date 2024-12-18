package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.NumberSettingBuilder
import xd.arkosammy.monkeyconfig.builders.SettingBuilder
import xd.arkosammy.monkeyconfig.types.NumberType

class TestNumberSettingImplementation<T : Number>(settingBuilder: NumberSettingBuilder<T>) : NumberSetting<T>(settingBuilder) {

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


    init {

        println("Hello world!!!")

    }

}