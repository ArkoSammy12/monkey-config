package io.arkosammy12.monkeyconfig.settings

import io.arkosammy12.monkeyconfig.builders.SettingBuilder
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.values.SettingValue

open class ListSetting<E : Any, S : SerializableType<*>>(
    settingBuilder: SettingBuilder<List<E>, ListType<S>>
) : AbstractSetting<List<E>, ListType<S>>(settingBuilder) {

    override val value: SettingValue<List<E>, ListType<S>> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}