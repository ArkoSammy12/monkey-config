package io.github.arkosammy12.monkeyconfig.util

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestElementPath {

    @Test
    fun `ElementPath should not accept empty nodes`() {

        assertThrows<IllegalArgumentException> {
            ElementPath("")
        }

        assertThrows<IllegalArgumentException> {
            ElementPath("node1", "", "node3")
        }

        assertThrows<IllegalArgumentException> {
            ElementPath(".")
        }

        assertThrows<IllegalArgumentException> {
            ElementPath("node1..node3")
        }

    }

    @Test
    fun `Element Path should not accept nodes with whitespaces`() {

        assertThrows<IllegalArgumentException> {
            ElementPath(" ")
        }

        assertThrows<IllegalArgumentException> {
            ElementPath("node1", " node2", "node3")
        }

        assertThrows<IllegalArgumentException> {
            ElementPath(". ")
        }

        assertThrows<IllegalArgumentException> {
            ElementPath("node1. .node3")
        }

    }

    @Test
    fun `Element Path creation with node lists results in proper string representation`() {

        assertEquals(ElementPath("node").string, "node")

        assertEquals(ElementPath("node1", "node2").string, "node1.node2")

        assertEquals(ElementPath(listOf("node1", "node2", "node3")).string, "node1.node2.node3")

    }

    @Test
    fun `Element Path creation with strings results in proper list representation`() {

        assertEquals(ElementPath("node").asList, listOf("node"))

        assertEquals(ElementPath("node1.node2").asList, listOf("node1", "node2"))

        assertEquals(ElementPath("node1.node2.node3").asList, listOf("node1", "node2", "node3"))

    }

}