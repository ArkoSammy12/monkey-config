package xd.arkosammy.monkeyconfig.builders

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingPath

class SettingBuilder<T : Any, S : SerializableType<*>>(val name: String) {

    var comment: String? = null

    lateinit var defaultValue: T

    lateinit var serializer: (T) -> S

    lateinit var deserializer: (S) -> T

    lateinit var settingPath: SettingPath

}