package com.imdeity.deitynether.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;

public class NetherCommand implements CommandExecutor{

	private DeityNether plugin = null;
	
	public NetherCommand(DeityNether instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p;
		if(sender instanceof Player){
			p = (Player)sender;
			
		}else{
			plugin.info("You must be logged in to do this, Deity ;)");
		}
		return true;
	}

	
	
}
