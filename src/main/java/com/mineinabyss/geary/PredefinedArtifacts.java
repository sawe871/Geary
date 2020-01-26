package com.mineinabyss.geary;

import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.ecs.components.ClimbingRopeCreator;
import com.mineinabyss.geary.ecs.components.Speed;
import com.mineinabyss.geary.ecs.components.effect.Explosion;
import com.mineinabyss.geary.ecs.components.equipment.Equippable;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlot;

public class PredefinedArtifacts {

  public static Entity createSpeedyShoes() {
    return new Entity().add(new Equippable(EquipmentSlot.FEET)).add(new Speed(4f));
  }

  public static Entity createExploder() {
    return new Entity().add(new Equippable(EquipmentSlot.HAND)).add(new Explosion());
  }

  public static Entity createClimbingRope() {
    return new Entity().add(new ClimbingRopeCreator(10)).add(new Equippable(EquipmentSlot.HAND));
  }

  public static Entity createGrapplingHook(double speedo, int staticModel, int firingModel,
      Color color, int hookModel) {
    return new Entity()
        .add(new GrapplingHook(speedo, staticModel, firingModel, color, hookModel))
        .add(new DisplayState(staticModel))
        .add(new Equippable(EquipmentSlot.HAND));
  }
}
