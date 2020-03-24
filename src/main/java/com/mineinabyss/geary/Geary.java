package com.mineinabyss.geary;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
import com.mineinabyss.geary.state.RecipeReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class Geary extends JavaPlugin implements GearyService {

  private ItemUtil itemUtil;
  private EntityToUUIDMapper mapper;
  private Engine engine;
  private static final String PERSISTENCE_FILE_NAME = "data.json";
  private static final String RECIPES_FILE_NAME = "recipes.json";
  private static final String BACKUP_SUFFIX = ".backup";

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
      new PersistentEntityReader(mapper, engine)
          .loadEntityMapper(new FileReader(getConfigFileByName(PERSISTENCE_FILE_NAME)));
    } catch (FileNotFoundException e) {
      getLogger().info(String
          .format("Missing Persistence File, %s, no entities loaded.", PERSISTENCE_FILE_NAME));
    }

    try {
      Reader reader = new FileReader(getConfigFileByName(RECIPES_FILE_NAME));
      new RecipeReader(this).readRecipes(reader).forEach(Bukkit::addRecipe);
    } catch (FileNotFoundException e) {
      getLogger().info(String
          .format("Missing Recipes File, %s, no entities loaded.", RECIPES_FILE_NAME));
    }

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
    File data = getConfigFileByName(PERSISTENCE_FILE_NAME);

    Files.copy(data.toPath(),
        getConfigFileByName(PERSISTENCE_FILE_NAME + BACKUP_SUFFIX).toPath(),
        REPLACE_EXISTING);

    String json = new PersistentEntityWriter(mapper).saveEntityMapper();

    FileWriter writer = new FileWriter(data);
    writer.write(json);
    writer.close();
  }

  private File getConfigFileByName(String filename) {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
    }
    return new File(getDataFolder(), filename);
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
