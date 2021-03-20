package me.braydon.anticheat.command;

import me.braydon.anticheat.check.Check;
import me.braydon.anticheat.check.CheckManager;
import me.braydon.anticheat.meta.MetadataManager;
import me.braydon.anticheat.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

/**
 * @author Braydon
 */
public class AnticheatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("anticheat.command")) {
            sender.sendMessage("§cNo permission.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("§c/" + label + " saveMetadata <player>");
            sender.sendMessage("§c/" + label + " debug <player> <check>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null)
            sender.sendMessage("§cPlayer is offline.");
        else {
            switch (args[0].toLowerCase()) {
                case "savemeta":
                case "savemetadata": {
                    if (sender instanceof Player)
                        sender.sendMessage("§cOnly the terminal can save metadata.");
                    else {
                        PlayerData targetPlayerData = PlayerData.get(target);
                        if (targetPlayerData == null) // This should never happen
                            return true;
                        sender.sendMessage("§7Metadata for §f" + target.getName() + "§7:");
                        sender.sendMessage(MetadataManager.getMetadataJson(targetPlayerData));
                    }
                    break;
                }
                case "debug": {
                    if (sender instanceof ConsoleCommandSender)
                        sender.sendMessage("§cThe terminal cannot debug.");
                    else {
                        PlayerData playerData = PlayerData.get((Player) sender);
                        if (playerData == null) // This should never happen
                            return true;
                        if (playerData.isDebugging()) {
                            playerData.stopDebugging();
                            sender.sendMessage("§cNo-longer debugging.");
                            return true;
                        }
                        if (args.length < 3)
                            sender.sendMessage("§cYou must provide a check to debug.");
                        else {
                            Class<? extends Check> checkClass = CheckManager.getCheckClass(args[2]);
                            if (checkClass == null) {
                                sender.sendMessage("§cInvalid check. Checks: §f" + CheckManager.CHECK_CLASSES.stream()
                                        .map(Class::getSimpleName)
                                        .collect(Collectors.joining("§7, §f")));
                                return true;
                            }
                            playerData.startDebugging(target.getUniqueId(), checkClass);
                            sender.sendMessage("§7Now debugging §f" + checkClass.getSimpleName() + " §7for §f" + target.getName() + "§7.");
                        }
                    }
                    break;
                }
            }
        }
        return true;
    }
}