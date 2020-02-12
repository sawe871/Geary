package com.mineinabyss.geary;

import com.badlogic.ashley.core.Component;
import java.io.Serializable;
import java.util.Set;
import java.util.function.Supplier;

public interface ComponentSupplier extends Serializable, Supplier<Set<Component>> {

}
