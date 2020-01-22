package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.mineinabyss.geary.core.ProjectileMapper;
import com.mineinabyss.geary.ecs.components.Actor;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.ProjectileLauncher;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ThrowingSystem extends ActingSystem {

  private final ProjectileMapper projectileMapper;
  private ComponentMapper<ProjectileLauncher> thrownComponentMapper = ComponentMapper.getFor(
      ProjectileLauncher.class);

  public ThrowingSystem(ProjectileMapper projectileMapper) {
    super(ImmutableList.of(ProjectileLauncher.class, Equipped.class));
    this.projectileMapper = projectileMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Optional<Player> actor = getActor(entity);

    actor.ifPresent(player -> throwProj(player, entity));
    entity.remove(Activated.class);
  }

  private void throwProj(Player actor, Entity entity) {
    ProjectileLauncher projectileLauncher = thrownComponentMapper.get(entity);
    org.bukkit.entity.Projectile projectile = actor.launchProjectile(Snowball.class,
        actor.getEyeLocation().getDirection().normalize()
            .multiply(projectileLauncher.getSpeed()));

    net.minecraft.server.v1_15_R1.EntitySnowball entitysnowball = ((CraftSnowball) projectile)
        .getHandle();

    ItemStack itemStack = new ItemStack(Material.SNOWBALL);
    ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.SNOWBALL);
    itemMeta.setCustomModelData(1);
    itemStack.setItemMeta(itemMeta);

    entitysnowball.setItem(CraftItemStack.asNMSCopy(itemStack));

    Entity projectileEntity = new Entity();

    projectileEntity.add(new Projectile(projectile, projectileLauncher.getCollisionComponents()));
    projectileEntity.add(new Position(projectile.getLocation()));

    getActor(entity).ifPresent(player -> projectileEntity.add(new Actor(player)));

    projectileLauncher.getLaunchComponents().get().forEach(projectileEntity::add);

    projectileMapper.addProjectile(projectile, projectileEntity);
    getEngine().addEntity(projectileEntity);
  }
}
