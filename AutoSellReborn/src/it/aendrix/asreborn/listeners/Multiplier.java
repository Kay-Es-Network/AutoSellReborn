package it.aendrix.asreborn.listeners;

import org.bukkit.scheduler.BukkitRunnable;

import it.aendrix.asreborn.main.Main;

public class Multiplier {

	String player;
	float amount;
	int time;

	public Multiplier(String player, float amount, int time) {
		this.player = player;
		this.amount = amount;
		this.time = time;
		
		start();
	}
	
	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	BukkitRunnable task;
	
	protected void start() {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				task = this;
				if (time<=0) {
					setAmount(0);
					
					Multiplier[] mm = MultiplierListeners.getMultiplier(player);
					for (int i = 0; i<mm.length; i++)
						if (mm[i].getTime() == getTime())
							MultiplierListeners.removeMultiplier(player, i);
					
					setPlayer(null);
					
					cancel();
				}
				time--;
			}
			
		}.runTaskTimerAsynchronously(Main.getInstance(), 20L, 20L);
		
	}
	
	protected void stop() {
		task.cancel();
	}
	
	

}
