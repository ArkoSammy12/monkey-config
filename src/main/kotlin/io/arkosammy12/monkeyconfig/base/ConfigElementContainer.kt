package io.arkosammy12.monkeyconfig.base

import io.arkosammy12.monkeyconfig.util.ElementPath

/**
 * A container of [ConfigElement]. Usually a [ConfigElementContainer] is also a [ConfigElement], which is the case for
 * [Section]s, which are the most common type of elements which can also have other elements within it.
 *
 * When implementing this interface for types that are meant to also be contained within [ConfigElementContainer], the [ConfigElement] interface should also be implemented.
 */
interface ConfigElementContainer {

    /**
     * An arbitrary collection of [ConfigElement]s. Implementors should return an immutable view of this collection
     * to avoid accidentally mutating this [ConfigElementContainer]. If the desired goal is to iterate through the [ConfigElement]s
     * in this instance, use one of the iteration methods provided by this interface.
     *
     * @see [ConfigElementContainer.forEachElement]
     * @see [ConfigElementContainer.traverseElements]
     */
    val configElements: Collection<ConfigElement>

    /**
     * Check whether there this instance contains an element whose path matches [elementPath], and whose types matches [elementClass].
     * This default implementation only checks the [ConfigElement]s contained in [configElements], which, by default, does not include the nested [ConfigElement]s.
     *
     * @param elementPath The [ElementPath] to match for.
     * @param elementClass The [Class] used to narrow down the check to a specific implementation of [ConfigElement]
     * @param T The specific implementation of [ConfigElement] to check against when looking for a matching [ConfigElement].
     *
     * @return **True** if a [ConfigElement] of type [T] and path [elementPath] was found, **False** otherwise.
     */
    fun <T : ConfigElement> containsElement(elementPath: ElementPath, elementClass: Class<in T>): Boolean =
        this.configElements.any { element -> element.path == elementPath && elementClass.isInstance(element) }

    /**
     * Used to access a [ConfigElement] contained within this [ConfigElementContainer] and whose path matches [elementPath], and whose type matches [elementClass].
     * This default implementation only checks the [ConfigElement]s contained in [configElements], which, by default, does not include the nested [ConfigElement]s.
     *
     * @param elementPath The [ElementPath] to match for.
     * @param elementClass The [Class] used to narrow down the check to a specific implementation of [ConfigElement]
     * @param T The specific implementation of [ConfigElement] to check against when looking for a matching [ConfigElement].
     *
     * @return The corresponding [ConfigElement] if found, or **null** otherwise.
     */
    fun <T : ConfigElement> getConfigElement(elementPath: ElementPath, elementClass: Class<in T>): T? {
        var returnedElement: T? = null
        this.traverseElements(elementClass) { element ->
            if (element.path == elementPath && elementClass.isInstance(element)) {
                returnedElement = element
                return@traverseElements
            }
        }
        return returnedElement
    }

    /**
     * Runs the given [action] for each [ConfigElement] of type [elementClass].
     * By default, it only iterates through the [ConfigElement]s contained within [configElements], which, by default, does not include nested [ConfigElement]s.
     *
     * @param T The specific implementation of [ConfigElement] to run this [action] for.
     * @param elementClass The [Class] that represents the type [T] of the [ConfigElement] to match for.
     * @param action The function to run for each [ConfigElement] of type [T].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ConfigElement> forEachElement(elementClass: Class<in T>, action: (T) -> Unit) {
        for (configElement: ConfigElement in this.configElements) {
            if (elementClass.isInstance(configElement)) {
                action(configElement as T)
            }
        }
    }

    /**
     * Runs the given [action] for each [ConfigElement] of type [elementClass] **and all of the [ConfigElement] contained within the [ConfigElementContainer]s of this instance.
     *
     * @param T The specific implementation of [ConfigElement] to run this [action] for.
     * @param elementClass The [Class] that represents the type [T] of the [ConfigElement] to match for.
     * @param action The function to run for each [ConfigElement] of type [T].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ConfigElement> traverseElements(elementClass: Class<in T>, currentElement: ConfigElementContainer? = null, action: (T) -> Unit) {
        val current: ConfigElementContainer = currentElement ?: this.also { element -> if (elementClass.isInstance(element)) action(element as T) }
        current.forEachElement(elementClass, action)
        current.forEachElement<ConfigElement> { element ->
            if (element is ConfigElementContainer) {
                element.traverseElements(elementClass, element, action)
            }
        }
    }

}

/**
 * Check whether there this instance contains an element whose path matches [elementPath], and whose types matches [T].
 * This default implementation only checks the [ConfigElement]s contained in [configElements], which, by default, does not include the nested [ConfigElement]s.
 *
 * @param elementPath The [ElementPath] to match for.
 * @param T The specific implementation of [ConfigElement] to check against when looking for a matching [ConfigElement].
 *
 * @return **True** if a [ConfigElement] of type [T] and path [elementPath] was found, **False** otherwise.
 */
inline fun <reified T : ConfigElement> ConfigElementContainer.containsElement(elementPath: ElementPath): Boolean =
    this.containsElement(elementPath, T::class.java)

/**
 * Used to access a [ConfigElement] contained within this [ConfigElementContainer] and whose path matches [elementPath], and whose type matches [T].
 * This default implementation only checks the [ConfigElement]s contained in [configElements], which, by default, does not include the nested [ConfigElement]s.
 *
 * @param elementPath The [ElementPath] to match for.
 * @param T The specific implementation of [ConfigElement] to check against when looking for a matching [ConfigElement].
 *
 * @return The corresponding [ConfigElement] if found, or **null** otherwise.
 */
inline fun <reified T : ConfigElement> ConfigElementContainer.getConfigElement(elementPath: ElementPath): T? =
    this.getConfigElement(elementPath, T::class.java)

/**
 * Runs the given [action] for each [ConfigElement] of type [T].
 * By default, it only iterates through the [ConfigElement]s contained within [configElements], which, by default, does not include nested [ConfigElement]s.
 *
 * @param T The specific implementation of [ConfigElement] to run this [action] for.
 * @param action The function to run for each [ConfigElement] of type [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ConfigElement> ConfigElementContainer.forEachElement(noinline action: (T) -> Unit) {
    this.forEachElement(T::class.java, action)
}

/**
 * Runs the given [action] for each [ConfigElement] of type [T] **and all of the [ConfigElement] contained within the [ConfigElementContainer]s of this instance.
 *
 * @param T The specific implementation of [ConfigElement] to run this [action] for.
 * @param action The function to run for each [ConfigElement] of type [T].
 */
@JvmOverloads
inline fun <reified T : ConfigElement> ConfigElementContainer.traverseElements(currentElement: ConfigElementContainer? = null, noinline action: (T) -> Unit) {
    this.traverseElements(T::class.java, currentElement, action)
}