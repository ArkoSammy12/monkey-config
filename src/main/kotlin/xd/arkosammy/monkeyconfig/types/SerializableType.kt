package xd.arkosammy.monkeyconfig.types

sealed interface SerializableType<T : Any> {

    val value: T

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