package com.mineinabyss.geary;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.mineinabyss.geary.ecs.systems.ClimableRopeCreationSystem;
import com.mineinabyss.geary.ecs.systems.EntityRemovalSystem;
import com.mineinabyss.geary.ecs.EntityToEntityMapper;
import com.mineinabyss.geary.ecs.systems.ExplosionSystem;
import com.mineinabyss.geary.ecs.systems.MovementEnhancingSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileCollisionSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileMovementSystem;
import com.mineinabyss.geary.ecs.systems.RopeAnchoringSystem;
import com.mineinabyss.geary.ecs.systems.RopeDisplaySystem;
import com.mineinabyss.geary.ecs.systems.ThrowingSystem;
import com.mineinabyss.geary.ecs.systems.YankingSystem;
import com.mineinabyss.geary.ecs.components.Equipped;
import com.mineinabyss.geary.ecs.components.Speed;
import org.bukkit.plugin.java.JavaPlugin;

public final class Geary extends JavaPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic
    Engine engine = new Engine();

    ClimbableBlockChecker climbableBlockChecker = new ClimbableBlockChecker();
    ProjectileMapper projectileMapper = new ProjectileMapper();

    engine.addSystem(new ThrowingSystem(projectileMapper));
    engine.addSystem(new ProjectileMovementSystem());
    engine.addSystem(new ProjectileCollisionSystem());
    engine.addSystem(new RopeAnchoringSystem());
    engine.addSystem(new YankingSystem());
    engine.addSystem(new ExplosionSystem());
    engine.addSystem(new RopeDisplaySystem());
    engine.addSystem(new ClimableRopeCreationSystem(climbableBlockChecker));
    MovementEnhancingSystem movementEnhancingSystem = new MovementEnhancingSystem();
    engine.addSystem(movementEnhancingSystem);
    engine.addSystem(new EntityRemovalSystem());

    engine
        .addEntityListener(Family.all(Equipped.class, Speed.class).get(), movementEnhancingSystem);

    EntityToEntityMapper entityToEntityMapper = new EntityToEntityMapper(engine);

    getServer().getPluginManager()
        .registerEvents(new ActionListener(entityToEntityMapper, projectileMapper, engine), this);
    getServer().getPluginManager()
        .registerEvents(new ClimbingListener(climbableBlockChecker), this);
    getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> engine.update(1), 0, 1);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
