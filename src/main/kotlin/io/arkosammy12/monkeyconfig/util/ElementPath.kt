package io.arkosammy12.monkeyconfig.util

/**
 * A utility class for representing paths within configuration files to reference and access [io.arkosammy12.monkeyconfig.base.ConfigElement].
 * An [io.arkosammy12.monkeyconfig.util.ElementPath] is represented by a dot separated list of strings called "nodes". Each node
 * cannot contain "." or periods to avoid accidentally creating a new node when it was not intended.
 *
 * @property string The actual string representation of the path.
 */
data class ElementPath(val string: String) {

    init {
        val nodes: List<String> = this.string.split(".")
        for (node: String in nodes) {
            if (node.contains("\\s+") || node.contains(" "))  {
                throw IllegalArgumentException("Setting nodes cannot contain whitespaces!")
            }
            if (node.isBlank() || node.isEmpty()) {
                throw IllegalArgumentException("Setting nodes cannot be blank or empty!")
            }
        }
    }

    /**
     * @param nodes A variadic list of the nodes that constitute this [io.arkosammy12.monkeyconfig.util.ElementPath].
     */
    constructor(vararg nodes: String) : this(nodes.joinToString("."))

    /**
     * @param nodes A list of the nodes that constitutes this [io.arkosammy12.monkeyconfig.util.ElementPath].
     */
    constructor(nodes: List<String>) : this(nodes.joinToString("."))

    /**
     * A list containing all the nodes of the underlying string representation of the [io.arkosammy12.monkeyconfig.util.ElementPath].
     */
    val asList: List<String>
        get() = this.string.split(".")

    /**
     * An array containing all the nodes of the underlying string representation of the [io.arkosammy12.monkeyconfig.util.ElementPath].
     */
    val asArray: Array<String>
        get() = this.asList.toTypedArray()

    /**
     * Checks if this [io.arkosammy12.monkeyconfig.util.ElementPath] contains a node matching [node].
     *
     * @param node The node to look for in this [io.arkosammy12.monkeyconfig.util.ElementPath].
     * @return **true** if the node is contained in this [io.arkosammy12.monkeyconfig.util.ElementPath], **false** otherwise.
     */
    fun contains(node: String) = this.asList.contains(node)

    /**
     * Returns a new [io.arkosammy12.monkeyconfig.util.ElementPath] that contains a new node at the end of its path.
     *
     * @param node The node to include in the returned [io.arkosammy12.monkeyconfig.util.ElementPath].
     * @return An [io.arkosammy12.monkeyconfig.util.ElementPath] with [node] appended.
     */
    fun withAppendedNode(node: String) : ElementPath {
        if (node.contains(".")) {
            throw IllegalArgumentException("Element path node cannot contain \".\"!")
        }
        val nodes: MutableList<String> = this.string.split(".").toMutableList()
        nodes.add(node)
        return ElementPath(nodes)
    }

    /**
     * Returns a new [io.arkosammy12.monkeyconfig.util.ElementPath] that contains a new node at the beginning of its path.
     *
     * @param node The node to include in the returned [io.arkosammy12.monkeyconfig.util.ElementPath].
     * @return An [io.arkosammy12.monkeyconfig.util.ElementPath] with [node] prepended.
     */
    fun withPrependedNode(node: String) : ElementPath {
        if (node.contains(".")) {
            throw IllegalArgumentException("Element path node cannot contain \".\"!")
        }
        val nodes: MutableList<String> = mutableListOf()
        nodes.add(node)
        nodes.addAll(this.string.split("."))
        return ElementPath(nodes)
    }

}