package com.mineinabyss.geary.core;

import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.core.ItemUtil.ItemDegradedException;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.ProjectileHitGround;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ActionListener implements Listener {

  private final ProjectileMapper projectileMapper;
  private final ItemUtil itemUtil;

  public ActionListener(ProjectileMapper projectileMapper, ItemUtil itemUtil) {
    this.projectileMapper = projectileMapper;
    this.itemUtil = itemUtil;
  }

  @EventHandler
  public void onPlayerAct(PlayerInteractEvent event) {
    switch (event.getAction()) {
      case LEFT_CLICK_AIR:
      case LEFT_CLICK_BLOCK:
        Optional<Entity> mainHand = removeOrGet(event.getItem(),
            event.getPlayer().getInventory());

        Location hitLoc = event.getPlayer().getEyeLocation().clone()
            .add(event.getPlayer().getEyeLocation().getDirection().multiply(2));

        mainHand.ifPresent(entity -> {
          entity.add(new Activated());
          entity.add(new Position(hitLoc));
          event.setCancelled(true);
        });
        break;
    }
  }

  @EventHandler
  public void onPlayerHoldItem(PlayerItemHeldEvent itemHeldEvent) {
    int slot = itemHeldEvent.getNewSlot();
    ItemStack itemStack = itemHeldEvent.getPlayer().getInventory().getItem(slot);

    Optional<Entity> entityOptional = removeOrGet(itemStack,
        itemHeldEvent.getPlayer().getInventory());
    entityOptional.ifPresent(entity -> entity.remove(Equipped.class));

    entityOptional
        .ifPresent(entity -> {
              entity.add(new Equipped(itemHeldEvent.getPlayer()));
            }
        );
  }

  @EventHandler
  public void onProjectileHitEvent(ProjectileHitEvent projectileHitEvent) {
    Entity entity = projectileMapper.getEntity(projectileHitEvent.getEntity());

    if (entity != null) {
      entity.add(new ProjectileHitGround());
      entity.add(new Position(projectileHitEvent.getEntity().getLocation()));
      projectileMapper.removeProjectile(projectileHitEvent.getEntity());
    }
  }


  @EventHandler
  public void onPlayerEquipItem(InventoryClickEvent inventoryClickEvent) {
    if (inventoryClickEvent.getSlotType() == SlotType.ARMOR) {

      InventoryHolder inventoryHolder = inventoryClickEvent.getInventory().getHolder();

      if (inventoryHolder instanceof Player) {
        int slot = inventoryClickEvent.getSlot();

        if (slot == 36) {
//          Entity speedyShoes = PredefinedArtifacts.createSpeedyShoes();
//
//          speedyShoes.add(new Equipped((Player) inventoryHolder));
//
//          EntityData data = entityToEntityMapper
//              .getEntity((org.bukkit.entity.Entity) inventoryHolder);
//
//          switch (inventoryClickEvent.getAction()) {
//            case PLACE_ONE:
//            case PLACE_ALL:
//            case PLACE_SOME:
//            case SWAP_WITH_CURSOR:
//              data.getEquipment().getFeet().ifPresent(engine::removeEntity);
//
//              data.getEquipment().setFeet(speedyShoes);
//              engine.addEntity(speedyShoes);
//              break;
//            case PICKUP_ALL:
//            case PICKUP_HALF:
//            case PICKUP_SOME:
//            case PICKUP_ONE:
//              data.getEquipment().getFeet().ifPresent(engine::removeEntity);
//              data.getEquipment().setFeet(null);
//              break;
        }
//        }
      }
    }
  }

  private Optional<Entity> removeOrGet(ItemStack itemStack, Inventory inventory) {
    try {
      return itemUtil.getEcsEntityFromItem(itemStack);
    } catch (ItemDegradedException e) {
      inventory.remove(itemStack);
      return Optional.empty();
    }
  }
}
