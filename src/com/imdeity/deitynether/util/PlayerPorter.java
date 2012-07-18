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
			testAndPort(p, false);
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(p.getWorld() == plugin.getServer().getWorld(plugin.config.getNetherWorldName())){
				DeityPlayer.sendErrorMessage("You are already in the nether", p);
			}else{
				if(DeityPlayer.hasWaited(p, plugin) || DeityPlayer.hasTimeLeft(p, plugin)){
					testAndPort(p, true);
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
	
	private void testAndPort(Player p, boolean mysql){
		if(p.getWorld() == plugin.getServer().getWorld(plugin.config.getNetherWorldName())){
			DeityPlayer.sendErrorMessage("You are already in the nether", p);
		}else{
			if(testPortConditions(p)){
				p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK, plugin.config.getNeededGold()));
				DeityPlayer.sendInfoMessage("%aTeleporting you to the nether...", p);
				testSpawn();
				p.teleport(plugin.config.getNetherWorldSpawn());
			}
		}
	}
	
	private boolean testPortConditions(Player p){
		int gold = 0;
		for(ItemStack i : p.getInventory()){
			if(i != null)
				if(i.getType() == Material.GOLD_BLOCK){
					gold += i.getAmount();
				}else{
					if(!AllowedItems.contains(i.getTypeId())){
						DeityPlayer.sendErrorMessage("You have items in your inventory that are not allowed in the Nether. " +
								"Remove them and try again", p);
						return false;
					}
				}
		}
		
		if(gold == neededGold)
			return true;
		else if(gold >= plugin.config.getNeededGold()){
			DeityPlayer.sendErrorMessage("You have too much gold. I wouldn't mind taking it... But alas, I am only an entity :'(", p);
			return false;
		}else{
			DeityPlayer.sendErrorMessage("You do not have enough gold -_- Get moar nao! Omnomnomnom <3", p);
			return false;
		}
	}
	
	private void testSpawn(){
		if(plugin.needsSpawn){
			plugin.wm.createSpawn(plugin.config.getNetherWorldSpawn());
			plugin.needsSpawn = false;
		}
	}
	
}
