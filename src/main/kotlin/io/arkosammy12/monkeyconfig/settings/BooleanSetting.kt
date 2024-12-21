package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.BooleanSettingBuilder
import io.arkosammy12.monkeyconfig.types.BooleanType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class BooleanSetting(
    settingBuilder: BooleanSettingBuilder
) : AbstractSetting<Boolean, BooleanType, BooleanSetting>(settingBuilder) {

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

    override val value: SettingValue<Boolean, BooleanType> =
        SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}