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
	
	public void addDefaultConfigValue(String path, Object value){
		if(!config.contains(path)){
			if(value instanceof String)
				config.set(path, (String)value);
			else config.set(path, value);
		}
	}
	
	public void save(){
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
		return DeityNether.server.getWorld(getMainWorldName()).getSpawnLocation();
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
	
	public Timestamp getNextReset(){
		Timestamp nextReset = Timestamp.valueOf(getLastReset());
		nextReset.setTime(nextReset.getTime() + getRegenerationInterval() * 24 * 60 * 60 * 1000);
		return nextReset;
	}
	
	public void setLastReset(){
		config.set("world.nether.last-reset", new Timestamp(System.currentTimeMillis()).toString());
		config.set("world.nether.needs-regeneration", false);
		save();
	}

	public World getNetherWorld() {
		return DeityNether.server.getWorld(getNetherWorldName());
	}
	
}
