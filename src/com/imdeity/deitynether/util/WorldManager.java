package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class WorldManager {

	private WorldCreator newNether = null;
	private DeityNether plugin = null;
	
	public WorldManager(DeityNether instance){
		plugin = instance;
		newNether = new WorldCreator(plugin.config.getNetherWorldName());
	}
	
	public void generateNewNether(Player player){
		if(player.isOp()){
			deleteWorld(plugin.config.getNetherWorldName());
			//TODO regeneration code
		}else{
		}
		player.sendMessage(DeityNether.HEADER + "§cYou don't have permission");
		
	}
	
	public boolean deleteWorld(String worldName){
		unloadNether();
		return deleteFilesInFolder(new File(worldName + "/"));
	}
	
	private void unloadNether(){
		World nether = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
		Location ms = plugin.config.getMainWorldSpawn();
		for(Player p : nether.getPlayers()){
			DeityPlayer player = new DeityPlayer(p);
			player.sendInfoMessage("Teleporting you to main world for Nether reset...");
			player.teleport(ms);
		}
//		plugin.getServer().unloadWorld(nether, false);
	}
	
	private boolean deleteFilesInFolder(File folder){
		for(File f : folder.listFiles()){
			if(f.isFile()){
				f.delete();
			}else if (f.isDirectory()){
				deleteFilesInFolder(f);
			}
		}
		
		return folder.delete();
	}
	
}
