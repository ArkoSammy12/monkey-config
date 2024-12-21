package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.NumberSettingBuilder

class TestNumberSettingImplementation<T : Number>(settingBuilder: NumberSettingBuilder<T>) : NumberSetting<T>(settingBuilder) {

    init {

        println("Hello world!!!")

    }

}