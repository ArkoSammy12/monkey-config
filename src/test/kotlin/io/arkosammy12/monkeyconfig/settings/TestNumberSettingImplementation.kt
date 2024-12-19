package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.NumberType

class TestNumberSettingImplementation<T : Number>(settingBuilder: SettingBuilder<T, NumberType<T>>) : NumberSetting<T>(settingBuilder) {

    init {

        println("Hello world!!!")

    }

}