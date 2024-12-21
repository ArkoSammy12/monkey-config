package io.arkosammy12.monkeyconfig.sections

import io.arkosammy12.monkeyconfig.base.Section
import io.arkosammy12.monkeyconfig.base.sections
import io.arkosammy12.monkeyconfig.base.settings
import io.arkosammy12.monkeyconfig.builders.SectionBuilder
import io.arkosammy12.monkeyconfig.util.ElementPath

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

    override val isInitialized: Boolean
        get() = this.internalIsInitialized

    override fun onInitialized() {
        this.internalIsInitialized =  true
        if (onInitializedFunction != null) {
            this.onInitializedFunction(this)
        }
    }

    override fun onUpdated() {
        if (onUpdatedFunction != null) {
            this.onUpdatedFunction(this)
        }
    }

    override fun onSaved() {
        if (onSavedFunction != null) {
            this.onSavedFunction(this)
        }
    }

    override fun toString(): String =
        "${this::class.simpleName}{name=$name, comment=$comment, settingAmount=${this.settings.size}, subSectionAmount=${this.sections.size}, initialized=$isInitialized, loadBeforeSave=$loadBeforeSave}"

}