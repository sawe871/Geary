package com.mineinabyss.geary.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.ecs.EntityMapper;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ItemUtil {

  private final NamespacedKey componentIdKey;
  private final EntityMapper entityMapper;
  private Engine engine;

  public ItemUtil(Plugin plugin, EntityMapper entityMapper,
      Engine engine) {
    componentIdKey = new NamespacedKey(plugin, "uuid");
    this.entityMapper = entityMapper;
    this.engine = engine;
  }

  public Optional<Entity> getEcsEntityFromItem(ItemStack itemStack) throws ItemDegradedException {
    if (itemStack != null && itemStack.hasItemMeta()) {
      ItemMeta itemMeta = itemStack.getItemMeta();

      if (itemMeta.getPersistentDataContainer().has(componentIdKey,
          PersistentDataType.BYTE_ARRAY)) {
        Optional<UUID> uuid = Optional.ofNullable(itemMeta.getPersistentDataContainer()
            .get(componentIdKey, PersistentDataType.BYTE_ARRAY)).map(ItemUtil::getUUIDFromBytes);

        Optional<Entity> entity = uuid.map(entityMapper::getEntity);

        // If this has a UUID but no mapping, kill it.
        if (!entity.isPresent()) {
          throw new ItemDegradedException();
        }

        return entity;
      }
    }
    return Optional.empty();
  }

  public ItemStack createItemWithEcsEntity(EntityInitializer entityInitializer, Material material) {

    ItemStack itemStack = new ItemStack(material);
    ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);

    itemStack.setItemMeta(itemMeta);

    attachToItemStack(entityInitializer, itemStack);

    return itemStack;
  }

  public void attachToItemStack(EntityInitializer entityInitializer, ItemStack itemStack) {
    Entity entity = entityInitializer.initializeEntity();

    engine.addEntity(entity);
    UUID uuid = entityMapper.getId(entity);

    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.getPersistentDataContainer()
        .set(componentIdKey, PersistentDataType.BYTE_ARRAY, getBytesFromUUID(uuid));

    itemStack.setItemMeta(itemMeta);
  }

  public static byte[] getBytesFromUUID(UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());

    return bb.array();
  }

  public static UUID getUUIDFromBytes(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    long high = byteBuffer.getLong();
    long low = byteBuffer.getLong();

    return new UUID(high, low);
  }

  @FunctionalInterface
  public interface EntityInitializer {

    Entity initializeEntity();
  }

  static class ItemDegradedException extends Exception {

  }
}
