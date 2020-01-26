package com.mineinabyss.geary.core.command;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Doubles;
import com.mineinabyss.geary.PredefinedArtifacts;
import com.mineinabyss.geary.core.ItemUtil;
import com.mineinabyss.geary.core.ItemUtil.EntityInitializer;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGiverBro implements TabExecutor {

  private final ItemUtil itemUtil;

  public ItemGiverBro(ItemUtil itemUtil) {
    this.itemUtil = itemUtil;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (command.getName().equals("gib") && commandSender instanceof Player) {
      Double speedo = .7;
      if (strings.length == 1) {
        speedo = Doubles.tryParse(strings[0]);
        speedo = speedo == null ? .7 : speedo;
      }
      double finalSpeedo = speedo;
      EntityInitializer entityInitializer;
      if (speedo < 1.5) {
        entityInitializer = () -> PredefinedArtifacts
            .createGrapplingHook(finalSpeedo, 3, 4, Color.fromRGB(142, 89, 60), 1);
      } else {
        entityInitializer = () -> PredefinedArtifacts
            .createGrapplingHook(finalSpeedo, 5, 5, Color.fromRGB(202, 204, 206), 2);
      }
      ItemStack itemStack = itemUtil
          .createItemWithEcsEntity(entityInitializer,
              Material.DIAMOND_SHOVEL);

      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setDisplayName(String.format("Grappling Hook: %.1f", finalSpeedo));
      itemStack.setItemMeta(itemMeta);

      ((Player) commandSender).getInventory().addItem(itemStack);
    }

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s,
      String[] strings) {
    return ImmutableList.of();
  }
}
