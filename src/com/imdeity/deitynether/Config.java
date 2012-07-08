package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

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
		if(!config.contains("world.regneration-interval-in-days"))			//World options
			config.set("world.nether.regeneration-interval-in-days", 7);
		if(!config.contains("world.nether.needs-regeneration"))
			config.set("world.nether.needs-regeneration", false);
		if(!config.contains("world.nether.wait-time-in-hours"))
			config.set("world.nether.wait-time-in-hours", 12);
		if(!config.contains("world.nether.name"))
			config.set("world.nether.name", "world_nether");
		if(!config.contains("world.nether.pigman-gold-drop-chance"))
			config.set("world.nether.pigman-gold-drop-chance", 10);
		if(!config.contains("world.nether.gold-blocks-need"))
			config.set("world.nether.gold-blocks-needed", 1);
		if(!config.contains("world.nether.last-reset"))
			config.set("world.nether.last-reset", new Timestamp(System.currentTimeMillis()).toString());
		if(!config.contains("world.main.name"))
			config.set("world.main.name", "world");
		if(!config.contains("spawn.nether.x"))
			config.set("spawn.nether.x", 0);
		if(!config.contains("spawn.nether.y"))
			config.set("spawn.nether.y", 64);
		if(!config.contains("spawn.nether.z"))
			config.set("spawn.nether.z", 0);
		if(!config.contains("mysql.database.name"))							//MySQL options
			config.set("mysql.database.name", "kingdoms");
		if(!config.contains("mysql.database.username"))
			config.set("mysql.database.username", "root");
		if(!config.contains("mysql.database.password"))
			config.set("mysql.database.password", "root");
		if(!config.contains("mysql.server.address"))
			config.set("mysql.server.address", "localhost");
		if(!config.contains("mysql.server.port"))
			config.set("mysql.server.port", 3306);
		save();
	}
	
	private void save(){
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getRegenerationInterval(){
		return getInt("world.nether.regeneration-interval-in-days");
	}
	
	public int getDropChance(){
		return getInt("world.nether.pigman-gold-drop-chance");
	}
	
	public String getMainWorldName(){
		return getString("world.main.name");
	}
	
	public int getNeededGold(){
		return getInt("world.nether.gold-blocks-needed");
	}
	
	public String getMySqlDatabaseName(){
		return getString("mysql.database.name");
	}
	
	public String getMySqlDatabaseUsername(){
		return getString("mysql.database.username");
	}
	
	public String getMySqlDatabasePassword(){
		return getString("mysql.database.password");
	}
	
	public String getMySqlServerAddress(){
		return getString("mysql.server.address");
	}
	
	public int getMySqlServerPort(){
		return getInt("mysql.server.port");
	}
	
	public Location getNetherWorldSpawn(){
		int x = getInt("spawn.nether.x");
		int y = getInt("spawn.nether.y");
		int z = getInt("spawn.nether.z");
		World world = Bukkit.getServer().getWorld(getNetherWorldName());
		return new Location(world, x, y, z);
	}
	
	public Location getMainWorldSpawn(){
		return Bukkit.getServer().getWorld(getMainWorldName()).getSpawnLocation();
	}
	
	public boolean getResetStatus(){
		return getBoolean("world.nether.needs-regeneration");
	}
	
	public int getWaitTime(){
		return getInt("world.nether.wait-time-in-hours") * 3600;
	}
	
	public String getNetherWorldName(){
		return getString("world.nether.name");
	}
	
	private boolean getBoolean(String path){
		return config.getBoolean(path);
	}
	
	private String getString(String path){
		return config.getString(path);
	}
	
	private int getInt(String path){
		return config.getInt(path);
	}
	
	public void setNetherSpawnY(int y){
		config.set("spawn.nether.y", y);
		save();
	}
	
	public void setResetStatus(boolean status){
		config.set("world.nether.needs-regeneration", status);
		save();
	}
	
	public String getLastReset(){
		return getString("world.nether.last-reset");
	}
	
	public void setLastReset(){
		config.set("world.nether.last-reset", new Timestamp(System.currentTimeMillis()).toString());
		config.set("world.nether.needs-regeneration", false);
		save();
	}
	
}
