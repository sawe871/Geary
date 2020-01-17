package com.mineinabyss.geary;

import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableSet;
import com.mineinabyss.geary.ecs.components.Activated;
import com.mineinabyss.geary.ecs.components.ClimbingRopeCreator;
import com.mineinabyss.geary.ecs.components.Equippable;
import com.mineinabyss.geary.ecs.components.Explosion;
import com.mineinabyss.geary.ecs.components.Rope;
import com.mineinabyss.geary.ecs.components.Speed;
import com.mineinabyss.geary.ecs.components.ProjectileLauncher;
import com.mineinabyss.geary.ecs.components.Yanker;
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

  public static Entity createGrapplingHook() {
    return new Entity()
        .add(new ProjectileLauncher(1,
            () -> ImmutableSet.of(new Yanker(), new Activated()),
            () -> ImmutableSet.of(new Rope(), new AnchorToActor(),
                new AnchorToProjectile())))
        .add(new Equippable(EquipmentSlot.HAND));
  }
}
