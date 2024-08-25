package karashokleo.attribute_loot_condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public record AttributeLootCondition(
        LootContext.EntityTarget entity,
        double min,
        double max
) implements LootItemCondition
{
    public static final ResourceLocation ID = new ResourceLocation(ALCMod.MOD_ID, ALCMod.MOD_ID);
    public static final ALCSerializer SERIALIZER = new ALCSerializer();
    public static final LootItemConditionType ALC = new LootItemConditionType(SERIALIZER);

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
        return () -> new AttributeLootCondition(entity, min, max);
    }

    public static class ALCSerializer implements Serializer<AttributeLootCondition>
    {
        @Override
        public void serialize(JsonObject json, AttributeLootCondition condition, JsonSerializationContext context)
        {
            json.add("entity", context.serialize(condition.entity));
            json.add("min", context.serialize(condition.min));
            json.add("max", context.serialize(condition.max));
        }

        @NotNull
        @Override
        public AttributeLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new AttributeLootCondition(
                    GsonHelper.getAsObject(json, "entity", context, LootContext.EntityTarget.class),
                    GsonHelper.getAsDouble(json, "min"),
                    GsonHelper.getAsDouble(json, "max")
            );
        }
    }
}
