package com.imdeity.deitynether.obj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.ChatUtils;
import com.imdeity.deitynether.util.NetherTime;

public class DeityPlayer{

	private Player p = null;
	private ChatUtils cu = new ChatUtils();
	private DeityNether plugin = null;
	private NetherTime nt = null;
	
	public DeityPlayer(Player player, DeityNether instance){
		p = player;
		plugin = instance;
		nt = new NetherTime(plugin);
	}
	
	public Timestamp getLastLeave(){
		try {
			String name = getName();
			String sql = "SELECT `time` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='leave'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getTimeInNether(){
		try {
			String name = getName();
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
	
	public int getTimeWaited(){
		try {
			String name = getName();
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
	
	public boolean hasEnteredNether(){
		try {
			String name = getName();
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
	
	public boolean hasWaited(){
		int waited = getTimeWaited();
		if(nt.neededWaitTime - waited == 0) return true;
		else return false;
	}
	
	public boolean hasTimeLeft(){
		return 3600 - getTimeInNether() != 0;
	}
	
	public void sendErrorMessage(String message){
		sendMessage(cu.format("%c" + message, true));
	}
	
	public void sendInfoMessage(String message){
		sendMessage(cu.format(message, true));
	}
	
	public void sendInvalidPermissionMessage(){
		sendErrorMessage("You don't have permission");
	}
	
	public void sendPluginHelp(){ //TODO add colour
		sendMessage(cu.format("%3-----[%bDeityNether Help%3]-----", false));
		sendMessage(cu.format("/nether [join | leave] - Join or leave the nether", false));
		sendMessage(cu.format("/nether time - See how much time you have left", false));
		if(isOp())
			sendMessage(cu.format("/nether regen [on | off] - Override the regeneration status for the nether", false)); //TODO tell console who reset status
		sendMessage(cu.format("/nether info - Plugin information", false));
	}
	
	public void sendPluginInformation(){
		sendMessage(cu.format("%3-----[%bDeityNether Information%3]-----", false));
		sendMessage(cu.format("%3#%0-%b###%0-", false));
		sendMessage(cu.format("%0--%b#%0--%b#     %3Developed by: %bKILL3RTACO", false));
		sendMessage(cu.format("%3#%0-%b#%0--%b#", false));
		sendMessage(cu.format("%3#%0-%b#%0--%b#", false));
		sendMessage(cu.format("%3#%0-%b###%0-", false));
	}
	
	public void sendThanksMessage(){
		sendMessage(cu.format("%3Thank you for visiting the nether, you may return in %b" + (nt.neededWaitTime / 3600) + " hours", true));
	}
//----------------------------------------------------------------------------------

	public String getName() {
		return p.getName();
	}

	public World getWorld() {
		return p.getWorld();
	}

	public boolean teleport(Location arg0) {
		return p.teleport(arg0);
	}

	public boolean hasPermission(String permission){
		return p.hasPermission(permission);
	}
	
	public Inventory getInventory(){
		return p.getInventory();
	}
	
	public boolean isOp() {
		return p.isOp();
	}

	
	public void sendMessage(String arg0) {
		p.sendMessage(arg0);
		
	}

}
