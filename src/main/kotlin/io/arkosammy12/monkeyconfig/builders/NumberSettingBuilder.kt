package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.settings.NumberSetting
import io.arkosammy12.monkeyconfig.types.NumberType
import io.arkosammy12.monkeyconfig.util.ElementPath

open class NumberSettingBuilder<T : Number>(
    name: String,
    defaultValue: T,
    path: ElementPath
) : SettingBuilder<T, NumberType<T>, NumberSetting<T>, NumberSettingBuilder<T>>(name, defaultValue, path) {

    override var serializer: (T) -> NumberType<T> = { value -> NumberType(value) }

    override var deserializer: (NumberType<T>) -> T = NumberType<T>::value

    override var implementation: (NumberSettingBuilder<T>) ->  NumberSetting<T> = ::NumberSetting

    open var minValue: T? = null

    open var maxValue: T? = null

    override fun build(): NumberSetting<T> =
        this.implementation(this)

}