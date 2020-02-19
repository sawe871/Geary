package com.mineinabyss.geary.core;

import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.core.ItemUtil.ItemDegradedException;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.ProjectileHitGround;
import com.mineinabyss.geary.ecs.components.Remove;
import com.mineinabyss.geary.ecs.components.control.Activated;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        Optional<Entity> mainHand = itemUtil.removeOrGet(event.getItem(),
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
  public void onItemDamaged(EntityDamageEvent event) {
    removeEntityIfNeeded(event);
  }


  private void removeEntityIfNeeded(EntityEvent event) {
    if (event.getEntity() instanceof Item && event.getEntity().isDead()) {
      ItemStack itemStack = ((Item) event.getEntity()).getItemStack();

      try {
        itemUtil
            .getEcsEntityFromItem(itemStack).ifPresent(entity -> entity.add(new Remove()));
      } catch (ItemDegradedException e) {
        // No special handling, item is dying anyway.
      }
    }
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
}
