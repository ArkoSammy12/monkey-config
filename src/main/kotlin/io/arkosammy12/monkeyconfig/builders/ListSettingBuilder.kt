package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.ListSetting
import io.arkosammy12.monkeyconfig.types.ListType
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class ListSettingBuilder<E : Any, S : SerializableType<*>>(
    name: String,
    defaultValue: List<E>,
    path: ElementPath
) : SettingBuilder<List<E>, ListType<S>, ListSetting<E, S>, ListSettingBuilder<E, S>>(name, defaultValue, path) {

    override var implementation: (ListSettingBuilder<E, S>) -> ListSetting<E, S> = ::ListSetting

    override fun build(): ListSetting<E, S> =
        this.implementation(this)

}