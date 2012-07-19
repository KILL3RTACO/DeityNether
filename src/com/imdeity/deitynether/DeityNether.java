package com.imdeity.deitynether;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deitynether.cmd.NetherCommand;
import com.imdeity.deitynether.listener.NetherWatcher;
import com.imdeity.deitynether.sql.NetherSQL;
import com.imdeity.deitynether.util.WorldManager;

public class DeityNether extends JavaPlugin{

	public static DeityNether plugin;
	public static Config config;
	public static Server server;
	public static NetherSQL db;
	private NetherWatcher watcher;
	public WorldManager wm = new WorldManager();
	private boolean hasError = false;
	public boolean needsSpawn = false;
	public static final String GENERAL_PERMISSION = "Deity.nether.general";
	public static final String OVERRIDE_PERMISSION = "Deity.nether.override";
	
	public void onDisable(){
		info("Disabled");
	}
	
	public void onEnable(){
		//TODO check if nether needs deleting (set needsSpawn to true if so)
		//TODO delete nether if needed
		plugin = this;
		server = this.getServer();
		getCommand("nether").setExecutor(new NetherCommand());
		initConfig();
		try {
			initDatabase();
			info("[MySQL] Connect to server");
		} catch (Exception e) {
			hasError = true;
			info("[MySQL] Failed to connect. Did you check the config.yml?");
		}
		if(!hasError){
			watcher = new NetherWatcher();
			server.getPluginManager().registerEvents(watcher, plugin);
			server.getScheduler().scheduleSyncRepeatingTask(plugin, watcher, 0, 20);
		}
		checkNetherDeletion();
	}
	
	private void checkNetherDeletion() {
		boolean needsDeletion = DeityNether.config.getResetStatus();
		if(needsDeletion){
			info("Deleting Nether...");
			DeityNether.plugin.wm.deleteWorld(DeityNether.config.getNetherWorldName());
			info("Nether Deleted! Spawn will be create when the first player enters the nether");
			config.setNetherSpawnY(64);
			config.setLastReset();
			needsSpawn = true;
		}else{
			info("Nether doesn't need to be deleted");
		}
	}

	private void initDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		info("[MySQL] Loading MySQL database...");
		db = new NetherSQL();
	}
	
	private void initConfig(){
		info("Loading config...");
		config = new Config(new File(getDataFolder() + "/config.yml"));
		config.addDefaultConfigValue("world.nether.regeneration-interval-in-days", 7);
		config.addDefaultConfigValue("world.nether.needs-regeneration", true);
		config.addDefaultConfigValue("world.nether.wait-time-in-hours", 12);
		config.addDefaultConfigValue("world.nether.name", "world_nether");
		config.addDefaultConfigValue("world.nether.pigman-gold-drop-chance", 10);
		config.addDefaultConfigValue("world.nether.gold-blocks-needed", 1);
		config.addDefaultConfigValue("world.nether.last-reset", new Timestamp(System.currentTimeMillis()).toString());
		config.addDefaultConfigValue("world.main.name", "world");
		config.addDefaultConfigValue("spawn.nether.x", 0);
		config.addDefaultConfigValue("spawn.nether.y", 64);
		config.addDefaultConfigValue("spawn.nether.z", 0);						//MySQL options
		config.addDefaultConfigValue("mysql.database.name", "kingdoms");
		config.addDefaultConfigValue("mysql.database.username", "root");
		config.addDefaultConfigValue("mysql.database.password", "root");
		config.addDefaultConfigValue("mysql.server.address", "localhost");
		config.addDefaultConfigValue("mysql.server.port", 3306);
		info("Saving config...");
		config.save();
	}
	
	public void info(String msg){
		Logger.getLogger("Minecraft").info("[DeityNether] " + msg);
	}
	
}
