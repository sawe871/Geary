package com.mineinabyss.geary.core;

import java.io.Serializable;
import org.bukkit.Location;

/**
 * Wraps a location.
 */
public interface LocationWrapper extends Serializable {

  Location getLocation();
}
