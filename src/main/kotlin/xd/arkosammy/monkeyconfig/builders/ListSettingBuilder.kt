package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class ListSettingBuilder<E : Any, S : SerializableType<*>>(
    name: String,
    path: ElementPath
) : SettingBuilder<List<E>, ListType<S>>(name, path) {

    override fun build(): Setting<List<E>, ListType<S>> {
        return ListSetting(this)
    }

}