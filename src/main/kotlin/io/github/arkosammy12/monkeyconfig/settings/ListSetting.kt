package io.github.arkosammy12.monkeyconfig.settings

import io.github.arkosammy12.monkeyconfig.builders.ListSettingBuilder
import io.github.arkosammy12.monkeyconfig.types.ListType
import io.github.arkosammy12.monkeyconfig.types.SerializableType
import io.github.arkosammy12.monkeyconfig.values.SettingValue

open class ListSetting<E : Any, S : SerializableType<*>>(
    settingBuilder: ListSettingBuilder<E, S>
) : AbstractSetting<List<E>, ListType<S>, ListSetting<E, S>>(settingBuilder) {

    override val value: SettingValue<List<E>, ListType<S>> =
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