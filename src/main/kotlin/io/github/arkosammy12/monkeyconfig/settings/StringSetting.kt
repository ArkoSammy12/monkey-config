package io.github.arkosammy12.monkeyconfig.settings

import io.github.arkosammy12.monkeyconfig.builders.StringSettingBuilder
import io.github.arkosammy12.monkeyconfig.types.StringType
import io.github.arkosammy12.monkeyconfig.values.SettingValue

open class StringSetting(
    settingBuilder: StringSettingBuilder
) : AbstractSetting<String, StringType, StringSetting>(settingBuilder) {

    override val value: SettingValue<String, StringType> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

    override fun onInitialized() {
        this.onInitializedFunction?.let { it(this) }
    }

    override fun onUpdated() {
        this.onInitializedFunction?.let { it(this) }
    }

    override fun onSaved() {
        this.onInitializedFunction?.let { it(this) }
    }

}