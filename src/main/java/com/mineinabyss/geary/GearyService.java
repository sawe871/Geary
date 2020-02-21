package com.mineinabyss.geary;

import com.badlogic.ashley.core.EntitySystem;
import com.mineinabyss.geary.core.ItemUtil.EntityInitializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public interface GearyService {

  ShapedRecipe createRecipe(NamespacedKey key, EntityInitializer entityInitializer,
      ItemStack itemStack);
  void attachToItemStack(EntityInitializer entityInitializer, ItemStack itemStack);
  void addSystem(EntitySystem entitySystem);
}
