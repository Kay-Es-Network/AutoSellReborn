package it.aendrix.asreborn.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import it.aendrix.asreborn.listeners.GlobalMultiplier;
import it.aendrix.asreborn.listeners.Multiplier;
import it.aendrix.asreborn.listeners.MultiplierListeners;
import it.aendrix.asreborn.listeners.UserLoader;
import it.aendrix.asreborn.main.utils;

public class Messages {

	static File f = new File("plugins/AutoSellReborn" + File.separator + "messages.yml");
	static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public Messages() {
		if (!f.exists()) {
			try {
				f.createNewFile();
				
				cfg.set("autosell.sell", "/n&fGli oggetti del tuo inventario sono stati venduti automaticamente e ha guadagnato &b%SELL%$/n");
				cfg.set("autosell.nosell", "/n&c&lINVENTARIO PIENO/n/n&fIl tuo inventario non contiene alcun oggetto vendibile/n");
				cfg.set("autosell.enable", "&a&lAUTOSELL ABILITATO");
				cfg.set("autosell.disable", "&8&lAUTOSELL DISABILITATO");
				cfg.set("multiplier.global", "&6&lMULTIPLIER GLOBALE/n/n&fIl multiplier globale è impostato a &b%AMOUNT%/n&7Tempo rimanente -> %TIME%/n");
				cfg.set("multiplier.private", "&6&lMULTIPLIER PRIVATO/n/n&fIl tuo multiplier permanente è impostato a &b%AMOUNT%/n");
				cfg.set("multiplier.user", "&6&lMULTIPLIER DI %PLAYER%/n/n&fIl multiplier permanente è impostato a &b%AMOUNT%/n");
				cfg.set("multiplier.temp", "/n&fMultiplier temporanei:/n%MULTIPLIERS%/n");
				cfg.set("multiplier.format", "&b&l%ID% &7-> &f%TIME% (&b%AMOUNT%&f)");
				
				cfg.set("error.noplayer", "&fQuesto giocatore non esiste");
				cfg.set("error.noperm", "&fNon hai abbastanza permessi");
		
				cfg.set("sellall.sell", "/n&fGli oggetti del tuo inventario sono stati venduti automaticamente e ha guadagnato &b%SELL%$/n");
				cfg.set("sellall.nosell", "/n&fIl tuo inventario non contiene alcun oggetto vendibile/n");
				
				cfg.save(f);
			} catch (IOException e) {}
		}
	}

	public File getFile() {
		return f;
	}

	public FileConfiguration getConfig() {
		return cfg;
	}

	
	
	public static void autosell(Player p, float sell) {
		String[] l = utils.getFormatString(cfg.getString("autosell.sell"));
		for (String s : l)
			utils.sendMsg(p, s.replaceAll("%SELL%", sell+""));	
	}

	public static void autosellnoinv(Player p) {
		String[] l = utils.getFormatString(cfg.getString("autosell.nosell"));
		for (String s : l)
			utils.sendMsg(p, s);	
	}
	
	public static void autosellenable(Player p) {
		String[] l = utils.getFormatString(cfg.getString("autosell.enable"));
		for (String s : l)
			utils.sendMsg(p, s);
	}
	
	public static void autoselldisable(Player p) {
		String[] l = utils.getFormatString(cfg.getString("autosell.disable"));
		for (String s : l)
			utils.sendMsg(p, s);
	}
	
	public static void sellall(Player p, float sell) {
		if (sell==-1) {
			String[] l = utils.getFormatString(cfg.getString("sellall.nosell"));
			for (String s : l)
				utils.sendMsg(p, s);
		}else {
			String[] l = utils.getFormatString(cfg.getString("sellall.sell"));
			for (String s : l)
				utils.sendMsg(p, s.replaceAll("%SELL%", sell+""));	
		}
	}
	
	public static void multiplierglobal(Player p) {
		float am = GlobalMultiplier.getMultiplier();
		String time = utils.formatSeconds(GlobalMultiplier.getTime());
		if (GlobalMultiplier.getTime()<=0) time = "&cScaduto";
		String[] l = utils.getFormatString(cfg.getString("multiplier.global"));
		for (String s : l)
			utils.sendMsg(p, s.replaceAll("%TIME%", time).replaceAll("%AMOUNT%", am+""));
	}
	
	public static void multiplierprivate(Player p) {
		float am = UserLoader.getUser(p.getName()).getMultiplier();
		String[] l = utils.getFormatString(cfg.getString("multiplier.private"));
		for (String s : l)
			utils.sendMsg(p, s.replaceAll("%AMOUNT%", am+""));
		
		if (MultiplierListeners.getMultiplier(p.getName())!=null) {
			Multiplier[] mm = MultiplierListeners.getMultiplier(p.getName());
			l = utils.getFormatString(cfg.getString("multiplier.temp"));
			for (String s : l)
				if (s.contains("%MULTIPLIERS%"))
					for (int i = 0; i<mm.length; i++)
						utils.sendMsg(p, cfg.getString("multiplier.format").replaceAll("%ID%", ""+i)
								.replaceAll("%TIME%", utils.formatSeconds(mm[i].getTime())).replaceAll("%AMOUNT%", ""+mm[i].getAmount()));
				else
					utils.sendMsg(p, s);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void multiplieruser(Player p, String p2) {
		float am = UserLoader.getUser(Bukkit.getOfflinePlayer(p2).getName()).getMultiplier();
		String[] l = utils.getFormatString(cfg.getString("multiplier.user"));
		for (String s : l)
			utils.sendMsg(p, s.replaceAll("%AMOUNT%", am+"").replaceAll("%PLAYER%", p2));
		
		if (MultiplierListeners.getMultiplier(Bukkit.getOfflinePlayer(p2).getName())!=null) {
			Multiplier[] mm = MultiplierListeners.getMultiplier(Bukkit.getOfflinePlayer(p2).getName());
			l = utils.getFormatString(cfg.getString("multiplier.temp"));
			for (String s : l)
				if (s.contains("%MULTIPLIERS%"))
					for (int i = 0; i<mm.length; i++)
						utils.sendMsg(p, cfg.getString("multiplier.format").replaceAll("%ID%", ""+i)
								.replaceAll("%TIME%", utils.formatSeconds(mm[i].getTime())).replaceAll("%AMOUNT%", ""+mm[i].getAmount()));
				else
					utils.sendMsg(p, s);
		}
	}
	
	public static void noplayer(Player p) {
		String[] l = utils.getFormatString(cfg.getString("error.noplayer"));
		for (String s : l)
			utils.sendMsg(p, s);
	}
	
	public static void noperm(CommandSender p) {
		String[] l = utils.getFormatString(cfg.getString("error.noperm"));
		for (String s : l)
			utils.sendMsg(p, s);
	}

}
