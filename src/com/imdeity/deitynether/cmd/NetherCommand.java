package com.imdeity.deitynether.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.ChatUtils;

public class NetherCommand implements CommandExecutor{

	private ChatUtils cu = new ChatUtils();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 0){
			if(sender instanceof Player){
				Player p = (Player)sender;
				String subcommand = args[0];
				if(subcommand.equalsIgnoreCase("join")){
					new JoinSubCommand(p);
				}else if(subcommand.equals("leave")){
					new LeaveSubCommand(p);
				}else if(subcommand.equalsIgnoreCase("time")){
					new TimeSubCommand(p);
				}else if(subcommand.equalsIgnoreCase("regen") && args.length > 1){
					if(p.isOp()){
						p.sendMessage(cu.format("&4You don't have permission to do that", true));
					}else{
						if(args[1].equalsIgnoreCase("on")){
							DeityNether.config.setResetStatus(true);
							p.sendMessage(cu.format("&dNether reset status set to: &3true", true));
							DeityNether.plugin.info(p.getName() + "set the nether deletion status to true. It will be deleted upon next restart");
						}else if(args[1].equalsIgnoreCase("off")){
							DeityNether.config.setResetStatus(false);
							p.sendMessage(cu.format("&dNether reset status set to: &3false", true));
							DeityNether.plugin.info(p.getName() + "set the nether deletion status to false");
						}
					}
				}else if(subcommand.equalsIgnoreCase("?") || subcommand.equalsIgnoreCase("help")){
					p.sendMessage("-----[DeityNether: Help]-----");
					p.sendMessage(cu.format("&3/nether join&7: &bEnter the nether", false));
					p.sendMessage(cu.format("&3/nether leave&7: &bLeave the nether", false));
					p.sendMessage(cu.format("&3/nether time&7: &bSee how much time you have left", false));
					if(p.isOp()) p.sendMessage(cu.format("&3/nether regen <on/off>&7: &bSet the deletion status of the nether", false));
					p.sendMessage(cu.format("&3/nether ?&7: &bDisplays this menu", false));
				}
			}
		}
		return true;
	}
	
}
