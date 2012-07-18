package com.imdeity.deitynether.util;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;

public class WorldManager {

	private DeityNether plugin = null;
	
	public WorldManager(DeityNether instance){
		plugin = instance;
	}
	
	public boolean deleteWorld(String worldName){
		return deleteFilesInFolder(new File(worldName + "/"));
	}
	
	public void setNetherRegenStatus(Player p, boolean status){
		if(p.isOp()){
			plugin.config.setResetStatus(status);
			DeityPlayer.sendInfoMessage("%dNether reset status set to: %b" + status, p);
			plugin.info(p.getName() + " set the regeneration status of the nether to: " + status);
		}else{
			DeityPlayer.sendInvalidPermissionMessage(p);
		}
	}
	
	public void createSpawn(Location center){ //Corners 1 and 2 are for the airbox, 1 and 3 is the floor | 11 * 11 * 5 = 605 (plenty) 
		center = getNewCenter(center);
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
	
	private void cuboid(Location p1, Location p2, Material type){
		World world = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
		LocalWorld localWorld = new BukkitWorld(world);
		EditSession es = new EditSession(localWorld, 3000);	//3000 is the change limit
		Vector pos1 = new Vector().setX(p1.getBlockX()).setY(p1.getBlockY()).setZ(p1.getBlockZ());
		Vector pos2 = new Vector().setX(p2.getBlockX()).setY(p2.getBlockY()).setZ(p2.getBlockZ());
		CuboidRegion region = new CuboidRegion(localWorld, pos1, pos2);
		Pattern pattern = new SingleBlockPattern(new BaseBlock(type.getId()));
		try {
			es.setBlocks(region, pattern);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
	
	private Location getNewCenter(Location origin){
		World nether = plugin.getServer().getWorld(plugin.config.getNetherWorldName());
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
