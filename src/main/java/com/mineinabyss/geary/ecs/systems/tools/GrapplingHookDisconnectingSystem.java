package com.mineinabyss.geary.ecs.systems.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.core.ProjectileToEntityMapper;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.effect.PullToLocation;
import com.mineinabyss.geary.ecs.components.equipment.Degrading;
import com.mineinabyss.geary.ecs.components.equipment.Durability;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHookExtended;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;

/**
 * Disconnects grappling hooks from the ground.
 */
public class GrapplingHookDisconnectingSystem extends IteratingSystem {

  private final ProjectileToEntityMapper projectileToEntityMapper;
  private final EntityToUUIDMapper entityToUUIDMapper;
  private ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper
      .getFor(Projectile.class);
  private ComponentMapper<DisplayState> displayStateComponentMapper = ComponentMapper
      .getFor(DisplayState.class);
  private ComponentMapper<GrapplingHookExtended> extendedMapper = ComponentMapper
      .getFor(GrapplingHookExtended.class);
  private ComponentMapper<Activated> activatedComponentMapper = ComponentMapper
      .getFor(Activated.class);
  private ComponentMapper<PullToLocation> pullToLocationComponentMapper = ComponentMapper
      .getFor(PullToLocation.class);
  private ComponentMapper<GrapplingHook> grapplingHookMapper = ComponentMapper
      .getFor(GrapplingHook.class);
  private ComponentMapper<Durability> durabilityComponentMapper = ComponentMapper
      .getFor(Durability.class);

  public GrapplingHookDisconnectingSystem(ProjectileToEntityMapper projectileToEntityMapper,
      EntityToUUIDMapper entityToUUIDMapper) {
    super(Family.all(GrapplingHook.class, GrapplingHookExtended.class).get());
    this.projectileToEntityMapper = projectileToEntityMapper;
    this.entityToUUIDMapper = entityToUUIDMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    GrapplingHookExtended grapplingHookExtended = extendedMapper.get(entity);
    Entity projectile = entityToUUIDMapper.getEntity(grapplingHookExtended.getExtendedEntity());

    if (shouldRemove(entity, projectile)) {
      GrapplingHook grapplingHook = grapplingHookMapper.get(entity);

      if (projectileComponentMapper.has(projectile)
          && projectileComponentMapper.get(projectile).getProjectile() != null) {
        org.bukkit.entity.Projectile mcProj = projectileComponentMapper.get(projectile)
            .getProjectile();
        projectileToEntityMapper.removeProjectile(mcProj);
        mcProj.remove();
      } else {
        // Since there was not an associated projectile, the hook made contact
        if (durabilityComponentMapper.has(entity)) {
          entity.add(new Degrading());
        }
      }

      getEngine().removeEntity(projectile);

      if (displayStateComponentMapper.has(entity)) {
        displayStateComponentMapper.get(entity).setModelNo(grapplingHook.getStaticModel());
      }

      entity.remove(GrapplingHookExtended.class);
      entity.remove(Activated.class);
    }
  }

  private boolean shouldRemove(Entity entity, Entity extendedEntity) {
    return activatedComponentMapper.has(entity) || isFinishedPulling(extendedEntity);
  }

  private boolean isFinishedPulling(Entity extendedEntity) {
    return !pullToLocationComponentMapper
        .has(extendedEntity) && !projectileComponentMapper.has(extendedEntity);
  }
}
