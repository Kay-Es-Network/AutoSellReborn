package it.aendrix.asreborn.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.aendrix.asreborn.files.Messages;
import it.aendrix.asreborn.main.Main;
import it.aendrix.asreborn.main.utils;

public class Commands implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("asa")) {
			if (!sender.hasPermission("*") && !sender.hasPermission("asa")) {
				Messages.noperm(sender);
				return true;
			}
			if (args.length == 0) {
				utils.sendMsg(sender, "&c/asa setmultiplier <player|global> <amount> [duration]");
				utils.sendMsg(sender, "&c/asa addmultiplier <player> <amount> <duration>");
				utils.sendMsg(sender, "&c/asa removemultiplier <player> <id>");
				return true;
			}
			if (args[0].equalsIgnoreCase("setmultiplier")) {
				if (args.length < 3) {
					utils.sendMsg(sender, "&c/asa setmultiplier <player|global> <amount> [duration]");
					return true;
				}
				float amount = Float.valueOf(args[2]);
				if (UserLoader.getUser(Bukkit.getOfflinePlayer(args[1]).getName()) != null) {
					User u = UserLoader.getUser(Bukkit.getOfflinePlayer(args[1]).getName());
					u.setMultiplier(Float.valueOf(args[2]));
					UserLoader.addUser(u);
					try {
						Main.sql.executeUpdate("UPDATE "+Main.getCfg().getTable()+" SET MULTIPLIER = " +u.getMultiplier() + " WHERE NAME = '"+u.getName()+"'");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					utils.sendMsg(sender, "&aMultiplier permanente settato");
					return true;
				}else if (args[1].equalsIgnoreCase("global")) {
					if (args.length == 4) {
						GlobalMultiplier.setMultiplier(amount);
						GlobalMultiplier.setTime(Integer.valueOf(args[3]));
						utils.sendMsg(sender, "&aMultiplier globale settato");
						return true;
					} else {
						utils.sendMsg(sender, "&cIl multiplier global necessita di durata");
						return true;
					}
				}else {
					utils.sendMsg(sender, "&cGiocatore inesistente");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("addmultiplier")) {
				if (args.length < 3) {
					utils.sendMsg(sender, "&c/asa addmultiplier <player> <amount> <duration>");
					return true;
				}
				float amount = Float.valueOf(args[2]);
				if (UserLoader.getUser(Bukkit.getOfflinePlayer(args[1]).getName()) != null) {
					if (args.length == 4) {
						MultiplierListeners.addMultiplier(Bukkit.getOfflinePlayer(args[1]).getName(), new Multiplier(Bukkit.getOfflinePlayer(args[1]).getName(),amount,Integer.valueOf(args[3])));
						utils.sendMsg(sender, "&aMultiplier temporaneo aggiunto");
						return true;
					}
				}else {
					utils.sendMsg(sender, "&cGiocatore inesistente");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("removemultiplier")) {
				if (args.length < 3) {
					utils.sendMsg(sender, "&c/asa removemultiplier <player> <id>");
					return true;
				}
				if (MultiplierListeners.getMultiplier(Bukkit.getOfflinePlayer(args[1]).getName()) != null) {
					if (args.length == 3) {
						MultiplierListeners.removeMultiplier(Bukkit.getOfflinePlayer(args[1]).getName(), Integer.valueOf(args[2]));
						utils.sendMsg(sender, "&aMultiplier settato");
						return true;
					}
				}else {
					utils.sendMsg(sender, "&cQuesto giocatore non ha un multiplier");
					return true;
				}
			}
	
		}else if (cmd.getName().equalsIgnoreCase("multiplier")) {
			if (args.length == 0)
				Messages.multiplierprivate((Player)sender);
			else if (args[0].equalsIgnoreCase("global"))
				Messages.multiplierglobal((Player)sender);
			else if (UserLoader.getUser(Bukkit.getOfflinePlayer(args[0]).getName()) != null)
				Messages.multiplieruser((Player)sender, args[0]);
			else
				Messages.noplayer((Player)sender);
			return true;
		}else if (cmd.getName().equalsIgnoreCase("sellall")) {
			float sell = SellSystem.sellAll((Player)sender);
			Messages.sellall((Player)sender, sell);
			return true;
		}else if (cmd.getName().equalsIgnoreCase("autosell")) {
			if (!sender.hasPermission("*") && !sender.hasPermission("asa") && !sender.hasPermission("autosell")) {
				Messages.noperm(sender);
				return true;
			}
			if (SellSystem.toggleAutoSell((Player)sender)) Messages.autosellenable((Player)sender);
			else Messages.autoselldisable((Player)sender);
			return true;
		}
		return true;
	}

}
