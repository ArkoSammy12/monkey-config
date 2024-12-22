@file:JvmName("NumberSettingValueUtils")

package io.arkosammy12.monkeyconfig.values

import io.arkosammy12.monkeyconfig.types.NumberType
import org.slf4j.Logger

class NumberSettingValue<T : Number> @JvmOverloads constructor(
    default: T,
    raw: T = default,
    val minValue: T?,
    val maxValue: T?,
    logger: Logger? = null,
    serializer: (T) -> NumberType<T>,
    deserializer: (NumberType<T>) -> T
) : SettingValue<T, NumberType<T>>(default, raw, logger, serializer, deserializer) {

    override var raw: T
        set(value) {
            if (minValue != null && value < this.minValue) {
                this.logger?.error("Value $value is below the minimum value for $this")
                return
            }
            if (this.maxValue != null && value > this.maxValue) {
                this.logger?.error("Value $value is above the maximum value for $this")
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
