package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineinabyss.geary.ecs.EntityMapper;
import com.mineinabyss.geary.ecs.EntityRef;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHook;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHookExtended;
import java.util.UUID;
import org.bukkit.Color;
import org.junit.Ignore;
import org.junit.Test;

public class SerializationUtilTest {

  @Ignore
  @Test
  public void name() {
    Entity entity = new Entity();
    Entity referencedEntity = new Entity();
    entity.add(new GrapplingHookExtended(EntityRef.create(UUID.randomUUID())));
    entity.add(new GrapplingHook(1, 1, 1, Color.fromRGB(1, 1, 1), 2));
    entity.add(new Activated());
    entity.add(new Projectile(UUID.randomUUID(), ImmutableSet::of));

    EntityMapper entityMapper = new EntityMapper();
    entityMapper.entityAdded(entity);
    entityMapper.entityAdded(referencedEntity);

    SerializationUtil serializationUtil = new SerializationUtil(entityMapper);

    System.out.println(serializationUtil.serialize(entity));

    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    System.out.println(gson.toJson(entityMapper));
  }
}