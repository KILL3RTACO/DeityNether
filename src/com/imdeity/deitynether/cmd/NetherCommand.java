package com.imdeity.deitynether.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.PlayerPorter;
import com.imdeity.deitynether.util.WorldManager;

public class NetherCommand implements CommandExecutor{

	private DeityNether plugin = null;
	private PlayerPorter porter = null;
	
	public NetherCommand(DeityNether instance){
		plugin = instance;
		porter = new PlayerPorter(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p;
		if(sender instanceof Player){
			p = (Player)sender;
			if(args.length == 0){
				porter.sendToNether(p);
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("regen")){
					testNetherRegen();
				}
			}else{
				
			}
		}else{
			plugin.info("You must be logged in to do this, Deity ;)");
		}
		return true;
	}
	
	private void testNetherRegen(){
		WorldManager manager = new WorldManager(plugin);
		manager.generateNewNether();
	}

	
	
}
