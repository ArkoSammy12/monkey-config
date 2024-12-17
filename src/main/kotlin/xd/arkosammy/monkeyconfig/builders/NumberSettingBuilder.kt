package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.NumberSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class NumberSettingBuilder<T : Number>(
    name: String,
    defaultValue: T,
    path: ElementPath
) : SettingBuilder<T, NumberType<T>>(name, defaultValue, path) {

    override var serializer: (T) -> NumberType<T> = ::NumberType

    override var deserializer: (NumberType<T>) -> T = NumberType<T>::value

    open var minValue: T? = null

    open var maxValue: T? = null

    override var implementation: (SettingBuilder<T, NumberType<T>>) -> Setting<T, NumberType<T>> = { builder ->
        if (builder is NumberSettingBuilder<T>) {
            NumberSetting(builder)
        } else {
            NumberSetting(builder, minValue, maxValue)
        }
    }

}