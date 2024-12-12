package xd.arkosammy.monkeyconfig.builders

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import java.nio.file.Path

class ConfigManagerBuilder<C : Config> internal constructor(val fileName: String, val fileFormat: ConfigFormat<C>, filePath: Path) {


    private val sections: MutableList<SectionBuilder> = mutableListOf()

    fun section(sectionName: String, builder: SectionBuilder.() -> Unit) {
        val sectionBuilder: SectionBuilder = SectionBuilder(sectionName, this)
        builder(sectionBuilder)
        this.sections.add(sectionBuilder)
    }

}

// TODO: Make this return the actual config manager instance
// TODO: Dots on names are illegal. Make sure to check for these
fun <C : Config> configManager(fileName: String, fileFormat: ConfigFormat<C>, filePath: Path, builder: ConfigManagerBuilder<C>.() -> Unit) {

    val configManagerBuilder: ConfigManagerBuilder<C> = ConfigManagerBuilder(fileName, fileFormat, filePath)
    builder(configManagerBuilder)

}