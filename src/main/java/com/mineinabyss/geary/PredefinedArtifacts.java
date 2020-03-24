package com.mineinabyss.geary;

import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.ecs.components.equipment.Durability;
import com.mineinabyss.geary.ecs.components.equipment.Equippable;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import org.bukkit.Color;

/**
 * TODO(https://github.com/MineInAbyss/geary/issues/11): Move to configuration
 */
public class PredefinedArtifacts {

  public static Entity createGrapplingHook(double speedo, int staticModel, int firingModel,
      Color color, int hookModel, int maxUses) {
    return new Entity()
        .add(new GrapplingHook(speedo, staticModel, firingModel, color, hookModel))
        .add(new DisplayState(staticModel))
        .add(new Durability(maxUses))
        .add(new Equippable());
  }
}
