package com.imdeity.deitynether.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
	
	private String header = format("&7[&c*ImDeity*&7]&f ", false);
	
	public void sendErrorMessage(String msg, Player p){
		p.sendMessage(format("&c" +  msg, true));
	}
	
	public void sendInfoMessage(String msg, Player p){
		p.sendMessage(format(msg, true));
	}

	public void sendThanksMessage(Player p) {
		p.sendMessage(format("&aThank you for visiting the nether you can come back in &4" + PlayerStats.getWaitTimeLeft(p), true));
	}
	
	public String format(String message, boolean addHeader){
		if(message.contains("&a"))						//letters
			message = message.replaceAll("&a", ChatColor.GREEN.toString());
		if(message.contains("&b"))
			message = message.replaceAll("&b", ChatColor.AQUA.toString());
		if(message.contains("&c"))
			message = message.replaceAll("&c", ChatColor.RED.toString());
		if(message.contains("&d"))
			message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
		if(message.contains("&e"))
			message = message.replaceAll("&e", ChatColor.YELLOW.toString());
		if(message.contains("&f"))
			message = message.replaceAll("&f", ChatColor.WHITE.toString());
		
		if(message.contains("&0"))						//numbers
			message = message.replaceAll("&0", ChatColor.BLACK.toString());
		if(message.contains("&1"))
			message = message.replaceAll("&1", ChatColor.DARK_BLUE.toString());
		if(message.contains("&2"))
			message = message.replaceAll("&2", ChatColor.DARK_GREEN.toString());
		if(message.contains("&3"))
			message = message.replaceAll("&3", ChatColor.DARK_AQUA.toString());
		if(message.contains("&4"))
			message = message.replaceAll("&4", ChatColor.DARK_RED.toString());
		if(message.contains("&5"))
			message = message.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
		if(message.contains("&6"))
			message = message.replaceAll("&6", ChatColor.GOLD.toString());
		if(message.contains("&7"))
			message = message.replaceAll("&7", ChatColor.GRAY.toString());
		if(message.contains("&8"))
			message = message.replaceAll("&8", ChatColor.DARK_GRAY.toString());
		if(message.contains("&9"))
			message = message.replaceAll("&9", ChatColor.BLUE.toString());

		if(addHeader)
			message = header + message;
		
		return message;
	}

	public void sendInvalidPermissionMessage(Player p) {
		p.sendMessage(format("&4You don't have permission to do that", true));
	}
}
