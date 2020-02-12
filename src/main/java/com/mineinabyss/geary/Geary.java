package com.mineinabyss.geary;

import com.badlogic.ashley.core.Engine;
import com.mineinabyss.geary.core.ActionListener;
import com.mineinabyss.geary.core.ItemUtil;
import com.mineinabyss.geary.core.ItemUtil.EntityInitializer;
import com.mineinabyss.geary.core.ProjectileMapper;
import com.mineinabyss.geary.ecs.EntityMapper;
import com.mineinabyss.geary.ecs.systems.DegredationSystem;
import com.mineinabyss.geary.ecs.systems.EntityRemovalSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileCollisionSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileLaunchingSubSystem;
import com.mineinabyss.geary.ecs.systems.movement.EntityPullingSystem;
import com.mineinabyss.geary.ecs.systems.rendering.ItemDisplaySystem;
import com.mineinabyss.geary.ecs.systems.rendering.RopeDisplaySystem;
import com.mineinabyss.geary.ecs.systems.tools.GrapplingHookDisconnectingSystem;
import com.mineinabyss.geary.ecs.systems.tools.GrapplingHookExtendingSystem;
import com.mineinabyss.geary.state.EntityMapperLoader;
import com.mineinabyss.geary.state.EntityMapperSaver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Geary extends JavaPlugin {

  private ItemUtil itemUtil;
  private EntityMapper mapper;
  private Engine engine;

  @Override
  public void onEnable() {
    // Plugin startup logic
    mapper = new EntityMapper();
    engine = new Engine();
    ProjectileMapper projectileMapper = new ProjectileMapper();
    ProjectileLaunchingSubSystem pslss = new ProjectileLaunchingSubSystem(projectileMapper,
        mapper);
    itemUtil = new ItemUtil(this, mapper, engine);

    engine.addEntityListener(mapper);

    engine.addSystem(new GrapplingHookExtendingSystem(pslss, mapper));
    engine.addSystem(new GrapplingHookDisconnectingSystem(projectileMapper, mapper));
    engine.addSystem(new ProjectileCollisionSystem());
    engine.addSystem(new EntityPullingSystem());
    engine.addSystem(new RopeDisplaySystem());
    engine.addSystem(new DegredationSystem());
    engine.addSystem(new ItemDisplaySystem());
    engine.addSystem(new EntityRemovalSystem());

    try {
      new EntityMapperLoader(mapper, engine).loadEntityMapper(new FileReader(getConfigFile()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

//    ItemGiverBro itemGiverBro = new ItemGiverBro(itemUtil);
//    getCommand("gib").setExecutor(itemGiverBro);
    getServer().getPluginManager()
        .registerEvents(new ActionListener(projectileMapper, itemUtil), this);
    getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> engine.update(1), 0, 1);
  }

  public void attach(EntityInitializer entityInitializer, ItemStack itemStack) {
    itemUtil.attachToItemStack(entityInitializer, itemStack);
  }

  @Override
  public void onDisable() {
    try {
      dumpConfig();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void dumpConfig() throws IOException {
    File data = getConfigFile();

    String json = new EntityMapperSaver(mapper).saveEntityMapper();

    FileWriter writer = new FileWriter(data);
    writer.write(json);
    writer.close();
  }

  private File getConfigFile() {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
    }

    return new File(getDataFolder(), "data.json");
  }
}
