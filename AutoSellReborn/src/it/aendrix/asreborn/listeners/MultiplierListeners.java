package it.aendrix.asreborn.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MultiplierListeners {

	//TIMED MULT
	private static HashMap<String, Multiplier[]> multipliers = new HashMap<String, Multiplier[]>();
	
	public static Multiplier[] getMultiplier(String player) {
		return multipliers.get(player);
	}
	
	public static void addMultiplier(String player, Multiplier m) {
		Multiplier[] mm;
		if (multipliers.containsKey(player)) {
			mm = new Multiplier[multipliers.get(player).length+1];
			for (int i = 0; i<mm.length-1; i++)
				mm[i] = multipliers.get(player)[i];
			mm[mm.length-1] = m;
		} else {
			mm = new Multiplier[1];
			mm[0] = m;
		}
		multipliers.put(player, mm);
	}
	
	public static void removeMultiplier(String player, int id) {
		Multiplier[] mm;
		if (multipliers.containsKey(player)) {
			if (id<0 || multipliers.get(player).length!=1) {
				multipliers.remove(player);
				return;
			}
			ArrayList<Multiplier> mx = new ArrayList<Multiplier>();
			for (int i = 0; i<multipliers.get(player).length; i++)
				if (i!=id)
					mx.add(multipliers.get(player)[i]);
				else
					multipliers.get(player)[i].stop();
			
			mm = mx.toArray(new Multiplier[mx.size()]);
			
			multipliers.replace(player, mm);
		}else return;
	}
	
	public static void loadAll() {
		File f = new File("plugins/AutoSellReborn" + File.separator + "multipliers.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		
		if (!f.exists()) return;
		
		if (cfg.getString("multipliers")==null) return;
		
		for(String player : cfg.getConfigurationSection("multipliers").getKeys(false))
			for(String key : cfg.getConfigurationSection("multipliers."+player).getKeys(false))
				addMultiplier(player,new Multiplier(player, (float) cfg.getDouble("multipliers."+player+"."+key+".amount"),
						cfg.getInt("multipliers."+player+"."+key+".remain")));
	}
	
	public static void saveAll() {
		File f = new File("plugins/AutoSellReborn" + File.separator + "multipliers.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		
		if (!f.exists()) 
			try {
				f.createNewFile();
			} catch (IOException e) {}

		for (String player : multipliers.keySet()) {
			Multiplier[] mm = getMultiplier(player);
			for(int i = 0; i<mm.length; i++) {
				if (mm[i].getTime() <= 0)
					continue;
				cfg.set("multipliers."+player+"."+i+".amount", mm[i].getAmount());
				cfg.set("multipliers."+player+"."+i+".remain", mm[i].getTime());
				mm[i].stop();
			}
		}
		
		try {
			cfg.save(f);
		} catch (IOException e) {}
	}
	
}
