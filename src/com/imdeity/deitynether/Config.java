package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config{

	YamlConfiguration config = null;
	File file = null;
	
	public Config(File file){
		config = YamlConfiguration.loadConfiguration(file);
		this.file = file;
	}
	
	public void loadDefaults(){
		if(!config.contains("world.nether.days-until-next-regen"))
			config.set("world.nether.days-until-next-regen", 7);
		if(!config.contains("world.main.name"))
			config.set("world.main.name", "world");
		if(!config.contains("world.nether.name"))
			config.set("world.nether.name", "world_nether");
		if(!config.contains("world.nether.pigman-gold-drop-chance"))
			config.set("world.nether.pigman-gold-drop-chance", 10);
		if(!config.contains("spawn.main-world.x"))
			config.set("spawn.main.x", 100);
		if(!config.contains("spawn.main.y"))
			config.set("spawn.main.y", 64);
		if(!config.contains("spawn.main.z"))
			config.set("spawn.main.z", 100);
		if(!config.contains("spawn.nether.x"))
			config.set("spawn.nether.x", 100);
		if(!config.contains("spawn.nether.y"))
			config.set("spawn.nether.y", 64);
		if(!config.contains("spawn.nether.z"))
			config.set("spawn.nether.z", 100);
		save();
	}
	
	private void save(){
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getDaysUntilNextRegen(){
		return config.getInt("world.nether.days-until-next-regen");
	}
	
	public int getDropChance(){
		return config.getInt("world.nether.pigman-gold-drop-chance");
	}
	
	public String getMainWorldName(){
		return config.getString("world.main.name");
	}
	
	public Location getMainWorldSpawn(){
		int x = config.getInt("spawn.main.x");
		int y = config.getInt("spawn.main.y");
		int z = config.getInt("spawn.main.z");
		World world = Bukkit.getServer().getWorld(getMainWorldName());
		return new Location(world, x, y, z);
	}
	
	public Location getNetherWorldSpawn(){
		int x = config.getInt("spawn.nether.x");
		int y = config.getInt("spawn.nether.y");
		int z = config.getInt("spawn.nether.z");
		World world = Bukkit.getServer().getWorld(getNetherWorldName());
		return new Location(world, x, y, z);
	}
	
	public String getNetherWorldName(){
		return config.getString("world.nether.name");
	}
	
}
