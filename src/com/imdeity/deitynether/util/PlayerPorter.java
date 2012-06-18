package com.imdeity.deitynether.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;

public class PlayerPorter {

	private DeityNether plugin = null;
	
	
	public PlayerPorter(DeityNether instance){
		plugin = instance;
	}
	
	public void sendToNether(Player player){
		if(player.hasPermission(DeityNether.GENERAL_PERMISSION)){
			//TODO test player.getInventory()
			//TODO test time left till next port
			//TODO port to nether
		}else if(player.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			//TODO ? test player.getInventory()
			//TODO ? test tie until next port
			//TODO port to nether
		}else{
			player.sendMessage(DeityNether.HEADER + "§cYou don't have permission");
		}
	}
	
	public void sendToOverworld(Player player){
		World overworld = plugin.getServer().getWorld(plugin.config.getMainWorldName());
		player.teleport(new Location(overworld, 100, 0, 100));
	}
	
	private boolean testPlayerInventory(Player p){
		return false;
	}
	
}
