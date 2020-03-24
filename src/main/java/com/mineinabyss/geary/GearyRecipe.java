package com.mineinabyss.geary;

import com.mineinabyss.geary.core.ItemUtil.EntityInitializer;
import java.util.Map;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NonNullList;
import net.minecraft.server.v1_15_R1.RecipeItemStack;
import net.minecraft.server.v1_15_R1.ShapedRecipes;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

/**
 * An implementation of a {@link org.bukkit.inventory.ShapedRecipe} that can attach an ECS entity to
 * each output ItemStack.
 * <p>
 * This is necessary because the default ShapedRecipe clones the provided output ItemStack for each
 * new item. This means that if the UUID is set on the initial ItemStack, the same UUID will be used
 * for all outputs of this recipe.
 * <p>
 * TODO(https://github.com/MineInAbyss/geary/issues/10): handle multiple versions of NMS
 */
public class GearyRecipe extends CraftShapedRecipe {

  private GearyService geary;
  private EntityInitializer entityInitializer;

  public GearyRecipe(NamespacedKey key, ItemStack base, GearyService geary,
      EntityInitializer entityInitializer) {
    super(key, base);
    this.geary = geary;
    this.entityInitializer = entityInitializer;
  }

  @Override
  public void addToCraftingManager() {
    String[] shape = this.getShape();
    Map<Character, RecipeChoice> ingred = this.getChoiceMap();
    int width = shape[0].length();
    NonNullList<RecipeItemStack> data = NonNullList.a(shape.length * width, RecipeItemStack.a);

    for (int i = 0; i < shape.length; ++i) {
      String row = shape[i];

      for (int j = 0; j < row.length(); ++j) {
        data.set(i * width + j, this.toNMS(ingred.get(row.charAt(j)), false));
      }
    }

    MinecraftServer.getServer().getCraftingManager().addRecipe(
        new GearyShapedRecipes(CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(),
            width,
            shape.length, data, CraftItemStack
            .asNMSCopy(this.getResult()), geary, entityInitializer));
  }

  private static class GearyShapedRecipes extends ShapedRecipes {

    private final GearyService geary;
    private final EntityInitializer entityInitializer;

    public GearyShapedRecipes(MinecraftKey minecraftkey, String s, int i, int j,
        NonNullList<RecipeItemStack> nonnulllist,
        net.minecraft.server.v1_15_R1.ItemStack itemstack, GearyService geary,
        EntityInitializer entityInitializer) {
      super(minecraftkey, s, i, j, nonnulllist, itemstack);
      this.geary = geary;
      this.entityInitializer = entityInitializer;
    }

    @Override
    public net.minecraft.server.v1_15_R1.ItemStack getResult() {
      net.minecraft.server.v1_15_R1.ItemStack itemStack = super.getResult();

      ItemStack gearyCopy = CraftItemStack.asBukkitCopy(itemStack);
      geary.attachToItemStack(entityInitializer, gearyCopy);

      return CraftItemStack.asNMSCopy(gearyCopy);
    }
  }
}
