package it.aendrix.asreborn.main;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class utils {

	public static void sendMsg(Player p, String s) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public static void sendMsg(CommandSender p, String s) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public static String formatSeconds(int s) {
		int hours = s / 3600;
		int minutes = (s % 3600) / 60;
		int seconds = s % 60;
		return hours+"h:"+minutes+"m:"+seconds+"s";
	}
	
	public static String[] getFormatString(String s) {
		return s.split("/n");
	}
	
}
