package com.mineinabyss.geary.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.PredefinedArtifacts;
import com.mineinabyss.geary.core.EntityToEntityMapper.EntityData;
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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ActionListener implements Listener {

  private final EntityToEntityMapper entityToEntityMapper;
  private final ProjectileMapper projectileMapper;
  private final ItemUtil itemUtil;
  private Engine engine;

  public ActionListener(EntityToEntityMapper entityToEntityMapper,
      ProjectileMapper projectileMapper,
      ItemUtil itemUtil, Engine engine) {
    this.entityToEntityMapper = entityToEntityMapper;
    this.projectileMapper = projectileMapper;
    this.itemUtil = itemUtil;
    this.engine = engine;
  }

  @EventHandler
  public void onPlayerAct(PlayerInteractEvent playerInteractEvent) {
    switch (playerInteractEvent.getAction()) {
      case LEFT_CLICK_AIR:
      case LEFT_CLICK_BLOCK:
        Optional<Entity> mainHand = entityToEntityMapper.getEntity(playerInteractEvent.getPlayer())
            .getEquipment().getMainHand();

        Location hitLoc = playerInteractEvent.getPlayer().getEyeLocation().clone()
            .add(playerInteractEvent.getPlayer().getEyeLocation().getDirection().multiply(2));

        mainHand.ifPresent(entity -> {
          entity.add(new Activated());
          entity.add(new Position(hitLoc));
          playerInteractEvent.setCancelled(true);
        });
        break;
    }
  }

  @EventHandler
  public void onPlayerHoldItem(PlayerItemHeldEvent itemHeldEvent) {
    int slot = itemHeldEvent.getNewSlot();
    EntityData data = entityToEntityMapper.getEntity(itemHeldEvent.getPlayer());

    ItemStack itemStack = itemHeldEvent.getPlayer().getInventory().getItem(slot);

    data.getEquipment().getMainHand().ifPresent(entity -> entity.remove(Equipped.class));

    itemUtil.getEcsEntityFromItem(itemStack)
        .ifPresent(entity -> {
              entity.add(new Equipped(itemHeldEvent.getPlayer()));
              data.getEquipment().setMainHand(entity);
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
          Entity speedyShoes = PredefinedArtifacts.createSpeedyShoes();

          speedyShoes.add(new Equipped((Player) inventoryHolder));

          EntityData data = entityToEntityMapper
              .getEntity((org.bukkit.entity.Entity) inventoryHolder);

          switch (inventoryClickEvent.getAction()) {
            case PLACE_ONE:
            case PLACE_ALL:
            case PLACE_SOME:
            case SWAP_WITH_CURSOR:
              data.getEquipment().getFeet().ifPresent(engine::removeEntity);

              data.getEquipment().setFeet(speedyShoes);
              engine.addEntity(speedyShoes);
              break;
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_SOME:
            case PICKUP_ONE:
              data.getEquipment().getFeet().ifPresent(engine::removeEntity);
              data.getEquipment().setFeet(null);
              break;
          }
        }
      }
    }
  }
}
