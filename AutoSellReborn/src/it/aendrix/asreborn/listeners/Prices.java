package it.aendrix.asreborn.listeners;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import it.aendrix.asreborn.files.Config;
import it.aendrix.asreborn.main.Main;

public class Prices {

	private static HashMap<Material, Float> prices = new HashMap<Material, Float>();
	private static Config cfg = Main.getCfg();
	
	public static HashMap<Material, Float> all() {
		return prices;
	}
	
	public static boolean exist(Material mat) {
		return prices.containsKey(mat);
	}
	
	public static float getPrice(Material mat) {
		if (exist(mat))
			return prices.get(mat);
		return 0;
	}
	
	public static float getPrice(String mat) {
		return prices.get(Material.valueOf(mat.toUpperCase()));
	}
	
	public static void load() {
		if (cfg.isUseshopguiplus() && !(new File("plugins/AutoSellReborn" + File.separator + "prices.yml").exists())) {
			String[] l = new File("plugins/ShopGUIPlus/shops").list();
			File f; FileConfiguration cc;
			for (String s : l) {
				System.out.println(s);
				f = new File("plugins/ShopGUIPlus/shops" + File.separator + s);
				cc = YamlConfiguration.loadConfiguration(f);
				String st = s.replace(".yml", "")+".items";
				
				if (cc.getConfigurationSection(st)==null)
					continue;
				
				for(String key : cc.getConfigurationSection(st).getKeys(false))
					if (cc.getString(st+"."+key+".sellPrice") != null)
						prices.put(Material.valueOf(cc.getString(st+"."+key+".item.material").toUpperCase()), (float) cc.getDouble(st+"."+key+".sellPrice"));
			}
			
			f = new File("plugins/AutoSellReborn" + File.separator + "prices.yml");
			cc = YamlConfiguration.loadConfiguration(f);
			
			for (Material m : prices.keySet())
				cc.set("items."+m.toString(), getPrice(m));
			
			try {
				cc.save(f);
			} catch (IOException e) {}
			
		} else {
			File f = new File("plugins/AutoSellReborn" + File.separator + "prices.yml");
			FileConfiguration cc = YamlConfiguration.loadConfiguration(f);
			if (f.exists()) {
				if (cc.getString("items")==null) return;
				for(String key : cc.getConfigurationSection("items").getKeys(false))
					prices.put(Material.valueOf(key), (float) cc.getDouble("items."+key));
			} else
				try {
					f.createNewFile();
				} catch (IOException e) {}
		}
	}
	
}
