package karashokleo.attribute_loot_condition;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.lang.reflect.Type;
import java.util.List;

public class ALCConfig
{
    public List<WeightedAttributeEntry> attributeWeights = List.of(
            new WeightedAttributeEntry(Attributes.MAX_HEALTH, 1),
            new WeightedAttributeEntry(Attributes.ARMOR, 1),
            new WeightedAttributeEntry(Attributes.ARMOR_TOUGHNESS, 1),
            new WeightedAttributeEntry(Attributes.ATTACK_DAMAGE, 1)
    );

    @JsonAdapter(WeightedAttributeAdapter.class)
    public record WeightedAttributeEntry(Attribute attribute, double weight)
    {
    }

    public static class WeightedAttributeAdapter implements JsonSerializer<WeightedAttributeEntry>, JsonDeserializer<WeightedAttributeEntry>
    {
        private static final String ATTRIBUTE_KEY = "attribute";
        private static final String WEIGHT_KEY = "weight";

        @Override
        public WeightedAttributeEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonObject object = json.getAsJsonObject();
            String attrId = GsonHelper.getAsString(object, ATTRIBUTE_KEY);
            Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(new ResourceLocation(attrId));
            double weight = GsonHelper.getAsDouble(object, WEIGHT_KEY);
            return new WeightedAttributeEntry(attribute, weight);
        }

        @Override
        public JsonElement serialize(WeightedAttributeEntry src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject object = new JsonObject();
            String attribute = BuiltInRegistries.ATTRIBUTE.getResourceKey(src.attribute).orElseThrow().location().toString();
            object.addProperty(ATTRIBUTE_KEY, attribute);
            object.addProperty(WEIGHT_KEY, src.weight);
            return object;
        }
    }
}
