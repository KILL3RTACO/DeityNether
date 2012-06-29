package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deitynether.cmd.NetherCommand;
import com.imdeity.deitynether.listener.NetherWatcher;
import com.imdeity.deitynether.sql.NetherSQL;
import com.imdeity.deitynether.util.WorldManager;

public class DeityNether extends JavaPlugin{

	private File configFile = new File("plugins/DeityNether/config.yml"); //No need for File.pathSeparator, as '/' works with any OS
	public Config config = new Config(configFile);
	public WorldManager wm = new WorldManager(this);
	private NetherWatcher watcher = new NetherWatcher(this);
	public static  NetherSQL mysql = null;
	public static boolean hasError = false;
	public static final String HEADER = "§7[§c*ImDeity*§7]§f ";
	public static final String GENERAL_PERMISSION = "Deity.nether.general";
	public static final String OVERRIDE_PERMISSION = "Deity.nether.override";
	
	public void onDisable(){
		info("Disabled");
	}
	
	public void onEnable(){
		CommandExecutor executor = new NetherCommand(this);
		addDataFolders();
		getCommand("nether").setExecutor(executor);
		try {
			setup();
			info("Connected!");
		} catch (Exception e) {
			info("MySQL setup incorrectly, check the config.yml");
		}
		if(!hasError){
			getServer().getPluginManager().registerEvents(watcher, this);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, watcher, 60L, 1200L);
			checkNetherDeletionStatus();
		}
		info("Loading config...");
		config.loadDefaults();
		info("Enabled");
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
	
	public void broadcastMessage(String message){
		getServer().broadcastMessage(message);
	}
	
	private void checkNetherDeletionStatus(){
		//TODO check MySQL if world needs to be reset, set 'needsDeleting' as result
		boolean needsDeleting = false;
		if(needsDeleting){
			wm.deleteWorld(config.getNetherWorldName());
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
	
	private void setup() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
			mysql = new NetherSQL(this);
	}
	
}
