package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Entity;
import com.mineinabyss.geary.ecs.EntityMapper;
import com.mineinabyss.geary.ecs.components.control.Activated;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import org.junit.Test;

public class EntityMapperSaverTest {

  @Test
  public void name() {
    EntityMapper entityMapper = new EntityMapper();
    Entity entity = new Entity();
    entity.add(new Activated());
    entity.add(new DisplayState(1));
    entityMapper.entityAdded(entity);
    System.out.println(new EntityMapperSaver(entityMapper).saveEntityMapper());
  }
}