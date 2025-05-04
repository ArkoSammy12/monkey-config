package io.github.arkosammy12.monkeyconfig

import io.github.arkosammy12.monkeyconfig.builders.tomlConfigManager
import io.github.arkosammy12.monkeyconfig.settings.TestNumberSetting
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object Main {

    val TEST_PATH: Path = Paths.get("").toAbsolutePath().resolve("configTestDir")

    init {
        if (!Files.exists(TEST_PATH)) {
            Files.createDirectory(TEST_PATH)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val configManager = tomlConfigManager("myFileConfig", TEST_PATH.resolve("test.toml")) {
            booleanSetting("monkeyStyle", true) {
                comment = "This is true by default!"
            }

            section("preferences") {
                comment = "These settings are for preferences."
                numberSetting<Int>("breakDaysLimit", 0) {
                    comment = "How many days I can take a break before getting back in the grind. Default is 0. Minimum is 0 and maximum is 10."
                    minValue = 0
                    maxValue = 10
                    implementation = ::TestNumberSetting
                }
                section("whitelist") {
                    stringSetting("prompt", "You need to be whitelisted!") {
                        comment = "The prompt shown to users who are not whitelisted."
                    }
                    booleanSetting("enabled", true) {
                        comment = "Default is true"
                    }
                    stringListSetting("list", listOf("ArkoSammy12")) {}
                }
            }
        }
        configManager.saveToFile()
    }

}