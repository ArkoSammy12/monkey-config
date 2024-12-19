package io.arkosammy12.monkeyconfig

import io.arkosammy12.monkeyconfig.util.ElementPath

// TODO: Integrate this better into the API: Mirror this hierarchy in the builder classes,
//  utilize this for lookup methods within sections and config managers
interface ConfigElement {

    val name: String

    val comment: String?

    val path: ElementPath

}