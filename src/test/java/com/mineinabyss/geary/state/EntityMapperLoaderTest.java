package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.ecs.EntityMapper;
import com.mineinabyss.geary.ecs.EntityRef;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.grappling.GrapplingHookExtended;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import java.io.StringReader;
import java.util.UUID;
import org.junit.Test;

public class EntityMapperLoaderTest {

  @Test
  public void name() {
    EntityMapper entityMapper = new EntityMapper();
    Entity entity = new Entity();
    entity.add(new Activated());
    entity.add(new DisplayState(1));
    entity.add(new GrapplingHookExtended(EntityRef.create(UUID.randomUUID())));
    entityMapper.entityAdded(entity);
    String serialized = new EntityMapperSaver(entityMapper).saveEntityMapper();

    EntityMapper entityMapper1 = new EntityMapper();
    new EntityMapperLoader(entityMapper1, new Engine())
        .loadEntityMapper(new StringReader(serialized));
  }
}