package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config{

	YamlConfiguration config = null;
	File file = null;
	
	public Config(File file){
		config = YamlConfiguration.loadConfiguration(file);
		this.file = file;
	}
	
	public void loadDefaults(){
		if(!config.contains("nether.days-until-next-regen"))
			config.set("nether.days-until-next-regen", 7);
		if(!config.contains("nether.main-world"))
			config.set("nether.main-world", "world");
		if(!config.contains("nether-world"))
			config.set("nether.nether-world", "world_nether");
		if(!config.contains("nether.pigman-gold-drop-chance"))
			config.set("nether.pigman-gold-drop-chance", 10);
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
		return config.getInt("nether.days-until-next-regen");
	}
	
	public int getDropChance(){
		return config.getInt("nether.pigman-gold-drop-chance");
	}
	
	public String getMainWorldName(){
		return config.getString("nether.main-world");
	}
	
	public String getNetherWorldName(){
		return config.getString("nether.nether-world");
	}
	
}
