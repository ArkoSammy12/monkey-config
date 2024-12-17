package xd.arkosammy.monkeyconfig.util

data class ElementPath(val string: String) {

    init {
        val nodes: List<String> = this.string.split(".")
        for (node: String in nodes) {
            if (node.contains("\\s+"))  {
                throw IllegalArgumentException("Setting nodes cannot contain whitespaces!")
            }
            if (node.isBlank() || node.isEmpty()) {
                throw IllegalArgumentException("Setting nodes cannot be blank or empty!")
            }
        }
    }

    constructor(vararg nodes: String) : this(nodes.joinToString("."))

    constructor(nodes: List<String>) : this(nodes.joinToString("."))

    val asList: List<String>
        get() = this.string.split(".")

    val asArray: Array<String>
        get() = this.asList.toTypedArray()

    fun withAppendedNode(node: String) : ElementPath {
        if (node.contains(".")) {
            throw IllegalArgumentException("Element path node cannot contain \".\"!")
        }
        val nodes: MutableList<String> = this.string.split(".").toMutableList()
        nodes.add(node)
        return ElementPath(nodes)
    }

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