package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Component;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.reflections.Reflections;

@RunWith(Parameterized.class)
public class ComponentSerializabilityTest {

  @Parameter
  public Class<? extends Component> componentClass;

  @Parameters
  public static Collection<Class<? extends Component>> testSerializability() {
    Reflections reflections = new Reflections("com.mineinabyss.geary.ecs.components");
    return reflections.getSubTypesOf(Component.class);
  }

  @Ignore
  @Test
  public void checkSerializability() {
    Field[] fields = componentClass.getDeclaredFields();

    for (Field field : fields) {
      if (!field.getType().isPrimitive() && !field.getType().isEnum()) {

        if (!Serializable.class.isAssignableFrom(field.getType())
            && !ConfigurationSerializable.class.isAssignableFrom(field.getType())) {
          throw new AssertionError(String
              .format("%s in %s is not serializable", field.getName(), componentClass.getName()));
        }
      }
    }
  }
}