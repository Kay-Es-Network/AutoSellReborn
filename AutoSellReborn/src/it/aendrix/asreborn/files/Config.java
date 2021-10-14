package it.aendrix.asreborn.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	File f = new File("plugins/AutoSellReborn" + File.separator + "config.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public File getFile() {
		return f;
	}
	
	public FileConfiguration getConfig() {
		return cfg;
	}
	
	boolean usemysql,useshopguiplus;
	String host, password, username, db, table;
	int port;
	float basemultiplier;
	
	public Config() {
		if (!f.exists()) {
			usemysql = false;
			useshopguiplus = false;
			host = "localhost";
			password = "";
			db = "";
			username = "";
			table = "user_data";
			port= 3306;
			basemultiplier = 1;
			
			try {
				f.createNewFile();
			} catch (IOException e) {}
			
			cfg.set("database.host", host);
			cfg.set("database.port", port);
			cfg.set("database.database", db);
			cfg.set("database.password", password);
			cfg.set("database.username", username);
			cfg.set("database.tablename", table);
			cfg.set("options.use-shopguiplus-prices", useshopguiplus);
			if (usemysql)
				cfg.set("options.storage", "MYSQL");
			else
				cfg.set("options.storage", "FILE");
			cfg.set("options.base-multiplier", basemultiplier);
			
			try {
				cfg.save(f);
			} catch (IOException e) {}
		}else {
			usemysql = false;
			if (cfg.getString("options.storage").equalsIgnoreCase("MYSQL")) {
				usemysql = true;
				host = cfg.getString("database.host");
				username = cfg.getString("database.username");
				db = cfg.getString("database.database");
				table = cfg.getString("database.tablename");
				password = cfg.getString("database.password");
				port = cfg.getInt("database.port");
			}
			basemultiplier = (float) cfg.getDouble("options.base-multiplier");
			useshopguiplus = cfg.getBoolean("options.use-shopguiplus-prices");
		}
	}

	public boolean isUsemysql() {
		return usemysql;
	}

	public boolean isUseshopguiplus() {
		return useshopguiplus;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getDb() {
		return db;
	}

	public String getTable() {
		return table;
	}

	public int getPort() {
		return port;
	}

	public float getBasemultiplier() {
		return basemultiplier;
	}

}
