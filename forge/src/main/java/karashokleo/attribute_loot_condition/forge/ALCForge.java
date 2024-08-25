package karashokleo.attribute_loot_condition.forge;

import karashokleo.attribute_loot_condition.ALCMod;
import karashokleo.attribute_loot_condition.AttributeLootCondition;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod(ALCMod.MOD_ID)
@Mod.EventBusSubscriber(modid = ALCMod.MOD_ID)
public final class ALCForge
{
    public ALCForge()
    {
        ALCMod.init();
    }

    @SubscribeEvent
    public static void register(RegisterEvent event)
    {
        event.register(
                Registries.LOOT_CONDITION_TYPE,
                AttributeLootCondition.ID,
                () -> AttributeLootCondition.ALC
        );
    }
}
