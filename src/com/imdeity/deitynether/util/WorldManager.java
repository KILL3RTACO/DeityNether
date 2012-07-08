package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class WorldManager {

	private DeityNether plugin = null;
	
	public WorldManager(DeityNether instance){
		plugin = instance;
	}
	
	public boolean deleteWorld(String worldName){
		return deleteFilesInFolder(new File(worldName + "/"));
	}
	
	public void setNetherRegenStatus(Player p, boolean status){
		DeityPlayer admin = new DeityPlayer(p, plugin);
		if(p.isOp()){
			plugin.config.setResetStatus(status);
			admin.sendInfoMessage("%dNether reset status set to: %b" + status);
			plugin.info(p.getName() + " set the regeneration status of the nether to: " + status);
		}else{
			admin.sendInvalidPermissionMessage();
		}
	}
	
	public void createSpawn(){ //Corners 1 and 2 are for the box, 1 and 3 is the floor
		int r = 10;
		World nether = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
		Location center = plugin.config.getNetherWorldSpawn();
		Location corner1 = new Location(nether, center.getBlockX() + r, center.getBlockY(), center.getBlockZ() + r);
		Location corner2 = new Location(nether, center.getBlockX() - r, center.getBlockY() + (2 * r), center.getBlockZ() - r);
		Location corner3 = new Location(nether, center.getBlockX() - r, center.getBlockY(), center.getBlockZ() - r);
		
	}
	
	private boolean deleteFilesInFolder(File folder){
		for(File f : folder.listFiles()){
			if(f.isFile()){
				f.delete();
			}else if (f.isDirectory()){
				deleteFilesInFolder(f);
			}
		}
		
		return folder.delete();
	}
	
}
