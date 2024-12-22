package io.arkosammy12.monkeyconfig.base

import io.arkosammy12.monkeyconfig.util.ElementPath

interface ConfigElementContainer {

    val configElements: Collection<ConfigElement>

    fun <T : ConfigElement> containsElement(elementPath: ElementPath, elementClass: Class<in T>): Boolean =
        this.configElements.any { element -> element.path == elementPath && elementClass.isInstance(element) }

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

    @Suppress("UNCHECKED_CAST")
    fun <T : ConfigElement> forEachElement(elementClass: Class<in T>, action: (T) -> Unit) {
        for (configElement: ConfigElement in this.configElements) {
            if (elementClass.isInstance(configElement)) {
                action(configElement as T)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ConfigElement> traverseElements(elementClass: Class<in T>, currentElement: ConfigElementContainer? = null, action: (T) -> Unit) {
        val current: ConfigElementContainer = currentElement ?: this.also { element -> if (elementClass.isInstance(element)) action(element as T) }
        current.forEachElement(elementClass, action)
        current.traverseElements(elementClass, current, action)
    }

}

inline fun <reified T : ConfigElement> ConfigElementContainer.containsElement(elementPath: ElementPath): Boolean =
    this.containsElement(elementPath, T::class.java)

inline fun <reified T : ConfigElement> ConfigElementContainer.getConfigElement(elementPath: ElementPath): T? =
    this.getConfigElement(elementPath, T::class.java)

inline fun <reified T : ConfigElement> ConfigElementContainer.forEachElement(noinline action: (T) -> Unit) {
    this.forEachElement(T::class.java, action)
}

@JvmOverloads
inline fun <reified T : ConfigElement> ConfigElementContainer.traverseElements(currentElement: ConfigElementContainer? = null, noinline action: (T) -> Unit) {
    this.traverseElements(T::class.java, currentElement, action)
}