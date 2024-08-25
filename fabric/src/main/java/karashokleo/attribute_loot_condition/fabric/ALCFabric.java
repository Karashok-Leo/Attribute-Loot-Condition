package karashokleo.attribute_loot_condition.fabric;

import karashokleo.attribute_loot_condition.ALCMod;
import karashokleo.attribute_loot_condition.AttributeLootCondition;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ALCFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ALCMod.init();
        register();
    }

    public static void register()
    {
        Registry.register(
                BuiltInRegistries.LOOT_CONDITION_TYPE,
                AttributeLootCondition.ID,
                AttributeLootCondition.ALC
        );
    }
}
