package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deitynether.cmd.NetherCommand;
import com.imdeity.deitynether.listener.NetherWatcher;
import com.imdeity.deitynether.obj.DeityPlayer;
import com.imdeity.deitynether.sql.NetherSQL;
import com.imdeity.deitynether.util.WorldManager;

public class DeityNether extends JavaPlugin{

	private File configFile = new File("plugins/DeityNether/config.yml"); //No need for File.pathSeparator, as '/' works with any OS
	public Config config = new Config(configFile);
	public WorldManager wm = new WorldManager(this);
	public NetherWatcher watcher = new NetherWatcher(this);
	public NetherSQL mysql = null;
	public static boolean hasError = false;
	public static final String HEADER = "�7[�c*ImDeity*�7]�f ";
	public static final String GENERAL_PERMISSION = "Deity.nether.general";
	public static final String OVERRIDE_PERMISSION = "Deity.nether.override";
	
	public void onDisable(){
		info("Disabled");
	}
	
	public void onEnable(){
		CommandExecutor executor = new NetherCommand(this);
		addDataFolders();
		getCommand("nether").setExecutor(executor);
		info("Loading config...");
		config.loadDefaults();
		try {
			setup();
			info("Connected!");
		} catch (Exception e) {
			info("MySQL setup incorrectly, check the config.yml");
		}
		if(!hasError){
			getServer().getPluginManager().registerEvents(watcher, this);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, watcher, 0L, 20L); //No delay, repeats every 20 ticks (1 second)
			checkNetherDeletionStatus();
		}
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
	
	public void updateDeleteStatus(boolean status){
		if(status){
			
		}else{
			
		}
	}
	
	private void checkNetherDeletionStatus(){
		boolean needsDeleting = mysql.getResetStatus();
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
	
	public void sendHelp(Player p){
		DeityPlayer player = new DeityPlayer(p, this);
		player.sendPluginHelp();
	}
	
	public void sendInfo(Player p){
		DeityPlayer player = new DeityPlayer(p, this);
		player.sendPluginInformation();
		
	}
	
}
