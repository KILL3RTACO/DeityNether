package com.imdeity.deitynether.cmd;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.util.PlayerPorter;

public class JoinSubCommand {

	private PlayerPorter porter = new PlayerPorter();
	
	public JoinSubCommand(Player p){
		porter.sendToNether(p);
	}
	
}
