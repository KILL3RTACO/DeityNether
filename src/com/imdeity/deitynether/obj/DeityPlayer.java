package com.imdeity.deitynether.obj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.ChatUtils;
import com.imdeity.deitynether.util.NetherTime;

public class DeityPlayer{

	private static ChatUtils cu = new ChatUtils();
	
	public static Timestamp getLastLeave(Player p, DeityNether plugin){
		try {
			String name = p.getName();
			String sql = "SELECT `time` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='leave'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getTimeInNether(Player p, DeityNether plugin){
		try {
			String name = p.getName();
			String sql = "SELECT `time_in_nether` FROM `nether_stats` WHERE `player`='" + name + "'";
			ResultSet rs  = plugin.mysql.getResultSet(sql);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static int getTimeWaited(Player p, DeityNether plugin){
		try {
			NetherTime nt = new NetherTime(plugin);
			String name = p.getName();
			String sql = "SELECT `time_waited` FROM `nether_stats` WHERE `player`='" + name + "'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			if(rs.next()){
				return rs.getInt(1);
			}else{ //Row does not exist, they haven't entered nether before, therefore they don't need to wait
				return nt.neededWaitTime;
			}
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static boolean hasEnteredNether(Player p, DeityNether plugin){
		try {
			String name = p.getName();
			String sql = "SELECT `id` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='join'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			if(!rs.next()){ //Row may not exist, but they also might've left and THAT row will exist
				sql = "SELECT `id` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='join'";
				rs = plugin.mysql.getResultSet(sql);
				if(rs.next()) //Row exists, this player has entered nether, but left
					return true;
				else //Row doesn't exist, this player hasn't entered the nether before
					return false;
			}else{ //They are in nether, therefore they have entered before
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean hasWaited(Player p, DeityNether plugin){
		int waited = getTimeWaited(p, plugin);
		NetherTime nt = new NetherTime(plugin);
		if(nt.neededWaitTime - waited == 0) return true;
		else return false;
	}
	
	public static boolean hasTimeLeft(Player p, DeityNether plugin){
		return 3600 - getTimeInNether(p, plugin) != 0;
	}
	
	public static void sendErrorMessage(String message, Player p){
		p.sendMessage(cu.format("%c" + message, true));
	}
	
	public static void sendInfoMessage(String message, Player p){
		p.sendMessage(cu.format(message, true));
	}
	
	public static void sendInvalidPermissionMessage(Player p){
		sendErrorMessage("You don't have permission", p);
	}
	
	public static void sendPluginHelp(Player p){ //TODO add colour
		p.sendMessage(cu.format("%3-----[%bDeityNether Help%3]-----", false));
		p.sendMessage(cu.format("/nether [join | leave] - Join or leave the nether", false));
		p.sendMessage(cu.format("/nether time - See how much time you have left", false));
		if(p.isOp())
			p.sendMessage(cu.format("/nether regen [on | off] - Override the regeneration status for the nether", false));
		p.sendMessage(cu.format("/nether info - Plugin information", false));
	}
	
	public static void sendPluginInformation(Player p){
		p.sendMessage(cu.format("%3-----[%bDeityNether Information%3]-----", false));
		p.sendMessage(cu.format("%3#%0-%b###%0-", false));
		p.sendMessage(cu.format("%0--%b#%0--%b#     %3Developed by: %bKILL3RTACO", false));
		p.sendMessage(cu.format("%3#%0-%b#%0--%b#", false));
		p.sendMessage(cu.format("%3#%0-%b#%0--%b#", false));
		p.sendMessage(cu.format("%3#%0-%b###%0-", false));
	}
	
	public static void sendThanksMessage(Player p, DeityNether plugin){
		NetherTime nt = new NetherTime(plugin);
		p.sendMessage(cu.format("%3Thank you for visiting the nether, you may return in %b" + (nt.neededWaitTime / 3600) + " hours", true));
	}


}
