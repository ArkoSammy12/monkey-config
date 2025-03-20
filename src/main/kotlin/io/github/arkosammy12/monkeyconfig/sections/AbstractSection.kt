package io.github.arkosammy12.monkeyconfig.sections

import io.github.arkosammy12.monkeyconfig.base.Section
import io.github.arkosammy12.monkeyconfig.base.sections
import io.github.arkosammy12.monkeyconfig.base.settings
import io.github.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.github.arkosammy12.monkeyconfig.util.ElementPath
import org.slf4j.Logger

abstract class AbstractSection(
    sectionBuilder: SectionBuilder,
) : Section {

    override val name: String = sectionBuilder.name

    override val path: ElementPath = sectionBuilder.path

    override val comment: String? = sectionBuilder.comment

    override val loadBeforeSave: Boolean = sectionBuilder.loadBeforeSave

    protected var internalIsInitialized: Boolean = false

    protected val onInitializedFunction: ((Section) -> Unit)? = sectionBuilder.onInitialized

    protected val onUpdatedFunction: ((Section) ->  Unit)? = sectionBuilder.onUpdated

    protected val onSavedFunction: ((Section) -> Unit)? = sectionBuilder.onSaved

    protected val logger: Logger? = sectionBuilder.logger

    override val isInitialized: Boolean
        get() = this.internalIsInitialized

    override fun onInitialized() {
        this.internalIsInitialized =  true
        this.onInitializedFunction?.let { it(this) }

    }

    override fun onUpdated() {
        this.onUpdatedFunction?.let { it(this) }
    }

    override fun onSaved() {
        this.onSavedFunction?.let { it(this) }
    }

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.settings.size}, subSectionAmount=${this.sections.size}, initialized=$isInitialized, loadBeforeSave=$loadBeforeSave}"

}