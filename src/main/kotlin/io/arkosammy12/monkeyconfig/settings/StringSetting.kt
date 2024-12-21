package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.StringSettingBuilder
import io.arkosammy12.monkeyconfig.types.StringType
import io.arkosammy12.monkeyconfig.values.SettingValue

class StringSetting(
    settingBuilder: StringSettingBuilder
) : AbstractSetting<String, StringType, StringSetting>(settingBuilder) {

    override val value: SettingValue<String, StringType> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

    override fun onInitialized() {
        if (onInitializedFunction != null) {
            this.onInitializedFunction(this)
        }
    }

    override fun onUpdated() {
        if (onUpdatedFunction != null) {
            this.onUpdatedFunction(this)
        }
    }

    override fun onSaved() {
        if (onSavedFunction != null) {
            this.onSavedFunction(this)
        }
    }

}