package com.mineinabyss.geary.state;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mineinabyss.geary.Geary;
import com.mineinabyss.geary.ecs.EntityRef;
import com.mineinabyss.geary.state.adapters.ComponentTypeAdapterFactory;
import com.mineinabyss.geary.state.adapters.EntityRefDeserializer;
import com.mineinabyss.geary.state.adapters.EntityTypeAdapter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Reads config based recipes.
 */
public class RecipeReader {

  private final Gson gson;

  public RecipeReader(Geary geary) {
    gson = new GsonBuilder().registerTypeAdapterFactory(new ComponentTypeAdapterFactory())
        .registerTypeAdapter(Entity.class, new EntityTypeAdapter())
        .registerTypeAdapter(EntityRef.class, new EntityRefDeserializer())
        .registerTypeAdapter(ItemStack.class,
            (InstanceCreator<ItemStack>) type -> new ItemStack(Material.BARRIER, 1))
        .registerTypeAdapter(ShapedRecipe.class, new ShapedRecipeDeserializer(geary))
        .create();
  }

  public Iterable<ShapedRecipe> readRecipes(Reader config) {
    List<ShapedRecipe> shapedRecipes = gson.fromJson(config, Recipes.class).getRecipes();

    return ImmutableList.copyOf(shapedRecipes);
  }

  /**
   * Data class to read recipes into.
   */
  private static class Recipes {

    private final List<ShapedRecipe> recipes;

    public Recipes(List<ShapedRecipe> recipes) {
      this.recipes = recipes;
    }

    public List<ShapedRecipe> getRecipes() {
      return recipes;
    }
  }

  static class ShapedRecipeDeserializer implements JsonDeserializer<ShapedRecipe> {

    private Geary geary;

    public ShapedRecipeDeserializer(Geary geary) {
      this.geary = geary;
    }

    @Override
    public ShapedRecipe deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext context) throws JsonParseException {

      String key = jsonElement.getAsJsonObject().getAsJsonPrimitive("key").getAsString();

      ItemStack itemStack = context
          .deserialize(jsonElement.getAsJsonObject().get("output"), ItemStack.class);

      JsonArray components = jsonElement.getAsJsonObject().getAsJsonArray("initializer");

      ShapedRecipe shapedRecipe = geary.createRecipe(new NamespacedKey(geary, key), () -> {
        Entity entity = new Entity();

        components.forEach(e -> entity.add(context.deserialize(e, Component.class)));
        return entity;
      }, itemStack);

      JsonArray rows = jsonElement.getAsJsonObject().getAsJsonArray("rows");

      ImmutableList<String> rowList = StreamSupport.stream(rows.spliterator(), false)
          .map(JsonElement::getAsString)
          .collect(toImmutableList());

      shapedRecipe.shape(
          rowList.toArray(new String[rows.size()]));

      Map<String, String> ingredients = context
          .deserialize(jsonElement.getAsJsonObject().getAsJsonObject("ingredients"),
              new TypeToken<Map<String, String>>() {
              }.getType());

      ingredients.forEach((k, v) -> {
        shapedRecipe.setIngredient(k.charAt(0), Material.valueOf(v));
      });

      return shapedRecipe;
    }
  }
}
