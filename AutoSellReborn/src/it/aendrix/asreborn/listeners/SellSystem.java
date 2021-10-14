package it.aendrix.asreborn.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import it.aendrix.asreborn.files.Messages;
import it.aendrix.asreborn.main.Main;

public class SellSystem implements Listener {

	public static float getSell(ItemStack item, float multiplier) {
		if (item != null)
			return item.getAmount() * Prices.getPrice(item.getType()) * GlobalMultiplier.getMultiplier() * multiplier;
		return 0;
	}
	
	public static float getTimedMultipliers(String player) {
		float a = 0;
		Multiplier[] mm = MultiplierListeners.getMultiplier(player);
		if (mm==null) return 0;
		for (Multiplier m : mm)
			a += m.getAmount();
		return a;
	}
	
	public static float sell(Player p, int slot) {
		ItemStack item = p.getInventory().getItem(slot);
		float sell = getSell(item, UserLoader.getUser(p.getName()).getMultiplier()+getTimedMultipliers(p.getName()));
		if (item != null && Prices.exist(item.getType())) {
			p.getInventory().setItem(slot, null);
			User u = UserLoader.getUser(p.getName());
			u.setSelled(u.getSelled()+item.getAmount());
			u.setSelledmoney((long)(u.getSelledmoney()+sell));
			UserLoader.addUser(u);
		}
		else return 0;
			
		return sell;
	}
	
	public static boolean addMoney(Player p, float i) {
		if (i==0) return false;
		Main.getEconomy().depositPlayer(p, i);
		return true;
	}
	
	public static float sellAll(Player p) {
		Inventory inv = p.getInventory();
		float sell = 0;
		for (int i = 0; i<inv.getSize(); i++)
			sell += sell(p,i);
		if (sell==0)
			return -1;
		addMoney(p,sell);
		return sell;
	}
	
	public static boolean toggleAutoSell(Player p) {
		User u = UserLoader.getUser(p.getName());
		boolean a = !u.isAutosell();

		u.setAutosell(a);
		UserLoader.addUser(u);
		
		return a;
	}
	
	public static boolean hasSpaceInventory(Inventory inv) {
		for (int i = 0; i<36; i++)
			if (inv.getItem(i) == null) return true;
		return false;
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onBreak(BlockBreakEvent e) {
		User u = UserLoader.getUser(e.getPlayer().getName());
		if (u.isAutosell() && !hasSpaceInventory(e.getPlayer().getInventory())) {
			float sellall = sellAll(e.getPlayer());
			if (sellall!=-1)
				Messages.autosell(e.getPlayer(),sellall);
			else
				Messages.autosellnoinv(e.getPlayer());
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onKill(EntityDeathEvent e) {
		if (e.getEntity().getKiller() == null && !(e.getEntity().getKiller() instanceof Player)) return;
		Player p = (Player) e.getEntity().getKiller();
		User u = UserLoader.getUser(p.getName());
		if (u.isAutosell() && !hasSpaceInventory(p.getInventory())) {
			float sellall = sellAll(p);
			if (sellall!=-1)
				Messages.autosell(p,sellall);
			else
				Messages.autosellnoinv(p);
		}
	}
	
}
