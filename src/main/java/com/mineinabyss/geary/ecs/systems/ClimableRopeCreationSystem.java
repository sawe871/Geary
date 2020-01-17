package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.mineinabyss.geary.ClimbableBlockChecker;
import com.mineinabyss.geary.ecs.components.Activated;
import com.mineinabyss.geary.ecs.components.Rope;
import com.mineinabyss.geary.ecs.components.ClimbingRopeCreator;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class ClimableRopeCreationSystem extends ActingSystem {

  private final ClimbableBlockChecker climbableBlockChecker;
  private ComponentMapper<ClimbingRopeCreator> ropeCreatorComponentMapper = ComponentMapper
      .getFor(ClimbingRopeCreator.class);

  public ClimableRopeCreationSystem(ClimbableBlockChecker climbableBlockChecker) {
    super(ImmutableList.of(ClimbingRopeCreator.class));
    this.climbableBlockChecker = climbableBlockChecker;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    ClimbingRopeCreator climbingRopeCreator = ropeCreatorComponentMapper.get(entity);

    getActor(entity).ifPresent(actor -> doit(climbingRopeCreator, actor));

    entity.remove(Activated.class);
  }

  private void doit(ClimbingRopeCreator climbingRopeCreator, Player actor) {
    double xOffset = actor.getEyeLocation().getDirection().getX();
    double zOffset = actor.getEyeLocation().getDirection().getZ();

    Block endBlock = actor.getTargetBlockExact(20);

    if (endBlock != null) {
      Block blockRopeFacing;
      if (xOffset * xOffset > zOffset * zOffset) {
        blockRopeFacing = endBlock.getLocation().add(new Vector(Math.copySign(1, xOffset), 0, 0))
            .getBlock();
      } else {
        blockRopeFacing = endBlock.getLocation().add(new Vector(0, 0, Math.copySign(1, zOffset)))
            .getBlock();
      }

      Location end = blockRopeFacing.getLocation();
      BlockIterator blockIterator = new BlockIterator(end.getWorld(),
          end.toVector().add(new Vector(.5, .5, .5)),
          new Vector(0, -1, 0), 0,
          climbingRopeCreator.getMaxLength() + 1);

      Set<Block> climbableBlocks = new HashSet<>();

      Block next = endBlock;
      Block prev = endBlock;
      while (blockIterator.hasNext()) {
        prev = next;
        next = blockIterator.next();

        if (!next.isPassable()) {
          break;
        }
        climbableBlocks.add(next);
      }

      BlockFace blockFace = endBlock.getFace(blockRopeFacing);

      Vector centering = new Vector(.5, .5, .5).subtract(blockFace.getDirection().multiply(.5));
      getEngine().addEntity(new Entity().add(
          new Rope(prev.getLocation().add(centering),
              end.add(centering))));

      climbableBlockChecker.addClimbableBlocks(climbableBlocks);
    }
  }
}
