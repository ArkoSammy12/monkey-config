package io.github.arkosammy12.monkeyconfig.sections

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import io.github.arkosammy12.monkeyconfig.base.ConfigElement
import io.github.arkosammy12.monkeyconfig.builders.ConfigElementBuilder
import io.github.arkosammy12.monkeyconfig.builders.SectionBuilder

open class DefaultSection(
    sectionBuilder: SectionBuilder,
) : AbstractSection(sectionBuilder) {

    override val configElements: List<ConfigElement>
        get() = this.internalConfigElements

    protected var internalConfigElements: List<ConfigElement>

    init {
        val configElements: MutableList<ConfigElement> = mutableListOf()
        for (configElementBuilder: ConfigElementBuilder<*, *> in sectionBuilder.configElementBuilders) {
            val configElement: ConfigElement = configElementBuilder.build()
            configElements.add(configElement)
        }

        this.internalConfigElements = configElements.toList()
    }


    override fun saveValue(fileConfig: FileConfig) {
        super.saveValue(fileConfig)
        val config: Config = fileConfig.get(this.path.string) ?: return
        config.entrySet().removeIf { entry -> this.configElements.none { element -> element.path.asList.last() == entry.key  } }
    }

}