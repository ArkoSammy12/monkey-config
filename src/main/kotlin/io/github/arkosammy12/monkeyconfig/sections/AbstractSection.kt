package io.github.arkosammy12.monkeyconfig.sections

import io.github.arkosammy12.monkeyconfig.base.Section
import io.github.arkosammy12.monkeyconfig.base.sections
import io.github.arkosammy12.monkeyconfig.base.settings
import io.github.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.github.arkosammy12.monkeyconfig.util.ElementPath
import org.slf4j.Logger

abstract class AbstractSection<I : Section, A : SectionBuilder<I, A>>(
    sectionBuilder: SectionBuilder<I, A>,
) : Section {

    override val name: String = sectionBuilder.name

    override val path: ElementPath = sectionBuilder.path

    override val comment: String? = sectionBuilder.comment

    override val loadBeforeSave: Boolean = sectionBuilder.loadBeforeSave

    protected var internalIsInitialized: Boolean = false

    protected val onInitializedFunction: ((I) -> Unit)? = sectionBuilder.onInitialized

    protected val onUpdatedFunction: ((I) ->  Unit)? = sectionBuilder.onUpdated

    protected val onSavedFunction: ((I) -> Unit)? = sectionBuilder.onSaved

    protected val logger: Logger? = sectionBuilder.logger

    override val isInitialized: Boolean
        get() = this.internalIsInitialized


    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.settings.size}, subSectionAmount=${this.sections.size}, initialized=$isInitialized, loadBeforeSave=$loadBeforeSave}"

}