package com.imdeity.deitynether.listener;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;

public class NetherWatcher implements Listener{

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
		}
	}
	
//	@EventHandler
//	public void onItemSpawn(ItemSpawnEvent event){
//		if(event.getEntityType() == EntityType.PIG_ZOMBIE)
//			event.setCancelled(true);
//	}
	
}
