package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.BooleanSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class BooleanSettingBuilder(
    name: String,
    defaultValue: Boolean,
    path: ElementPath
) : SettingBuilder<Boolean, BooleanType>(name, defaultValue, path) {

    override var serializer: (Boolean) -> BooleanType = { booleanValue -> BooleanType(booleanValue) }

    override var deserializer: (BooleanType) -> Boolean = { serializedBooleanValue -> serializedBooleanValue.value }

    override var implementation: (SettingBuilder<Boolean, BooleanType>) -> Setting<Boolean, BooleanType> = ::BooleanSetting

}