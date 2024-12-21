package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.ListSettingBuilder
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class ListSetting<E : Any, S : SerializableType<*>>(
    settingBuilder: ListSettingBuilder<E, S>
) : AbstractSetting<List<E>, ListType<S>, ListSetting<E, S>>(settingBuilder) {

    override val value: SettingValue<List<E>, ListType<S>> =
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