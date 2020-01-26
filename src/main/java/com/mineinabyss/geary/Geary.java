package com.mineinabyss.geary;

import com.badlogic.ashley.core.Engine;
import com.mineinabyss.geary.core.ActionListener;
import com.mineinabyss.geary.core.EntityToEntityMapper;
import com.mineinabyss.geary.core.ItemToEntityMapper;
import com.mineinabyss.geary.core.ItemUtil;
import com.mineinabyss.geary.core.ProjectileMapper;
import com.mineinabyss.geary.core.command.ItemGiverBro;
import com.mineinabyss.geary.ecs.systems.ProjectileCollisionSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileLaunchingSubSystem;
import com.mineinabyss.geary.ecs.systems.movement.EntityPullingSystem;
import com.mineinabyss.geary.ecs.systems.rendering.ItemDisplaySystem;
import com.mineinabyss.geary.ecs.systems.rendering.RopeDisplaySystem;
import com.mineinabyss.geary.ecs.systems.tools.GrapplingHookDisconnectingSystem;
import com.mineinabyss.geary.ecs.systems.tools.GrapplingHookExtendingSystem;
import org.bukkit.plugin.java.JavaPlugin;

public final class Geary extends JavaPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic
    Engine engine = new Engine();

    ProjectileMapper projectileMapper = new ProjectileMapper();
    ProjectileLaunchingSubSystem pslss = new ProjectileLaunchingSubSystem(projectileMapper);
    EntityToEntityMapper entityToEntityMapper = new EntityToEntityMapper(engine);
    ItemToEntityMapper itemToEntityMapper = new ItemToEntityMapper(engine);
    ItemUtil itemUtil = new ItemUtil(this, itemToEntityMapper);

    ItemGiverBro itemGiverBro = new ItemGiverBro(itemUtil);

    engine.addSystem(new GrapplingHookExtendingSystem(pslss));
    engine.addSystem(new GrapplingHookDisconnectingSystem(projectileMapper));
    engine.addSystem(new ProjectileCollisionSystem());
    engine.addSystem(new EntityPullingSystem());
    engine.addSystem(new RopeDisplaySystem());
    engine.addSystem(new ItemDisplaySystem());

    getCommand("gib").setExecutor(itemGiverBro);

    getServer().getPluginManager()
        .registerEvents(
            new ActionListener(entityToEntityMapper, projectileMapper, itemUtil, engine), this);
    getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> engine.update(1), 0, 1);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
