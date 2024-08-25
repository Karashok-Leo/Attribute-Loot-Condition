package karashokleo.attribute_loot_condition;

import net.tinyconfig.ConfigManager;

public final class ALCMod
{
    public static final String MOD_ID = "attribute_loot_condition";

    public static final ConfigManager<ALCConfig> CONFIG = new ConfigManager<>(MOD_ID, new ALCConfig())
            .builder()
            .setDirectory(".")
            .enableLogging(true)
            .build();

    public static void init()
    {
        CONFIG.refresh();
    }
}
