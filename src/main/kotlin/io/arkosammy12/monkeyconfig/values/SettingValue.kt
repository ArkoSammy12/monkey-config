package io.arkosammy12.monkeyconfig.values

import io.arkosammy12.monkeyconfig.types.SerializableType
import org.slf4j.Logger

/**
 * A utility class used to manage the value of a [io.arkosammy12.monkeyconfig.base.Setting], and is used
 * by [io.arkosammy12.monkeyconfig.settings.AbstractSetting]s.
 *
 * @param default The default value of the associated [io.arkosammy12.monkeyconfig.base.Setting].
 * @param raw The actual value that the associated [io.arkosammy12.monkeyconfig.base.Setting] holds.
 * @param logger An optional [Logger] provided by the associated [io.arkosammy12.monkeyconfig.base.ConfigManager].
 * @param serializer A function that transforms the raw value of type [V] into the serialized value of type [S].
 * @param deserializer A function that transforms a serialized value of type [S] into the raw value of type [V].
 * @param V The type of the raw value held by the associated [io.arkosammy12.monkeyconfig.base.Setting].
 * @param S The [SerializableType] used for writing the associated [io.arkosammy12.monkeyconfig.base.Setting] to a configuration file.
 */
open class SettingValue<V : Any, S : SerializableType<*>> @JvmOverloads constructor(
    val default: V,
    open var raw: V = default,
    protected val logger: Logger? = null,
    private val serializer: (V) -> S,
    private val deserializer: (S) -> V
) {

    /**
     * The value of the associated [io.arkosammy12.monkeyconfig.base.Setting] in its serialized form.
     */
    open val serialized: S
        get() = this.serializer(this.raw)

    /**
     * The default value of the associated [io.arkosammy12.monkeyconfig.base.Setting]
     */
    open val defaultSerialized: S
        get() = this.serializer(this.default)

    /**
     * Sets the [raw] value of the associated [io.arkosammy12.monkeyconfig.base.Setting] via a serializable type instance.
     *
     * @param serialized The [SerializableType] instance to use for setting the [raw] value.
     */
    open fun setFromSerialized(serialized: S) {
        this.raw = this.deserializer(serialized)
    }

    /**
     * Sets the [raw] value of the associated [io.arkosammy12.monkeyconfig.base.Setting] to the [default] value.
     */
    open fun reset() {
        this.raw = default
    }

    override fun toString(): String =
        "${this::class.simpleName}{defaultValue=$default, value=$raw, defaultSerializedValue=$defaultSerialized, serializedValue=$serialized}"

}