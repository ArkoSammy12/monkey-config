package xd.arkosammy.monkeyconfig

import xd.arkosammy.monkeyconfig.util.ElementPath

interface ConfigElement {

    val name: String

    val comment: String?

    val path: ElementPath

}