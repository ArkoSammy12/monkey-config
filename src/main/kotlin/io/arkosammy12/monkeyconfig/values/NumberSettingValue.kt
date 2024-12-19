@file:JvmName("NumberSettingValueUtils")

package io.arkosammy12.monkeyconfig.values

import io.arkosammy12.monkeyconfig.types.NumberType

class NumberSettingValue<T : Number>(
    default: T,
    raw: T = default,
    val minValue: T?,
    val maxValue: T?,
    serializer: (T) -> NumberType<T>,
    deserializer: (NumberType<T>) -> T
) : SettingValue<T, NumberType<T>>(default, raw, serializer, deserializer) {

    override var raw: T
        set(value) {
            if (minValue != null && value < this.minValue) {
                // TODO: LOG
                return
            }
            if (this.maxValue != null && value > this.maxValue) {
                // TODO: LOG
                return
            }
            super.raw = value
        }
        get() = super.raw

    override fun toString(): String =
        "${this::class.simpleName}{defaultValue=$default, value=$raw, defaultSerializedValue=$defaultSerialized, serializedValue=$serialized, minValue=$minValue, maxValue=$maxValue}"

}

operator fun Number.compareTo(other: Number): Int {
    val thisAsDouble: Double = this.toDouble()
    val otherAsDouble: Double = other.toDouble()
    return when {
        thisAsDouble > otherAsDouble -> 1
        thisAsDouble < otherAsDouble -> -1
        else -> 0
    }
}
