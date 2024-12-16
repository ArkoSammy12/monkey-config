package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.NumberSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class NumberSettingBuilder<T : Number>(
    name: String,
    path: ElementPath
) : SettingBuilder<T, NumberType<T>>(name, path) {

    override var serializer: (T) -> NumberType<T> = { numberValue -> NumberType(numberValue) }

    override var deserializer: (NumberType<T>) -> T = { serializedNumberValue -> serializedNumberValue.value }

    var minValue: T? = null

    var maxValue: T? = null

    override fun build(): Setting<T, NumberType<T>> {
        return NumberSetting(this)
    }

}