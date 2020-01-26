package com.mineinabyss.geary.core;

import com.badlogic.ashley.core.Entity;
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

  private final Plugin plugin;
  private final NamespacedKey componentUUIDKey;
  private final ItemToEntityMapper itemToEntityMapper;

  public ItemUtil(Plugin plugin, ItemToEntityMapper itemToEntityMapper) {
    this.plugin = plugin;

    componentUUIDKey = new NamespacedKey(plugin, "uuid");
    this.itemToEntityMapper = itemToEntityMapper;
  }

  public Optional<Entity> getEcsEntityFromItem(ItemStack itemStack) {
    if (itemStack != null && itemStack.hasItemMeta()) {
      ItemMeta itemMeta = itemStack.getItemMeta();

      if (itemMeta.getPersistentDataContainer().has(componentUUIDKey,
          PersistentDataType.BYTE_ARRAY)) {
        UUID uuid = getUUIDFromBytes(itemMeta.getPersistentDataContainer()
            .get(componentUUIDKey, PersistentDataType.BYTE_ARRAY));

        return itemToEntityMapper.getEntityForUUID(uuid);
      }
    }
    return Optional.empty();
  }

  public ItemStack createItemWithEcsEntity(EntityInitializer entityInitializer, Material material) {
    Entity entity = entityInitializer.initializeEntity();

    UUID uuid = UUID.randomUUID();
    itemToEntityMapper.addEntityWithUUID(uuid, entity);

    ItemStack itemStack = new ItemStack(material);
    ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);

    itemMeta.getPersistentDataContainer()
        .set(componentUUIDKey, PersistentDataType.BYTE_ARRAY, getBytesFromUUID(uuid));

    itemStack.setItemMeta(itemMeta);

    return itemStack;
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
}
