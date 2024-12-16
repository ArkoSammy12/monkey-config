package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.builders.ListSettingBuilder
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.values.SettingValue

open class ListSetting<E : Any, S : SerializableType<*>>(
    settingBuilder: ListSettingBuilder<E, S>
) : AbstractSetting<List<E>, ListType<S>>(settingBuilder) {

    override val value: SettingValue<List<E>, ListType<S>> = SettingValue(settingBuilder.defaultValue, serializer = settingBuilder.serializer, deserializer = settingBuilder.deserializer)

}