package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deitynether.cmd.NetherCommand;
import com.imdeity.deitynether.listener.NetherWatcher;

public class DeityNether extends JavaPlugin{

	private File configFile = new File("plugins/DeityNether/config.yml"); //No need for File.pathSeparator, as '/' works with any OS
	public Config config = new Config(configFile);
	public static String HEADER = "§7[§c*ImDeity§7]§f ";
	public static String GENERAL_PERMISSION = "Deity.nether.general";
	public static String OVERRIDE_PERMISSION = "Deity.nether.override";
	
	public void onDisable(){
		
	}
	
	public void onEnable(){
		CommandExecutor executor = new NetherCommand(this);
		addDataFolders();
		getCommand("nether").setExecutor(executor);
		getServer().getPluginManager().registerEvents(new NetherWatcher(this), this);
		info("Loading config...");
		config.loadDefaults();
	}
	
	public void info(String message){
		Logger.getLogger("Minecraft").info("[DeityNether] " + message);
	}
	
	private void addDataFolders(){
		try{
			createDirs(configFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void createDirs(File file) throws IOException{	//Code taken from my own plugin, Admins
		if(!file.exists()){
			
			info("Cannot find /" + file.getPath().substring(20) +", creating new file...");
			if(file.getParentFile() != null)
				file.getParentFile().mkdirs();
			if(file.getPath().endsWith(".txt") || file.getPath().endsWith(".yml")){
				file.createNewFile();
			}else{
				file.mkdir();
			}
		}
	}
	
}
