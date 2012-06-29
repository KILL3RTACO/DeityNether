package com.imdeity.deitynether.util;

import com.imdeity.deitynether.DeityNether;

public class ChatUtils {
	
	public String format(String message, boolean addHeader){
		if(message.contains("%a"))						//letters
			message = message.replaceAll("%a", "§a");
		if(message.contains("%b"))
			message = message.replaceAll("%b", "§b");
		if(message.contains("%c"))
			message = message.replaceAll("%c", "§c");
		if(message.contains("%d"))
			message = message.replaceAll("%d", "§d");
		if(message.contains("%e"))
			message = message.replaceAll("%e", "§e");
		if(message.contains("%f"))
			message = message.replaceAll("%f", "§f");
		
		if(message.contains("%0"))						//numbers
			message = message.replaceAll("%0", "§0");
		if(message.contains("%1"))
			message = message.replaceAll("%1", "§1");
		if(message.contains("%2"))
			message = message.replaceAll("%2", "§2");
		if(message.contains("%3"))
			message = message.replaceAll("%3", "§3");
		if(message.contains("%4"))
			message = message.replaceAll("%4", "§4");
		if(message.contains("%5"))
			message = message.replaceAll("%5", "§5");
		if(message.contains("%6"))
			message = message.replaceAll("%6", "§6");
		if(message.contains("%7"))
			message = message.replaceAll("%7", "§7");
		if(message.contains("%8"))
			message = message.replaceAll("%8", "§8");
		if(message.contains("%9"))
			message = message.replaceAll("%9", "§9");

		if(addHeader)
			message = DeityNether.HEADER + message;
		
		return message;
	}
	
}
