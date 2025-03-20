@file:JvmName("NumberSettingValueUtils")

package io.github.arkosammy12.monkeyconfig.values

import io.github.arkosammy12.monkeyconfig.types.NumberType
import org.slf4j.Logger

/**
 * A [SettingValue] that stores a numerical value.
 *
 * @property minValue An optional numerical value that sets the lower bound for the raw value of the associated [io.github.arkosammy12.monkeyconfig.base.Setting].
 * @property maxValue An optional numerical value that sets the upper bound for the raw value of the associated [io.github.arkosammy12.monkeyconfig.base.Setting].
 * @param T The specific type of the numerical value.
 */
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
