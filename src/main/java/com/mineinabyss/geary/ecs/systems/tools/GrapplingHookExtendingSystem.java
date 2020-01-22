package com.mineinabyss.geary.ecs.systems.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.common.collect.ImmutableSet;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.Rope;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.effect.PullToLocation;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHookExtended;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import com.mineinabyss.geary.ecs.systems.ProjectileLaunchingSubSystem;
import org.bukkit.entity.Player;

public class GrapplingHookExtendingSystem extends IteratingSystem {

  private final ProjectileLaunchingSubSystem projectileLaunchingSubSystem;
  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);
  private ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper
      .getFor(Projectile.class);

  private ComponentMapper<DisplayState> displayStateComponentMapper = ComponentMapper
      .getFor(DisplayState.class);

  public GrapplingHookExtendingSystem(ProjectileLaunchingSubSystem projectileLaunchingSubSystem) {
    super(Family.all(GrapplingHook.class, Activated.class, Equipped.class).exclude(
        GrapplingHookExtended.class).get());
    this.projectileLaunchingSubSystem = projectileLaunchingSubSystem;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Player owner = equippedComponentMapper.get(entity).getOwner();
    Entity launchedProjectile = projectileLaunchingSubSystem.launchProjectile(.7, owner);
    getEngine().addEntity(launchedProjectile);
    Projectile projectile = projectileComponentMapper.get(launchedProjectile);
    org.bukkit.entity.Projectile mcProjectile = projectile
        .getProjectile();
    launchedProjectile.add(new Rope(() -> owner.getEyeLocation().add(0, -.5, 0),
        mcProjectile::getLocation));
    projectile.setCollisionComponents(() -> ImmutableSet.of(new PullToLocation()));

    if (displayStateComponentMapper.has(entity)) {
      displayStateComponentMapper.get(entity).setModelNo(4);
    }
    entity.add(new GrapplingHookExtended(launchedProjectile));
    entity.remove(Activated.class);
  }
}
