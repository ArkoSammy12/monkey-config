package xd.arkosammy.monkeyconfig.util

import com.electronwill.nightconfig.core.utils.StringUtils

class SettingPath private constructor(private val strings: List<String>) {

    fun getString(): String {
        var path: String = ""
        for ((index, string) in strings.withIndex()) {
            path += string
            if (index + 1 < strings.size) {
                path += "."
            }
        }
        return path
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SettingPath) return false

        if (strings != other.strings) return false

        return true
    }

    override fun hashCode(): Int {
        return strings.hashCode()
    }


    companion object {

        fun of(vararg path: String): SettingPath {

            val strings: MutableList<String> = mutableListOf()

            for (string: String in path) {
                if (string.contains(".")) {
                    throw IllegalArgumentException("Setting paths cannot contain \".\"!")
                }
                strings.add(string.trim())
            }

            return SettingPath(strings.toList())

        }

        fun ofDotted(path: String): SettingPath {
            if (path.isEmpty() || path.isBlank()) {
                throw IllegalArgumentException("Setting path cannot be empty!")
            }
            val originalStrings: List<String> = path.split(".")
            val strings: MutableList<String> = mutableListOf()
            for (string: String in originalStrings) {
                if (string.isEmpty() || string.isBlank()) {
                    throw IllegalArgumentException("Setting path cannot contain empty sections!")
                }
                if (string.contains(".")) {
                    throw IllegalArgumentException("Setting paths cannot contain \".\"!")
                }
                strings.add(string.trim())
            }
            return SettingPath(strings.toList())
        }

    }

}