package com.mineinabyss.geary.state;

import static com.google.common.truth.Truth.assertThat;

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

public class DeserialationUtilTest {

  @Ignore
  @Test
  public void testy() {
    Entity entity = new Entity();
    entity.add(new Activated());
    entity.add(new GrapplingHook(.1, 1, 1, Color.YELLOW, 3));
    entity.add(new GrapplingHookExtended(EntityRef.create(UUID.randomUUID())));
    entity.add(new Projectile(UUID.randomUUID(), ImmutableSet::of));

    EntityMapper entityMapper = new EntityMapper();
    entityMapper.entityAdded(entity);
    SerializationUtil serializationUtil = new SerializationUtil(entityMapper);

    String entityIdListenerJson = serializationUtil.serializeEntityMap(entityMapper);

    Gson gson = new GsonBuilder().create();
    String result = gson
        .toJson(serializationUtil.serialize(entity));
    DeserialationUtil deserialationUtil = new DeserialationUtil();

    EntityMapper deserilizedListner = deserialationUtil.deserializeIdMap(entityIdListenerJson);

    Entity entity1 = deserialationUtil.deserializeEntity(result);
    entityMapper.entityAdded(entity1);
    assertThat(result)
        .isEqualTo(gson.toJson(new SerializationUtil(deserilizedListner).serialize(entity1)));
  }
}