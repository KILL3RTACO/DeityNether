package com.imdeity.deitynether.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.NetherTime;
import com.imdeity.deitynether.util.PlayerPorter;

public class NetherCommand implements CommandExecutor{

	private DeityNether plugin = null;
	private PlayerPorter porter = null;
	private NetherTime nt = null;
	
	public NetherCommand(DeityNether instance){
		plugin = instance;
		porter = new PlayerPorter(plugin);
		nt = new NetherTime(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p;
		if(sender instanceof Player){
			p = (Player)sender;
			if(args.length == 0){
				//TODO what to do here?
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
					plugin.sendHelp(p);
				}else if(args[0].equalsIgnoreCase("info")){
					plugin.sendInfo(p);
				}else if(args[0].equalsIgnoreCase("join")){
					porter.sendToNether(p);
				}else if(args[0].equalsIgnoreCase("leave")){
					porter.sendToOverworld(p);
				}else if(args[0].equalsIgnoreCase("time")){
					nt.getTimeLeft(p);
				}else{
					p.sendMessage(DeityNether.HEADER + "§cInvalid argument(s), please use §f\"/nether ?\" §cfor help");
				}
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("regen")){
					if(args[1].equalsIgnoreCase("on"))
						plugin.wm.setNetherRegenStatus(p, true);
					else if(args[1].equalsIgnoreCase("off"))
						plugin.wm.setNetherRegenStatus(p, false);
					else
						p.sendMessage(DeityNether.HEADER + "§cInvalid argument(s), please use §f\"/nether ?\" §cfor help");
				}
			}
		}else{
			plugin.info("You must be logged in to do this, Deity ;)");
		}
		return true;
	}

	
	
}
