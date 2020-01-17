package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.common.collect.ImmutableList;
import com.mineinabyss.geary.ecs.components.Activated;
import com.mineinabyss.geary.ecs.components.Equipped;
import java.util.Optional;
import org.bukkit.entity.Player;

public abstract class ActingSystem extends IteratingSystem {

  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);

  public ActingSystem(ImmutableList<Class<? extends Component>> all) {
    super(Family.all(
        ImmutableList.builder().add(Activated.class).addAll(all).build()
            .stream().toArray(Class[]::new)).get(), 5);
  }

  protected Optional<Player> getActor(com.badlogic.ashley.core.Entity entity) {
    return Optional.ofNullable(equippedComponentMapper.get(entity).getOwner());
  }
}
