package com.imdeity.deitynether.listener;

import java.sql.Timestamp;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.PlayerPorter;
import com.imdeity.deitynether.util.PlayerStats;

public class NetherWatcher implements Listener, Runnable{

	private static final int NEEDED_WAIT_TIME = DeityNether.config.getWaitTime();
	private PlayerPorter porter = null;
	
	public NetherWatcher(){
		porter = new PlayerPorter();
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		ItemStack drop = new ItemStack(Material.GOLD_NUGGET, 1);
		Entity entity = event.getEntity();
		int chance = DeityNether.config.getDropChance();
		if(entity instanceof PigZombie && entity.getWorld().getEnvironment() == Environment.NETHER){
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH));
			int rn = (int)Math.random() * ((100 - chance) + 1) + chance;
			if(rn < chance){
				event.getDrops().add(drop);
			}
		}else if(entity instanceof Player){
			PlayerStats.setLeaveTime((Player)event.getEntity(), true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(player.getWorld() == DeityNether.plugin.getServer().getWorld(DeityNether.config.getNetherWorldName()))
			if(!player.hasPermission(DeityNether.OVERRIDE_PERMISSION) && player.hasPermission(DeityNether.GENERAL_PERMISSION)){
				porter.sendToOverworld(player, true);
			}else if(player.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
				porter.sendToOverworld(player, false);
			}
	}

	@Override
	public void run() {
		for(Player p : DeityNether.plugin.getServer().getOnlinePlayers()){
			if(p.getWorld() == DeityNether.plugin.getServer().getWorld(DeityNether.config.getNetherWorldName())){
				if(!p.hasPermission(DeityNether.OVERRIDE_PERMISSION) && p.hasPermission(DeityNether.GENERAL_PERMISSION)){
					PlayerStats.addTime(p);
					checkPlayer(p);
				}
			}else{
				if(PlayerStats.getTimeWaited(p) != NEEDED_WAIT_TIME)
					PlayerStats.addWaitTime(p);
			}
		}
		
		for(OfflinePlayer p : DeityNether.plugin.getServer().getOfflinePlayers()){
				PlayerStats.addWaitTime(p);
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp nextReset = DeityNether.config.getNextReset();
		if(now.after(nextReset) || now.equals(nextReset)){
			DeityNether.config.setResetStatus(true);
		}
	}
	
	private void checkPlayer(Player p){
		if(!p.hasPermission(DeityNether.OVERRIDE_PERMISSION) && p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(PlayerStats.getTimeInNether(p) == 3600){//Their time is up
				p.sendMessage(DeityNether.lang.formatThanks());
				p.teleport(DeityNether.config.getMainWorldSpawn());
			}
		}
	}
	
}
