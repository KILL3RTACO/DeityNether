package com.imdeity.deitynether;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.imdeity.deitynether.util.ChatUtils;

public class Language {

	private ChatUtils cu = new ChatUtils();
	YamlConfiguration lang = null;
	File file = null;
	
	public Language(File file){
		lang = YamlConfiguration.loadConfiguration(file);
		this.file = file;
	}

	public void addDefaultValue(String path, Object value){
		if(!lang.contains(path)){
			if(value instanceof String)
				lang.set(path, (String)value);
			else lang.set(path, value);
		}
	}
	
}
