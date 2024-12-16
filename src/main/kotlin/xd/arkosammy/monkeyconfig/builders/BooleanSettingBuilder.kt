package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.settings.BooleanSetting
import xd.arkosammy.monkeyconfig.settings.Setting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.util.ElementPath

open class BooleanSettingBuilder(
    name: String,
    path: ElementPath
) : SettingBuilder<Boolean, BooleanType>(name, path) {

    override var serializer: (Boolean) -> BooleanType = { booleanValue -> BooleanType(booleanValue) }

    override var deserializer: (BooleanType) -> Boolean = { serializedBooleanValue -> serializedBooleanValue.value }

    override fun build(): Setting<Boolean, BooleanType> {
        return BooleanSetting(this)
    }

}