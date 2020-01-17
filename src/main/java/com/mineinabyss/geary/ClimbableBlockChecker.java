package com.mineinabyss.geary;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class ClimbableBlockChecker {

  private Set<BlockVector> climbableBlocks;

  public ClimbableBlockChecker() {
    climbableBlocks = new HashSet<>();
  }

  public boolean checkClimbable(Location location) {
    return climbableBlocks.contains(location.toVector().toBlockVector());
  }

  public void addClimbableBlocks(Set<Block> blocks) {
    blocks.stream()
        .map(block -> block.getLocation().toVector().add(new Vector(.5, .5, .5)).toBlockVector())
        .forEach(climbableBlocks::add);
  }
}
