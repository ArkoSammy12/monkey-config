# Monkey Config

```kotlin
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
```

---

Monkey Config's goal is to be an easy-to-use,
declarative configuration library
that acts as a wrapper for [TheElectronWill's night-config library](https://github.com/TheElectronWill/night-config).

This library is intended to be used for applications where it makes sense for user to edit configuration files directly. 
These configurations can be reloaded and saved on the fly, so this can also act as a back-end for any kind of configuration UI that is also intended to be edited manually.

Furthermore, this library:

- Helps with setting up a configuration schema, using different config formats, such as JSON or TOML.
- Easily extendable. Integrates easily with your own implementation.

At the moment, Monkey Config is not multiplatform, and only targets the JVM platform.

## Gradle
---

To include this library in your project, first include the following repository in your `build.gradle.kts` build script.

```kotlin
maven {
    name = "enjaraiMavenReleases"
    url = uri("https://maven.enjarai.dev/releases")
}
```

Then, you can include the following in your `dependencies` block.

```kotlin
dependencies {
    implementation("io.github.arkosammy12:monkey-config:${version}")
}

```

Where `version` is the latest version which can be found in the Releases section.