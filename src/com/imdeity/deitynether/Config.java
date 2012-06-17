package com.imdeity.deitynether;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config{

	YamlConfiguration config = null;
	
	public Config(File file){
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void loadDefaults(){
		if(!config.contains("pigman-gold-drop-chance"))
			config.set("pigman-gold-drop-chance", 10);
		if(!config.contains("main-world"))
			config.set("main-world", "world");
		if(!config.contains("nether-world"))
			config.set("nether-world", "world_the_nether");
	}
	
	public int getDropChance(){
		return config.getInt("pigman-gold-drop-chance");
	}
	
	public String getMainWorldName(){
		return config.getString("main-world");
	}
	
	public String getNetherWorldName(){
		return config.getString("nether-world");
	}
	
}
