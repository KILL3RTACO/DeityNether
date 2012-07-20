package com.imdeity.deitynether;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deitynether.cmd.NetherCommand;
import com.imdeity.deitynether.listener.NetherWatcher;
import com.imdeity.deitynether.sql.NetherSQL;
import com.imdeity.deitynether.util.PlayerPorter;
import com.imdeity.deitynether.util.WorldManager;

public class DeityNether extends JavaPlugin{

	public static DeityNether plugin;
	public static Config config;
	public static Language lang;
	public static Server server;
	public static NetherSQL db;
	private NetherWatcher watcher;
	public WorldManager wm = new WorldManager();
	private boolean hasError = false;
	public boolean needsSpawn = false;
	public static final String GENERAL_PERMISSION = "Deity.nether.general";
	public static final String OVERRIDE_PERMISSION = "Deity.nether.override";
	
	public void onDisable(){
		for(Player p : server.getWorld(config.getNetherWorldName()).getPlayers()){
			PlayerPorter porter = new PlayerPorter();
			if(p.hasPermission(OVERRIDE_PERMISSION)){
				porter.sendToOverworld(p, false);
			}else if(p.hasPermission(GENERAL_PERMISSION)){
				porter.sendToOverworld(p,  true);
			}
		}
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
		boolean needsDeletion = config.getResetStatus();
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
		lang = new Language(new File(getDataFolder() + "/language.yml"));
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
		
		info("Loading language file...");
		lang.addDefaultValue("header", "&7[&c*ImDeity*&7]&f");
		lang.addDefaultValue("nether.join", "%header &aTeleporting you to the nether...");
		lang.addDefaultValue("nether.leave", "%header &aTeleporting you to the main world...");
		lang.addDefaultValue("nether.time.play.time_left", "%header &aYou have &2%numMin %numSec &aleft in the nether");
		lang.addDefaultValue("nether.time.play.override", "%header &2You can stay here as long as you want honey <3");
		lang.addDefaultValue("nether.time.play.perm_error", "%header %4o.O how did you get in here? I call h4x0r!");
		lang.addDefaultValue("nether.time.wait.time_left", "%header &aYou need to wait &2%numHour %numMin %numSec");
		lang.addDefaultValue("nether.time.wait.override", "%header &2You can visit anytime babe <3");
		lang.addDefaultValue("nether.time.wait.perm_error", "%header %4¡Qué Lastíma! You can't visit the nether");
		lang.addDefaultValue("nether.thanks", "%header &2So long, and thanks for all the fish! ^o^"); //I love that movie
		lang.addDefaultValue("nether.regen", "%header &dNether regen status set to: &b%bool");
		lang.addDefaultValue("nether.error.permissions", "%header &4You don't have permission to do that");
		lang.addDefaultValue("nether.error.arguments", "%header &4Invalid argument(s), use \"&f/nether ?&4\" for help");
		lang.addDefaultValue("nether.error.not_in_nether", "%header &4You aren't in the nether silly");
		lang.addDefaultValue("nether.error.need_to_wait", "%header &4Sorry you need to wait &6%numHour %numMin %numSec");
		lang.addDefaultValue("nether.error.already_in_nether", "%header &4You are already in the nether. Look around, it's quite ovbious");
		lang.addDefaultValue("nether.error.too_much_gold", "%header &4You have too much gold. I wouldn't mind taking it... But alas, I am only an entity :'(");
		lang.addDefaultValue("nether.error.too_little_gold", "%header &4You do not have enough gold -_- Get moar nao! Omnomnomnom <3");
		lang.addDefaultValue("nether.error.invalid_items", "%header &4You have items in your inventory that are not allowed in the Nether. " +
								"Remove them and try again");
		info("Saving language file...");
		lang.save();
	}
	
	public void info(String msg){
		Logger.getLogger("Minecraft").info("[DeityNether] " + msg);
	}
	
}
