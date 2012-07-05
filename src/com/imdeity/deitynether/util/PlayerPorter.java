package com.imdeity.deitynether.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class PlayerPorter {

	private DeityNether plugin = null;
	private NetherTime nt = null;
	private int neededGold = 0;
	
	public PlayerPorter(DeityNether instance){
		plugin = instance;
		nt = new NetherTime(plugin);
		neededGold = plugin.config.getNeededGold();
	}
	
	public void sendToNether(Player p){
		DeityPlayer player = new DeityPlayer(p, plugin);
		if(player.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(player.getWorld() == plugin.getServer().getWorld(plugin.config.getNetherWorldName())){
				player.sendErrorMessage("You are already in the nether");
			}else{
				if(player.hasWaited() || player.hasTimeLeft()){
					int result = testPlayerInventory(player);
					if(result == 1){
						player.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK, plugin.config.getNeededGold()));
						player.sendInfoMessage("%aTeleporting you to the nether...");
						player.teleport(plugin.config.getNetherWorldSpawn());
						plugin.mysql.setJoinTime(player);
					}else if(result == 0){ //not enough gold
						player.sendErrorMessage("You do not have enough gold to go to the nether. Go get more.");
					}else if(result == -1){ //Illegal items
						player.sendErrorMessage("You have items in your inventory that are not allowed in the nether. Remove them and try again");
					}else if(result == 2){ //Too much gold >.>
						player.sendErrorMessage("You have too much gold in your inventory. Please only carry %6" + neededGold + "GOLD_BLOCK" +
								" %cwhen going to the nether");
					}
				}else{
					player.sendErrorMessage("Sorry, you need to wait " + nt.getWaitTimeLeft(player));
				}
			}
		}else if(player.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			player.sendInfoMessage("%aTeleporting you to the nether...");
			player.teleport(plugin.config.getNetherWorldSpawn());
		}else{
			player.sendInvalidPermissionMessage();
		}
	}
	
	public void sendToOverworld(Player p){
		DeityPlayer player = new DeityPlayer(p, plugin);
		if(player.getWorld() == plugin.getServer().getWorld(plugin.config.getMainWorldName())){
			player.sendErrorMessage("You aren't in the nether");
		}else{
			player.sendInfoMessage("%aTeleporting you to the main world...");
			player.teleport(plugin.config.getMainWorldSpawn());
			plugin.mysql.setLeaveTime(player, false);
		}
	}
	
	private int testPlayerInventory(DeityPlayer p){
		int gold = 0;
		for(ItemStack i : p.getInventory()){
			if(i != null)
				if(i.getType() == Material.GOLD_BLOCK){
					gold += i.getAmount();
				}else{
					if(!AllowedItems.contains(i.getTypeId())) return -1;
				}
		}
		
		if(gold == neededGold)
			return 1;
		else if(gold >= plugin.config.getNeededGold())
			return 2;
		else
			return -1;
	}
	
}
