package io.github.arkosammy12.monkeyconfig.settings

import io.github.arkosammy12.monkeyconfig.builders.NumberSettingBuilder
import io.github.arkosammy12.monkeyconfig.types.NumberType
import io.github.arkosammy12.monkeyconfig.values.NumberSettingValue
import io.github.arkosammy12.monkeyconfig.values.SettingValue

open class NumberSetting<T : Number>(
    settingBuilder: NumberSettingBuilder<T>,
) : AbstractSetting<T, NumberType<T>, NumberSetting<T>>(settingBuilder) {

    override val value: SettingValue<T, NumberType<T>> =
        NumberSettingValue(settingBuilder.defaultValue, minValue = settingBuilder.minValue, maxValue = settingBuilder.maxValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

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