package it.aendrix.asreborn.listeners;

public class User {

	String name;
	float multiplier;
	boolean autosell;
	long selled;
	long selledmoney;
	
	public User(String name, float multiplier, boolean autosell, long selled, long selledmoney) {
		this.name = name;
		this.multiplier = multiplier;
		this.autosell = autosell;
		this.selled = selled;
		this.selledmoney = selledmoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(float f) {
		this.multiplier = f;
	}

	public boolean isAutosell() {
		return autosell;
	}

	public void setAutosell(boolean autosell) {
		this.autosell = autosell;
	}

	public long getSelled() {
		return selled;
	}

	public void setSelled(long selled) {
		this.selled = selled;
	}

	public long getSelledmoney() {
		return selledmoney;
	}

	public void setSelledmoney(long selledmoney) {
		this.selledmoney = selledmoney;
	}

}
