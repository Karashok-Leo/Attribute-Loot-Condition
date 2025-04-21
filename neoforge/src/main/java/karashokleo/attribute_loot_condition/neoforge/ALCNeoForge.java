package karashokleo.attribute_loot_condition.neoforge;

import karashokleo.attribute_loot_condition.ALCMod;
import karashokleo.attribute_loot_condition.AttributeWeightedSumLootCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ALCMod.MOD_ID)
public final class ALCNeoForge
{
    public static final DeferredRegister<LootItemConditionType> REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, ALCMod.MOD_ID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> ALC_HOLDER = REGISTER.register(
            AttributeWeightedSumLootCondition.ID.getPath(),
            () -> AttributeWeightedSumLootCondition.ALC
    );

    public ALCNeoForge(IEventBus eventBus)
    {
        ALCMod.init();
        register(eventBus);
    }

    public static void register(IEventBus eventBus)
    {
        REGISTER.register(eventBus);
    }
}
