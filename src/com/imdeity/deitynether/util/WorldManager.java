package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class WorldManager {

	private DeityNether plugin = null;
	
	public WorldManager(DeityNether instance){
		plugin = instance;
	}
	
	@Deprecated
	public void generateNewNether(Player p){
		DeityPlayer player = new DeityPlayer(p, plugin);
		if(player.isOp()){
			deleteWorld(plugin.config.getNetherWorldName());
			//TODO regeneration code
		}else{
		}
		player.sendInvalidPermissionMessage();
		
	}
	
	public boolean deleteWorld(String worldName){
		return deleteFilesInFolder(new File(worldName + "/"));
	}
	
	public boolean getNetherRegenStatus(){
		//TODO return MySQL table value denoting the RegenStatus 
		return false;
	}
	
	public void setNetherRegenStatus(Player p, boolean status){
		DeityPlayer admin = new DeityPlayer(p, plugin);
		if(admin.isOp()){
			plugin.mysql.overrideResetStatus(status);
			admin.sendInfoMessage("%dNether reset status set to: %b" + status);
		}else{
			admin.sendInvalidPermissionMessage();
		}
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
