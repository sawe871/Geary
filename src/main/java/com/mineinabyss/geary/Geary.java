package com.mineinabyss.geary;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.mineinabyss.geary.core.ActionListener;
import com.mineinabyss.geary.core.ItemUtil;
import com.mineinabyss.geary.core.ItemUtil.EntityInitializer;
import com.mineinabyss.geary.core.ProjectileToEntityMapper;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import com.mineinabyss.geary.ecs.systems.DegredationSystem;
import com.mineinabyss.geary.ecs.systems.EntityRemovalSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileCollisionSystem;
import com.mineinabyss.geary.ecs.systems.ProjectileLaunchingSubSystem;
import com.mineinabyss.geary.ecs.systems.movement.EntityPullingSystem;
import com.mineinabyss.geary.ecs.systems.rendering.ItemDisplaySystem;
import com.mineinabyss.geary.ecs.systems.rendering.RopeDisplaySystem;
import com.mineinabyss.geary.ecs.systems.tools.GrapplingHookDisconnectingSystem;
import com.mineinabyss.geary.ecs.systems.tools.GrapplingHookExtendingSystem;
import com.mineinabyss.geary.state.PersistentEntityReader;
import com.mineinabyss.geary.state.PersistentEntityWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class Geary extends JavaPlugin implements GearyService {

  private ItemUtil itemUtil;
  private EntityToUUIDMapper mapper;
  private Engine engine;

  @Override
  public void onEnable() {
    // Plugin startup logic
    mapper = new EntityToUUIDMapper();
    engine = new Engine();
    ProjectileToEntityMapper projectileToEntityMapper = new ProjectileToEntityMapper();
    ProjectileLaunchingSubSystem pslss = new ProjectileLaunchingSubSystem(projectileToEntityMapper,
        mapper);
    itemUtil = new ItemUtil(this, mapper, engine);

    engine.addEntityListener(mapper);

    engine.addSystem(new GrapplingHookExtendingSystem(pslss, mapper));
    engine.addSystem(new GrapplingHookDisconnectingSystem(projectileToEntityMapper, mapper));
    engine.addSystem(new ProjectileCollisionSystem());
    engine.addSystem(new EntityPullingSystem());
    engine.addSystem(new RopeDisplaySystem());
    engine.addSystem(new DegredationSystem());
    engine.addSystem(new ItemDisplaySystem(itemUtil));
    engine.addSystem(new EntityRemovalSystem());

    try {
      new PersistentEntityReader(mapper, engine).loadEntityMapper(new FileReader(getConfigFile()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

//    ItemGiverBro itemGiverBro = new ItemGiverBro(itemUtil);
//    getCommand("gib").setExecutor(itemGiverBro);

    getServer().getPluginManager()
        .registerEvents(new ActionListener(projectileToEntityMapper, itemUtil), this);
    getServer().getScheduler().scheduleSyncRepeatingTask(this, this::doEngineUpdates, 0, 1);
    getServer().getServicesManager()
        .register(GearyService.class, this, this, ServicePriority.Highest);
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

    String json = new PersistentEntityWriter(mapper).saveEntityMapper();

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

  private void doEngineUpdates() {

    getServer().getOnlinePlayers().forEach(player -> {
      ItemStack itemStack = player.getInventory().getItemInMainHand();

      itemUtil.removeOrGet(itemStack, player.getInventory())
          .ifPresent(entity -> entity.add(new Equipped(player)));
    });

    engine.update(1);
  }

  @Override
  public ShapedRecipe createRecipe(NamespacedKey key, EntityInitializer entityInitializer,
      ItemStack itemStack) {
    return new GearyRecipe(key, itemStack, this, entityInitializer);
  }

  @Override
  public void attachToItemStack(EntityInitializer entityInitializer, ItemStack itemStack) {
    itemUtil.attachToItemStack(entityInitializer, itemStack);
  }

  @Override
  public void addSystem(EntitySystem entitySystem) {
    engine.addSystem(entitySystem);
  }
}
