package io.github.arkosammy12.monkeyconfig.builders

import io.github.arkosammy12.monkeyconfig.sections.DefaultSection

class DefaultSectionBuilder(
    name: String,
    manager: ConfigManagerBuilder,
    parent: SectionBuilder<*, *>? = null
) : SectionBuilder<DefaultSection, DefaultSectionBuilder>(name, manager, parent) {

    override var implementation: (DefaultSectionBuilder) -> DefaultSection = ::DefaultSection

    override fun build(): DefaultSection =
        this.implementation(this)

}