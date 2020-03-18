package com.mineinabyss.geary.ecs;

import com.badlogic.ashley.core.Component;
import java.io.Serializable;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Supplier that supplies sets of components to be used in entities. When a {@link
 * com.badlogic.ashley.core.EntitySystem} attaches new components to an entity, this supplier should
 * be used if the system expects fresh components each time.
 */
public interface ComponentSupplier extends Serializable, Supplier<Set<Component>> {

}
