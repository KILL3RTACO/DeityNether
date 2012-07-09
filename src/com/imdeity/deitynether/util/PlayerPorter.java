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
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			DeityPlayer.sendInfoMessage("%aTeleporting you to the nether...", p);
			testSpawn();
			p.teleport(plugin.config.getNetherWorldSpawn());
				
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(p.getWorld() == plugin.getServer().getWorld(plugin.config.getNetherWorldName())){
				DeityPlayer.sendErrorMessage("You are already in the nether", p);
			}else{
				if(DeityPlayer.hasWaited(p, plugin) || DeityPlayer.hasTimeLeft(p, plugin)){
					int result = testPlayerInventory(p);
					if(result == 1){
						p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK, plugin.config.getNeededGold()));
						DeityPlayer.sendInfoMessage("%aTeleporting you to the nether...", p);
						testSpawn();
						plugin.mysql.setJoinTime(p);
					}else if(result == 0){ //not enough gold
						DeityPlayer.sendErrorMessage("You do not have enough gold to go to the nether. Go get more.", p);
					}else if(result == -1){ //Illegal items
						DeityPlayer.sendErrorMessage("You have items in your inventory that are not allowed in the nether. Remove them and try again", p);
					}else if(result == 2){ //Too much gold >.>
						DeityPlayer.sendErrorMessage("You have too much gold in your inventory. Please only carry %6" + neededGold + " GOLD_BLOCK" +
								" %cwhen going to the nether", p);
					}
				}else{
					DeityPlayer.sendErrorMessage("Sorry, you need to wait " + nt.getWaitTimeLeft(p), p);
				}
			}
		}else{
			DeityPlayer.sendInvalidPermissionMessage(p);
		}
	}
	
	public void sendToOverworld(Player p){
		if(p.getWorld() == plugin.getServer().getWorld(plugin.config.getMainWorldName())){
			DeityPlayer.sendErrorMessage("You aren't in the nether", p);
		}else{
			DeityPlayer.sendInfoMessage("%aTeleporting you to the main world...", p);
			p.teleport(plugin.config.getMainWorldSpawn());
			plugin.mysql.setLeaveTime(p, false);
		}
	}
	
	private int testPlayerInventory(Player p){
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
			return 0;
	}
	
	private void testSpawn(){
		if(plugin.needsSpawn){
			plugin.wm.createSpawn(plugin.config.getNetherWorldSpawn());
			plugin.needsSpawn = false;
		}
	}
	
}
