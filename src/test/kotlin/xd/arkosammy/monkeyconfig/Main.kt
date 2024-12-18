package xd.arkosammy.monkeyconfig

import xd.arkosammy.monkeyconfig.builders.tomlConfigManager
import xd.arkosammy.monkeyconfig.settings.TestNumberSettingImplementation
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
        val configManager = tomlConfigManager("testConfig", TEST_PATH.resolve("test.toml")) {
            booleanSetting("testTopLevelBooleanSetting", true) {
                comment = "Test comment"
            }
            section("testSection") {
                comment = "Test comment here"
                numberSetting<Int>("testNumberSetting", 0) {
                    comment = "Test comment"
                    minValue = 0
                    maxValue = 10
                    implementation = { builder -> TestNumberSettingImplementation(builder) }
                }
                section("testSubsection") {
                    comment = "Test comment"
                    stringSetting("testStringSetting", "hello_dicedpixels") {

                        comment = "Test comment"

                    }
                }
            }
        }
        configManager.saveToFile()
    }

}