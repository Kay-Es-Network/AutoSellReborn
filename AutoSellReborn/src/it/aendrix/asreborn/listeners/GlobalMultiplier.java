package it.aendrix.asreborn.listeners;

import org.bukkit.scheduler.BukkitRunnable;

import it.aendrix.asreborn.main.Main;

public class GlobalMultiplier {

	private static float multiplier = 1;
	private static int time = 0;

	public static void start() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (time <= 0)
					multiplier = 1;
				else time --;
			}

		}.runTaskTimerAsynchronously(Main.getInstance(), 20L, 20L);
	}

	public static float getMultiplier() {
		return multiplier;
	}

	public static void setMultiplier(float multiplier) {
		GlobalMultiplier.multiplier = multiplier;
	}

	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		GlobalMultiplier.time = time;
	}
	
	public static boolean enabled() {
		return time > 0;
	}

}
