package karashokleo.attribute_loot_condition;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public record AttributeWeightedSumLootCondition(
        LootContext.EntityTarget entity,
        double min,
        double max
) implements LootItemCondition
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ALCMod.MOD_ID, "attribute_weighted_sum");
    public static final MapCodec<AttributeWeightedSumLootCondition> CODEC = RecordCodecBuilder.mapCodec(
            (instance) -> instance
                    .group(
                            LootContext.EntityTarget.CODEC
                                    .fieldOf("entity")
                                    .forGetter(AttributeWeightedSumLootCondition::entity),
                            Codec.DOUBLE
                                    .fieldOf("min")
                                    .forGetter(AttributeWeightedSumLootCondition::min),
                            Codec.DOUBLE
                                    .fieldOf("max")
                                    .forGetter(AttributeWeightedSumLootCondition::max)
                    )
                    .apply(instance, AttributeWeightedSumLootCondition::new)
    );
    public static final LootItemConditionType ALC = new LootItemConditionType(CODEC);

    public static double getTotalValue(LivingEntity entity)
    {
        List<ALCConfig.WeightedAttributeEntry> attributeWeights = ALCMod.CONFIG.value.attributeWeights;
        double totalValue = 0;
        for (ALCConfig.WeightedAttributeEntry entry : attributeWeights)
        {
            AttributeInstance attribute = entity.getAttribute(entry.attribute());
            double value = attribute == null ? 0 : attribute.getValue();
            totalValue += value * entry.weight();
        }
        return totalValue;
    }

    @Override
    public boolean test(LootContext lootContext)
    {
        if (lootContext.getParam(this.entity.getParam()) instanceof LivingEntity livingEntity)
        {
            double totalValue = getTotalValue(livingEntity);
            return (min <= 0 || totalValue >= min) &&
                   (max <= 0 || totalValue < max);
        } else return false;
    }

    @NotNull
    @Override
    public Set<LootContextParam<?>> getReferencedContextParams()
    {
        return ImmutableSet.of(LootContextParams.ORIGIN, this.entity.getParam());
    }

    @NotNull
    @Override
    public LootItemConditionType getType()
    {
        return ALC;
    }

    public static LootItemCondition.Builder create(LootContext.EntityTarget entity, int min, int max)
    {
        return () -> new AttributeWeightedSumLootCondition(entity, min, max);
    }
}
