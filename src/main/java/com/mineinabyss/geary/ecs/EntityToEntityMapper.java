package com.mineinabyss.geary.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.google.auto.value.AutoValue;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.equipment.Equipment;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityToEntityMapper {

  private final Engine engine;
  Map<UUID, EntityData> uuidEntityMap;

  public EntityToEntityMapper(Engine engine) {
    this.engine = engine;
    uuidEntityMap = new HashMap<>();
  }

  public EntityData getEntity(org.bukkit.entity.Entity mcEntity) {
    return uuidEntityMap
        .computeIfAbsent(mcEntity.getUniqueId(), uuid -> createEntityForMcEntity(mcEntity));
  }

  /**
   * Creates and adds a new entity to the system based off of a minecraft entity.
   *
   * @param mcEntity the minecraft entity to add
   * @return the newly created ecs entity.
   */
  private EntityData createEntityForMcEntity(org.bukkit.entity.Entity mcEntity) {
    Entity entity = new Entity();
    Position position = new Position(mcEntity.getLocation());
    entity.add(position);
    Equipment equipment = new Equipment();
    entity.add(equipment);

    engine.addEntity(entity);
    return EntityData.create(equipment, position, entity);
  }

  @AutoValue
  public static abstract class EntityData {

    public abstract Equipment getEquipment();

    public abstract Position getPosition();

    public abstract Entity getEntity();

    public static EntityData create(Equipment newEquipment, Position newPosition,
        Entity newEntity) {
      return new AutoValue_EntityToEntityMapper_EntityData(newEquipment, newPosition, newEntity);
    }

  }
}
