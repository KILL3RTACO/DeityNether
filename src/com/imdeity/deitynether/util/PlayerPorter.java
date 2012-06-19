package com.imdeity.deitynether.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;

public class PlayerPorter {

	private DeityNether plugin = null;
	
	
	public PlayerPorter(DeityNether instance){
		plugin = instance;
	}
	
	public void sendToNether(Player player){
		if(player.hasPermission(DeityNether.GENERAL_PERMISSION)){
			//TODO test time left till next port
			if(testPlayerInventory(player)){
				player.teleport(plugin.config.getNetherWorldSpawn());
			}else{
				player.sendMessage(DeityNether.HEADER + "§cYou have items in your inventory that are not allowed to be brought into the" +
						" nether, please remove them and try again");
			}
		}else if(player.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			player.teleport(plugin.config.getNetherWorldSpawn());
		}else{
			player.sendMessage(DeityNether.HEADER + "§cYou don't have permission");
		}
	}
	
	public void sendToOverworld(Player player){
		player.teleport(plugin.config.getMainWorldSpawn());
	}
	
	private boolean testPlayerInventory(Player p){
		for(ItemStack i : p.getInventory()){
			if(i != null)
				if(!AllowedItems.contains(i.getTypeId())) return false;
		}
		return true;
	}
	
}
