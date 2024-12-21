package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.NumberSettingBuilder
import io.arkosammy12.monkeyconfig.types.NumberType
import io.arkosammy12.monkeyconfig.values.NumberSettingValue
import io.arkosammy12.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    settingBuilder: NumberSettingBuilder<T>,
) : AbstractSetting<T, NumberType<T>, NumberSetting<T>>(settingBuilder) {

    override val value: SettingValue<T, NumberType<T>> =
        NumberSettingValue(settingBuilder.defaultValue, minValue = settingBuilder.minValue, maxValue = settingBuilder.maxValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

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