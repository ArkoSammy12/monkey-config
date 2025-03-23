package iol.github.arkosammy12.monkeyconfig;

import io.github.arkosammy12.monkeyconfig.base.ConfigManager;
import io.github.arkosammy12.monkeyconfig.settings.TestNumberSetting;
import kotlin.Unit;

import static io.github.arkosammy12.monkeyconfig.builders.ConfigManagerBuilderKt.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static Path TEST_PATH = Paths.get("").toAbsolutePath().resolve("configTestDir");

    public static void main(String[] args) throws IOException {

        if (!Files.exists(TEST_PATH)) {
            Files.createDirectories(TEST_PATH);
        }

        ConfigManager configManager = tomlConfigManager("myFileConfig", TEST_PATH.resolve("test.toml"), (managerBuilder) -> {
            managerBuilder.booleanSetting("monkeyStyle", true, (monkeyStyle) -> {
               monkeyStyle.setComment("This is true by default!");
               return Unit.INSTANCE;
            });
            managerBuilder.section("preferences", (preferences) -> {
                preferences.setComment("These settings are for preferences.");
                preferences.numberSetting("breakDaysLimit", 0, (breakDaysLimit) -> {
                    breakDaysLimit.setComment("How many days I can take a break before getting back in the grind. Default is 0. Minimum is 0 and maximum is 10.");
                    breakDaysLimit.setMinValue(0);
                    breakDaysLimit.setMaxValue(10);
                    breakDaysLimit.setImplementation(TestNumberSetting::new);
                    return Unit.INSTANCE;
                });
                preferences.section("whitelist", (whitelist -> {
                    whitelist.stringSetting("prompt", "You need to be whitelisted!", (prompt) -> {
                        prompt.setComment("The prompt shown to users who are not whitelisted.");
                        return Unit.INSTANCE;
                    });
                    whitelist.booleanSetting("enabled", true, (enabled) -> {
                        enabled.setComment("Default is true");
                        return Unit.INSTANCE;
                    });
                    whitelist.stringListSetting("list", List.of("ArkoSammy12"), (list) -> Unit.INSTANCE);
                    return Unit.INSTANCE;
                }));
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });
        configManager.saveToFile();

    }

}
