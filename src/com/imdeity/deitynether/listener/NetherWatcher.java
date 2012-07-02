package com.imdeity.deitynether.listener;

import java.sql.Timestamp;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class NetherWatcher implements Listener, Runnable{

	private DeityNether plugin = null;
	
	public NetherWatcher(DeityNether instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		ItemStack drop = new ItemStack(Material.GOLD_NUGGET, 1);
		Entity entity = event.getEntity();
		int chance = plugin.config.getDropChance();
		if(entity instanceof PigZombie && entity.getWorld().getEnvironment() == Environment.NETHER){
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH));
			int rn = (int)Math.random() * ((100 - chance) + 1) + chance;
			if(rn < chance){
				event.getDrops().add(drop);
//				plugin.getServer().getWorld(plugin.config.getNetherWorldName()).dropItem(entity.getLocation(), drop);
			}
		}else if(entity instanceof Player){
			//TODO set MySql so they can't come back
		}
	}

	@Override
	public void run() {
//		for(Player p : playerJoins.keySet()){
//			playerDurations.put(p, playerDurations.get(p) + 1); //adds one minute to their duration
//			
//		}
		for(Player p : plugin.getServer().getWorld(plugin.config.getNetherWorldName()).getPlayers()){
			DeityPlayer player = new DeityPlayer(p, plugin);
			plugin.mysql.addTime(player);
			if(player.getTimeInNether() == 3600){
				player.sendThanksMessage();
				player.teleport(plugin.config.getMainWorldSpawn());
			}
		}
		//Check players
	}
	
//	@EventHandler
//	public void onItemSpawn(ItemSpawnEvent event){
//		if(event.getEntityType() == EntityType.PIG_ZOMBIE)
//			event.setCancelled(true);
//	}
	
}
