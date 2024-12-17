@file:JvmName("NumberSettingValue")

package xd.arkosammy.monkeyconfig.values

import xd.arkosammy.monkeyconfig.types.NumberType

class NumberSettingValue<T : Number>(
    default: T,
    raw: T = default,
    val lowerBound: T?,
    val upperBound: T?,
    serializer: (T) -> NumberType<T>,
    deserializer: (NumberType<T>) -> T
) : SettingValue<T, NumberType<T>>(default, raw, serializer, deserializer) {

    override var raw: T
        set(value) {
            if (lowerBound != null && value < this.lowerBound) {
                // TODO: LOG
                return
            }
            if (this.upperBound != null && value > this.upperBound) {
                // TODO: LOG
                return
            }
            super.raw = value
        }
        get() = super.raw

    override fun toString(): String =
        "${this::class.simpleName}{defaultValue=$default, value=$raw, defaultSerializedValue=$defaultSerialized, serializedValue=$serialized, minValue=$lowerBound, maxValue=$upperBound}"

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
