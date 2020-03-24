package com.mineinabyss.geary;

import com.badlogic.ashley.core.EntitySystem;
import com.mineinabyss.geary.core.ItemUtil.EntityInitializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Service interface for {@link Geary}, exposed to other plugins.
 */
public interface GearyService {

  /**
   * Creates a ShapedRecipe with an associated entity initializer. The namespaced key should be
   * provided by the calling plugin.
   * <p>
   * The {@param entityInitializer} will be invoked once per item successfully crafted in the trade
   * window.
   */
  ShapedRecipe createRecipe(NamespacedKey key, EntityInitializer entityInitializer,
      ItemStack itemStack);

  /**
   * Invokes {@param entityInitializer} and associates the resulting ECS entity with the provided
   * {@param itemStack}.
   * <p>
   * Note that this method alters the metadata of the item stack, and that removing the item stack
   * will result in a persistent entity with no associated item.
   */
  void attachToItemStack(EntityInitializer entityInitializer, ItemStack itemStack);

  /**
   * Adds an additional system for processing ECS entities.
   */
  void addSystem(EntitySystem entitySystem);
}
