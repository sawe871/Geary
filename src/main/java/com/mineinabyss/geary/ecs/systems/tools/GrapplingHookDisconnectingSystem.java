package com.mineinabyss.geary.ecs.systems.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.core.ProjectileMapper;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.effect.PullToLocation;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHookExtended;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;

public class GrapplingHookDisconnectingSystem extends IteratingSystem {

  private final ProjectileMapper projectileMapper;
  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);
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

  public GrapplingHookDisconnectingSystem(ProjectileMapper projectileMapper) {
    super(Family.all(GrapplingHook.class, Equipped.class,
        GrapplingHookExtended.class).get());
    this.projectileMapper = projectileMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    if (shouldRemove(entity)) {
      GrapplingHookExtended grapplingHookExtended = extendedMapper.get(entity);

      Entity projectile = grapplingHookExtended.getExtendedEntity();

      if (projectileComponentMapper.has(projectile)) {
        org.bukkit.entity.Projectile mcProj = projectileComponentMapper.get(projectile)
            .getProjectile();
        projectileMapper.removeProjectile(mcProj);
        mcProj.remove();
      }

      getEngine().removeEntity(grapplingHookExtended.getExtendedEntity());

      if (displayStateComponentMapper.has(entity)) {
        displayStateComponentMapper.get(entity).setModelNo(3);
      }
      entity.remove(GrapplingHookExtended.class);
      entity.remove(Activated.class);
    }
  }

  private boolean shouldRemove(Entity entity) {
    Entity extendedEntity = extendedMapper.get(entity).getExtendedEntity();
    return activatedComponentMapper.has(entity) || (!pullToLocationComponentMapper
        .has(extendedEntity) && !projectileComponentMapper.has(extendedEntity));
  }
}
