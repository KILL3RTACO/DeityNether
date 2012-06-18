package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;

public class WorldManager {

	private WorldCreator currentNether = null;
	private WorldCreator newNether = null;
	private DeityNether plugin = null;
	
	public WorldManager(DeityNether instance){
		plugin = instance;
		currentNether = new WorldCreator(plugin.config.getNetherWorldName());
		newNether = new WorldCreator(plugin.config.getNetherWorldName());
	}
	
	public void generateNewNether(){
		unloadNether();
		File file = new File(plugin.config.getNetherWorldName() + "/");
		if(file.exists()){
			
		}
		
		newNether.environment(Environment.NETHER).generateStructures(true).createWorld();
		
	}
	
	private void unloadNether(){
		World world = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
		World main = plugin.getServer().getWorld(plugin.config.getMainWorldName());
		for(Player p : world.getPlayers()){
			p.sendMessage(DeityNether.HEADER + "Teleporting you to main world for Nether reset...");
			p.teleport(main.getSpawnLocation());
		}
	}
	
}
