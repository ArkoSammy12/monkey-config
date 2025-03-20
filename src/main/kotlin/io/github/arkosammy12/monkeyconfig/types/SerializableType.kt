@file:JvmName("SerializableTypeUtils")

package io.github.arkosammy12.monkeyconfig.types

import io.github.arkosammy12.monkeyconfig.base.Setting

/**
 * Represents a wrapper for a type which can be directly written to and read from a file.
 * Used by [Setting]s
 * to serialize their actual values.
 *
 * @param T The type of the serializable value.
 */
sealed interface SerializableType<T : Any> {

    /**
     * The value of the underlying serializable type.
     */
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