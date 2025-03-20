package io.github.arkosammy12.monkeyconfig.settings

import io.github.arkosammy12.monkeyconfig.builders.NumberSettingBuilder

class TestNumberSetting<T : Number>(settingBuilder: NumberSettingBuilder<T>) : NumberSetting<T>(settingBuilder) {

    init {

        println("Hello world!!!")

    }

}