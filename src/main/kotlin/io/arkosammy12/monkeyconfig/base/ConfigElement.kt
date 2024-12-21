package io.arkosammy12.monkeyconfig.base

import com.electronwill.nightconfig.core.file.FileConfig
import io.arkosammy12.monkeyconfig.util.ElementPath

sealed interface ConfigElement {

    val name: String

    val comment: String?

    val path: ElementPath

    fun saveValue(fileConfig: FileConfig)

    fun updateValue(fileConfig: FileConfig)

    fun onInitialized() {}

    fun onUpdated() {}

    fun onSaved() {}

}