package io.arkosammy12.monkeyconfig.builders

import io.arkosammy12.monkeyconfig.base.Setting
import io.arkosammy12.monkeyconfig.types.SerializableType
import io.arkosammy12.monkeyconfig.util.ElementPath

/**
 * A builder for [Setting]s. This class includes two new properties, [serializer] and [deserializer] which must be initialized so that
 * the resulting [Setting] knows how to serialize its value to a valid type which can be written to a file and viceversa.
 *
 * @param V The type of the value that the resulting [Setting] will hold.
 * @param S The [SerializableType] of the value which will be used to write to a configuration file and back.
 * @param I The specific implementation of [Setting] that this builder builds to, which has type parameters [V] and [S].
 * @param T The specific implementation of [io.arkosammy12.monkeyconfig.builders.SettingBuilder] which corresponds to the class extending this builder class,
 * which has type parameters [V], [S], [I] and [T].
 */
abstract class SettingBuilder<V : Any, S : SerializableType<*>, I : Setting<V, S>, T : SettingBuilder<V, S, I, T>>(
    name: String,
    val defaultValue: V,
    override val path: ElementPath
) : ConfigElementBuilder<I, T>(name) {

    /**
     * A function that transforms the [Setting]'s value to a [SerializableType] to then be written to a file.
     */
    open lateinit var serializer: (V) -> S

    /**
     * A function that transforms a [SerializableType] value read from a configuration file to a value of type [V]
     * which is the actual type of the [Setting].
     */
    open lateinit var deserializer: (S) -> V

    override lateinit var implementation: (T) -> I

}