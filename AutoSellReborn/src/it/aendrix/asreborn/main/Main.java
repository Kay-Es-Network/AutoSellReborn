package it.aendrix.asreborn.main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import it.aendrix.asreborn.files.Config;
import it.aendrix.asreborn.files.Messages;
import it.aendrix.asreborn.listeners.Commands;
import it.aendrix.asreborn.listeners.GlobalMultiplier;
import it.aendrix.asreborn.listeners.MultiplierListeners;
import it.aendrix.asreborn.listeners.Prices;
import it.aendrix.asreborn.listeners.SellSystem;
import it.aendrix.asreborn.listeners.UserLoader;
import it.aendrix.asreborn.placeholders.UserPlaceholders;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Economy econ;
	private static Main instance;
	private static Config config;

	@Override
	public void onEnable() {
		instance = this;

		createDir();
		
		if (!setupEconomy()) 
			System.out.println("VAULT ERROR");

		config = new Config();
		new Messages();
		
		try {
			tryConnection();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		} 
		
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new UserPlaceholders(this).register();
		}
		
		UserLoader.loadAll();
		Prices.load();
		MultiplierListeners.loadAll();
		GlobalMultiplier.start();
		
		getServer().getPluginManager().registerEvents((Listener)new UserLoader(), (Plugin)this);
		getServer().getPluginManager().registerEvents((Listener)new SellSystem(), (Plugin)this);
		this.getCommand("asa").setExecutor(new Commands());
		this.getCommand("autosell").setExecutor(new Commands());
		this.getCommand("sellall").setExecutor(new Commands());
		this.getCommand("multiplier").setExecutor(new Commands());

	}

	@Override
	public void onDisable() {
		MultiplierListeners.saveAll();
		UserLoader.saveAll();
	}
	
	public static Connection getConnection() {
		return connection;
	}

	public static Statement getSQL() {
		return sql;
	}

	private static void createDir() {
		new File("plugins/AutoSellReborn").mkdir();
	}

	public static Main getInstance() {
		return instance;
	}

	public static Config getCfg() {
		return config;
	}

	// DATABASE
	public static Connection connection;
	public static Statement sql;

	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed())
			return;
		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				System.out.println("CONNESSIONE A MYSQL FALLITA");
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + getCfg().getHost() + ":" + getCfg().getPort() + "/" + getCfg().getDb(),
					getCfg().getUsername(), getCfg().getPassword());
		}
	}

	public void tryConnection() throws SQLException, ClassNotFoundException, IOException {
		openConnection();
		sql = connection.createStatement();
	}

	public static void reloadConnection() throws SQLException, ClassNotFoundException, IOException {
		sql.close();
		connection.close();
		instance.tryConnection();
	}
	
	private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public static Economy getEconomy() {
		return econ;
	}

}
