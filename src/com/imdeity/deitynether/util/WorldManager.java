package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
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
	
	public void createSpawn(Location center){ //Corners 1 and 2 are for the box, 1 and 3 is the floor
		//center = getNewCenter(center);
		int r = 10;
		int h = 5;
		World nether = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
		Location corner1 = new Location(nether, center.getBlockX() - r, center.getBlockY(), center.getBlockZ() - r);
		Location corner2 = new Location(nether, center.getBlockX() + r, center.getBlockY() + h, center.getBlockZ() + r);
		Location corner3 = new Location(nether, center.getBlockX() + r, center.getBlockY(), center.getBlockZ() + r);
		cuboid(corner1, corner2, Material.AIR);
		cuboid(corner1, corner3, Material.COBBLESTONE);
		plugin.config.setNetherSpawnY(center.getBlockY());
		plugin.info("Nether spawn height set to: " + center.getBlockY());
	}
	
	private void cuboid(Location bottom, Location top, Material type){
		for(int x = bottom.getBlockX(); x <= top.getBlockX(); x++){
			for(int y = bottom.getBlockY(); y <= top.getBlockY(); y++){
				for(int z = bottom.getBlockZ(); z <= top.getBlockZ(); z++){
					new Location(plugin.getServer().getWorld(plugin.config.getNetherWorldName()), x, y, z).getBlock().setType(type);
					plugin.info("Nether: " + x + " " + y + " " + z);
				}
			}
		}
	}
	
	private Location getNewCenter(Location origin){
		World nether = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
		for(int y = origin.getBlockY(); y <= 0; y--){
			Location center = new Location(nether, origin.getBlockX(), y, origin.getBlockY());
			if(NetherFloor.contains(center.getBlock().getTypeId())){
				return center;
			}
		}
		return origin;
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
