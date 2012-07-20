package com.imdeity.deitynether.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;

public class PlayerPorter {

	private final int GOLD_NEEDED = DeityNether.config.getNeededGold();
	
	public void sendToNether(Player p){
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			testAndPort(p, false);
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(p.getWorld() == DeityNether.server.getWorld(DeityNether.config.getNetherWorldName())){
				p.sendMessage(DeityNether.lang.formatAlreadyInNether());
			}else{
				if(PlayerStats.hasWaited(p) || PlayerStats.hasTimeLeft(p)){
					testAndPort(p, true);
				}else{
					p.sendMessage(DeityNether.lang.formatNeedToWait(PlayerStats.getWaitHoursLeft(p), 
							PlayerStats.getWaitMinutesLeft(p), PlayerStats.getWaitSecondsLeft(p)));
				}
			}
		}else{
			p.sendMessage(DeityNether.lang.formatInvalidArguments());
		}
	}
	
	public void sendToOverworld(Player p, boolean mysql){
		if(p.getWorld() == DeityNether.server.getWorld(DeityNether.config.getMainWorldName())){
			DeityNether.lang.formatNotInNether();
		}else{
			DeityNether.lang.formatLeaveMessage();
			p.teleport(DeityNether.config.getMainWorldSpawn());
			if(mysql) PlayerStats.setLeaveTime(p, false);
		}
	}
	
	private void testAndPort(Player p, boolean mysql){
		if(p.getWorld() == DeityNether.server.getWorld(DeityNether.config.getNetherWorldName())){
			p.sendMessage(DeityNether.lang.formatAlreadyInNether());
		}else{
			if(testPortConditions(p)){
				p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK, DeityNether.config.getNeededGold()));
				p.sendMessage(DeityNether.lang.formatJoinMessage());
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
						p.sendMessage(DeityNether.lang.formatInvalidItems());
						return false;
					}
				}
			}
		}
		
		if(gold == GOLD_NEEDED)
			return true;
		else if(gold > GOLD_NEEDED){
			p.sendMessage(DeityNether.lang.formatTooMuchGold());
			return false;
		}else{
			p.sendMessage(DeityNether.lang.formatTooLittleGold());
			return false;
		}
	}
	
}
