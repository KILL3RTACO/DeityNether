package com.imdeity.deitynether.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;

public class PlayerPorter {

	private final int GOLD_NEEDED = DeityNether.config.getNeededGold();
	private ChatUtils cu = new ChatUtils();
	
	public void sendToNether(Player p){
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			testAndPort(p, false);
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(p.getWorld() == DeityNether.server.getWorld(DeityNether.config.getNetherWorldName())){
				cu.sendErrorMessage("You are already in the nether", p);
			}else{
				if(PlayerStats.hasWaited(p) || PlayerStats.hasTimeLeft(p)){
					testAndPort(p, true);
				}else{
					cu.sendErrorMessage("Sorry, you need to wait " + PlayerStats.getWaitTimeLeft(p), p);
				}
			}
		}else{
			cu.sendInvalidPermissionMessage(p);
		}
	}
	
	public void sendToOverworld(Player p, boolean mysql){
		if(p.getWorld() == DeityNether.server.getWorld(DeityNether.config.getMainWorldName())){
			cu.sendErrorMessage("You aren't in the nether", p);
		}else{
			cu.sendInfoMessage("&aTeleporting you to the main world...", p);
			p.teleport(DeityNether.config.getMainWorldSpawn());
			if(mysql) PlayerStats.setLeaveTime(p, false);
		}
	}
	
	private void testAndPort(Player p, boolean mysql){
		if(p.getWorld() == DeityNether.server.getWorld(DeityNether.config.getNetherWorldName())){
			cu.sendErrorMessage("You are already in the nether", p);
		}else{
			if(testPortConditions(p)){
				p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK, DeityNether.config.getNeededGold()));
				cu.sendInfoMessage("&aTeleporting you to the nether...", p);
				testSpawn();
				p.teleport(DeityNether.config.getNetherWorldSpawn());
				if(mysql) PlayerStats.setJoinTime(p);
			}
		}
	}
	
	private void testSpawn() {
		if(DeityNether.plugin.needsSpawn){
			DeityNether.plugin.wm.createSpawn(DeityNether.config.getNetherWorldSpawn());
		}
	}

	private boolean testPortConditions(Player p){
		int gold = 0;
		for(ItemStack i : p.getInventory()){
			if(i != null){
				if(i.getType() == Material.GOLD_BLOCK){
					gold += i.getAmount();
				}else{
					if(!AllowedItems.contains(i.getTypeId())){
						cu.sendErrorMessage("You have items in your inventory that are not allowed in the Nether. " +
								"Remove them and try again", p);
						return false;
					}
				}
			}
		}
		
		if(gold == GOLD_NEEDED)
			return true;
		else if(gold >= DeityNether.config.getNeededGold()){
			cu.sendErrorMessage("You have too much gold. I wouldn't mind taking it... But alas, I am only an entity :'(", p);
			return false;
		}else{
			cu.sendErrorMessage("You do not have enough gold -_- Get moar nao! Omnomnomnom <3", p);
			return false;
		}
	}
	
}
