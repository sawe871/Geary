package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableSet;
import com.mineinabyss.geary.core.ProjectileToEntityMapper;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import com.mineinabyss.geary.ecs.components.Actor;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.Projectile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ProjectileLaunchingSubSystem {

  private final ProjectileToEntityMapper projectileToEntityMapper;
  private final EntityToUUIDMapper entityToUUIDMapper;

  public ProjectileLaunchingSubSystem(ProjectileToEntityMapper projectileToEntityMapper,
      EntityToUUIDMapper entityToUUIDMapper) {
    this.projectileToEntityMapper = projectileToEntityMapper;
    this.entityToUUIDMapper = entityToUUIDMapper;
  }

  // TODO player should be removed
  public Entity launchProjectile(double speed, int projModel, Player player) {
    org.bukkit.entity.Projectile projectile = player.launchProjectile(Snowball.class,
        player.getEyeLocation().getDirection().normalize()
            .multiply(speed));

    net.minecraft.server.v1_15_R1.EntitySnowball entitysnowball = ((CraftSnowball) projectile)
        .getHandle();

    ItemStack itemStack = new ItemStack(Material.SNOWBALL);
    ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.SNOWBALL);
    itemMeta.setCustomModelData(projModel);
    itemStack.setItemMeta(itemMeta);

    entitysnowball.setItem(CraftItemStack.asNMSCopy(itemStack));

    Entity projectileEntity = new Entity();

    projectileEntity.add(new Position(projectile.getLocation()));

    projectileEntity.add(new Actor(player));

    projectileEntity.add(new Projectile(projectile, ImmutableSet::of));

    projectileToEntityMapper.addProjectile(projectile, projectileEntity);

    entityToUUIDMapper.entityAdded(projectileEntity);

    return projectileEntity;
  }
}
