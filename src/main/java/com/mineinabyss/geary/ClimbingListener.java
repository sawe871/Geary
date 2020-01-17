package com.mineinabyss.geary;

import java.util.Optional;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClimbingListener implements Listener {

  ClimbableBlockChecker climbableBlockChecker;

  public ClimbingListener(ClimbableBlockChecker climbableBlockChecker) {
    this.climbableBlockChecker = climbableBlockChecker;
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {

    if (playerMoveEvent.getPlayer().getGameMode() == GameMode.SURVIVAL) {
      Optional.ofNullable(playerMoveEvent.getTo()).map(climbableBlockChecker::checkClimbable)
          .ifPresent(canClimb -> {
            if (canClimb) {
              playerMoveEvent.getPlayer().setAllowFlight(true);
              playerMoveEvent.getPlayer().setFlying(true);
            } else {
              playerMoveEvent.getPlayer().setAllowFlight(false);
              playerMoveEvent.getPlayer().setFlying(false);
            }
          });
    }
  }
}
