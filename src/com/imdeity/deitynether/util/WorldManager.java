package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;

public class WorldManager {

	
	public boolean deleteWorld(String worldName){
		return deleteFilesInFolder(new File(worldName + "/"));
	}
	
	public void setNetherRegenStatus(Player p, boolean status){
		if(p.isOp()){
			DeityNether.config.setResetStatus(status);
			p.sendMessage(DeityNether.lang.formatRegenStatus(status));
			DeityNether.plugin.info(p.getName() + " set the regeneration status of the nether to: " + status);
		}else{
			p.sendMessage(DeityNether.lang.formatInvalidPermissions());
		}
	}
	
	public void createSpawn(Location center){ //Corners 1 and 2 are for the airbox, 1 and 3 is the floor | 11 * 11 * 5 = 605 (plenty) 
		center = getNewCenter(center);
		int r = 10;
		int h = 5;
		World nether = DeityNether.server.getWorld(DeityNether.config.getNetherWorldName());
		Location corner1 = new Location(nether, center.getBlockX() - r, center.getBlockY(), center.getBlockZ() - r);
		Location corner2 = new Location(nether, center.getBlockX() + r, center.getBlockY() + h, center.getBlockZ() + r);
		Location corner3 = new Location(nether, center.getBlockX() + r, center.getBlockY(), center.getBlockZ() + r);
		cuboid(corner1, corner2, Material.AIR);
		cuboid(corner1, corner3, Material.COBBLESTONE);
		DeityNether.config.setNetherSpawnY(center.getBlockY());
		DeityNether.plugin.info("Nether spawn height set to: " + center.getBlockY());
	}
	
	private void cuboid(Location p1, Location p2, Material type){
		World world = DeityNether.server.getWorld(DeityNether.config.getNetherWorldName());
		for(int x = p1.getBlockX(); x <= p2.getBlockX(); x++){
			for(int y = p1.getBlockY(); x <= p2.getBlockY(); y++){
				for(int z = p1.getBlockZ(); x <= p2.getBlockZ(); z++){
					new Location(world, x, y, z).getBlock().setType(type);
				}
			}
		}
	}
	
	private Location getNewCenter(Location origin){
		World nether = DeityNether.server.getWorld(DeityNether.config.getNetherWorldName());
		if(origin.getBlock().getType() != Material.AIR){
			for(int y = 127; y <= 0; y--){
				Location center = new Location(nether, origin.getBlockX(), y, origin.getBlockY());
				if(center.getBlock().getType() == Material.AIR){
					return new Location(nether, origin.getBlockX(), y -2 , origin.getBlockX());
				}
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
