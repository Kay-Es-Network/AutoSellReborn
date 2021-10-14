package it.aendrix.asreborn.placeholders;

import org.bukkit.entity.Player;

import it.aendrix.asreborn.listeners.SellSystem;
import it.aendrix.asreborn.listeners.UserLoader;
import it.aendrix.asreborn.main.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class UserPlaceholders extends PlaceholderExpansion{
	
	private Main plugin;

	public UserPlaceholders(Main plugin){
		this.plugin = plugin;
	}
	
	@Override
    public boolean persist(){
        return true;
    }

	@Override
    public boolean canRegister(){
        return true;
    }
    
	@Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

	@Override
    public String getIdentifier(){
        return "autosell";
    }

	@Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
	public String onPlaceholderRequest(Player p, String id) {
    	if (p!=null) {
    		if (id.equalsIgnoreCase("name")) {
    			return ""+UserLoader.getUser(p.getName()).getName();
    		}if (id.equalsIgnoreCase("multiplier")) {
    			return ""+UserLoader.getUser(p.getName()).getMultiplier()+SellSystem.getTimedMultipliers(p.getName());
    		}if (id.equalsIgnoreCase("autosell")) {
    			if (UserLoader.getUser(p.getName()).isAutosell()) {
    				return "&fABILITATO";
    			}else {
    				return "&fDISABILITATO";
    			}
    		}if (id.equalsIgnoreCase("selled")) {
    			return ""+UserLoader.getUser(p.getName()).getSelled();
    		}if (id.equalsIgnoreCase("selledmoney")) {
    			return ""+UserLoader.getUser(p.getName()).getSelledmoney();
    		}else return "";
    	}
    	return null;
    }

}