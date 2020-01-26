package com.mineinabyss.geary.ecs.components.grappling;

import com.badlogic.ashley.core.Component;
import org.bukkit.Color;

public class GrapplingHook implements Component {

  private double firingSpeed;
  private int staticModel;
  private int extendedModel;
  private int hookModel;
  private Color color;

  public GrapplingHook(double firingSpeed, int staticModel, int extendedModel,
      Color color, int hookModel) {
    this.firingSpeed = firingSpeed;
    this.staticModel = staticModel;
    this.extendedModel = extendedModel;
    this.hookModel = hookModel;
    this.color = color;
  }

  public double getFiringSpeed() {
    return firingSpeed;
  }

  public int getStaticModel() {
    return staticModel;
  }

  public int getExtendedModel() {
    return extendedModel;
  }

  public Color getColor() {
    return color;
  }

  public int getHookModel() {
    return hookModel;
  }
}
