@file:JvmName("SerializableType")

package xd.arkosammy.monkeyconfig.types

import xd.arkosammy.monkeyconfig.settings.Setting

sealed interface SerializableType<T : Any> {

    val value: T

}

@Suppress("UNCHECKED_CAST")
fun <T : Any, V : SerializableType<*>> setValueSafely(setting: Setting<T, V>, value : SerializableType<*>) {
    when (value) {
        is NumberType<*> -> {
            if(setting.value.defaultSerialized is NumberType<*>) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is BooleanType -> {
            if(setting.value.defaultSerialized is BooleanType) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is EnumType<*> -> {
            if(setting.value.defaultSerialized is EnumType<*>) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is ListType<*> -> {
            if(setting.value.defaultSerialized is ListType<*>) {
                setting.value.setFromSerialized(value as V)
            }
        }
        is StringType -> {
            if(setting.value.defaultSerialized is StringType) {
                setting.value.setFromSerialized(value as V)
            }
        }
    }
}

fun toSerializedType(rawValue: Any): SerializableType<*> {
    return when (rawValue) {
        is SerializableType<*> -> rawValue
        is List<*> -> ListType(rawValue.filterNotNull().map { e -> toSerializedType(e) })
        is Number -> NumberType(rawValue)
        is String -> StringType(rawValue)
        is Boolean -> BooleanType(rawValue)
        is Enum<*> -> EnumType(rawValue)
        else -> throw IllegalArgumentException("Value $rawValue of type \"${rawValue::class.simpleName}\" cannot be converted to an instance of SerializableType")
    }
}