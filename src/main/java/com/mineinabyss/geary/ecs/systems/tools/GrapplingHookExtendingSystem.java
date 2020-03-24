package com.mineinabyss.geary.ecs.systems.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.common.collect.ImmutableSet;
import com.mineinabyss.geary.ecs.EntityRef;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.Rope;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.effect.PullToLocation;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHookExtended;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import com.mineinabyss.geary.ecs.systems.ProjectileLaunchingSubSystem;
import java.util.Optional;
import org.bukkit.entity.Player;

/**
 * Extends grappling hooks.
 */
public class GrapplingHookExtendingSystem extends IteratingSystem {

  private final ProjectileLaunchingSubSystem projectileLaunchingSubSystem;
  private final EntityToUUIDMapper entityToUUIDMapper;
  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);
  private ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper
      .getFor(Projectile.class);

  private ComponentMapper<DisplayState> displayStateComponentMapper = ComponentMapper
      .getFor(DisplayState.class);

  private ComponentMapper<GrapplingHook> grapplingHookComponentMapper = ComponentMapper
      .getFor(GrapplingHook.class);

  public GrapplingHookExtendingSystem(ProjectileLaunchingSubSystem projectileLaunchingSubSystem,
      EntityToUUIDMapper entityToUUIDMapper) {
    super(Family.all(GrapplingHook.class, Activated.class, Equipped.class).exclude(
        GrapplingHookExtended.class).get());
    this.projectileLaunchingSubSystem = projectileLaunchingSubSystem;
    this.entityToUUIDMapper = entityToUUIDMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Optional<Player> optionalPlayer = Optional
        .ofNullable(equippedComponentMapper.get(entity).getOwner());

    if (optionalPlayer.isPresent()) {
      Player owner = optionalPlayer.get();
      GrapplingHook grapplingHook = grapplingHookComponentMapper.get(entity);
      Entity launchedProjectile = projectileLaunchingSubSystem
          .launchProjectile(grapplingHook.getFiringSpeed(), grapplingHook.getHookModel(), owner);
      getEngine().addEntity(launchedProjectile);
      Projectile projectile = projectileComponentMapper.get(launchedProjectile);
      org.bukkit.entity.Projectile mcProjectile = projectile
          .getProjectile();
      launchedProjectile.add(new Rope(() -> owner.getEyeLocation().add(0, -.5, 0),
          mcProjectile::getLocation, grapplingHook.getColor()));
      projectile.setCollisionComponents(() -> ImmutableSet.of(new PullToLocation()));

      if (displayStateComponentMapper.has(entity)) {
        displayStateComponentMapper.get(entity).setModelNo(grapplingHook.getExtendedModel());
      }

      entity.add(
          new GrapplingHookExtended(
              EntityRef.create(entityToUUIDMapper.getId(launchedProjectile))));
    }
    entity.remove(Activated.class);
  }
}
