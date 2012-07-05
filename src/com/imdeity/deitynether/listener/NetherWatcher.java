package com.imdeity.deitynether.listener;

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
import com.imdeity.deitynether.obj.DeityOfflinePlayer;
import com.imdeity.deitynether.obj.DeityPlayer;
import com.imdeity.deitynether.util.NetherTime;
import com.imdeity.deitynether.util.PlayerPorter;

public class NetherWatcher implements Listener, Runnable{

	private DeityNether plugin = null;
	private NetherTime nt = null;
	private PlayerPorter porter = null;
	
	public NetherWatcher(DeityNether instance){
		plugin = instance;
		nt = new NetherTime(plugin);
		porter = new PlayerPorter(plugin);
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
			}
		}else if(entity instanceof Player){
			plugin.mysql.setLeaveTime(new DeityPlayer((Player)event.getEntity(), plugin), true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(player.getWorld() == plugin.getServer().getWorld(plugin.config.getNetherWorldName()))
			porter.sendToOverworld(player);
	}

	@Override
	public void run() {
		for(Player p : plugin.getServer().getOnlinePlayers()){
			DeityPlayer player = new DeityPlayer(p, plugin);
			if(p.getWorld() == plugin.getServer().getWorld(plugin.config.getNetherWorldName())){
				plugin.mysql.addTime(player);
				checkPlayer(player);
			}else{
				if(player.getTimeWaited() != nt.neededWaitTime)
					plugin.mysql.addWaitTime(player);
			}
		}
		
		for(OfflinePlayer p : plugin.getServer().getOfflinePlayers()){
			DeityOfflinePlayer player = new DeityOfflinePlayer(p, plugin);
				plugin.mysql.addWaitTime(player);
		}
		
		plugin.checkNetherResetStatus();
	}
	
	private void checkPlayer(DeityPlayer player){
		if(player.getTimeInNether() == 3600){			//Their is up
			player.sendThanksMessage();					//"Thank you for entering the nether you may again in <config-value> hours
			player.teleport(plugin.config.getMainWorldSpawn());
		}
	}
	
}
