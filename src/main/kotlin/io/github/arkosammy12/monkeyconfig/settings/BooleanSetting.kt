package io.github.arkosammy12.monkeyconfig.settings

import io.github.arkosammy12.monkeyconfig.builders.BooleanSettingBuilder
import io.github.arkosammy12.monkeyconfig.types.BooleanType
import io.github.arkosammy12.monkeyconfig.values.SettingValue

open class BooleanSetting(
    settingBuilder: BooleanSettingBuilder
) : AbstractSetting<Boolean, BooleanType, BooleanSetting>(settingBuilder) {

    override fun onInitialized() {
        this.onInitializedFunction?.let { it(this) }
    }

    override fun onUpdated() {
        this.onUpdatedFunction?.let { it(this) }
    }

    override fun onSaved() {
        this.onSavedFunction?.let { it(this) }
    }

    override val value: SettingValue<Boolean, BooleanType> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}