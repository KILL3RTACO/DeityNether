package com.imdeity.deitynether.obj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.NetherTime;

public class DeityOfflinePlayer implements OfflinePlayer {

	private OfflinePlayer p = null;
	private DeityNether plugin = null;
	private NetherTime nt = null;
	
	public DeityOfflinePlayer(OfflinePlayer player, DeityNether instance){
		plugin = instance;
		p = player;
		nt = new NetherTime(plugin);
	}
	
	public int getTimeWaited(){
		try {
			String name = getName();
			String sql = "SELECT `duration` FROM `nether-wait-times` WHERE `player`='" + name + "'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			if(rs.next()){
				return rs.getInt(1);
			}else{
				return nt.neededWaitTime;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public boolean hasEnteredNether(){
		try {
			String name = getName();
			String sql = "SELECT `id` FROM `nether-actions` WHERE `player`='" + name + "' AND `action`='join'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			if(!rs.next()){ //Row may not exist, but they also might've left and THAT row will exist
				sql = "SELECT `id` FROM `nether-actions` WHERE `player`='" + name + "' AND `action`='join'";
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
	
	public boolean isInNether(){
		try {
			String name = getName();
			String sql = "SELECT `id` FROM `nether-actions` WHERE `player`='" + name +"' AND `action`='join'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			return rs.next(); 
			//true means that this row exists and they are in nether, false means the row doesn't exist, therefore they haven't entered before
			//or the row with the action of 'leave' exists meaning they have entered before but left
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//----------------------------------------------------------------
	
	@Override
	public boolean isOp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setOp(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getBedSpawnLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getFirstPlayed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastPlayed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return p.getName();
	}

	@Override
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPlayedBefore() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBanned() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWhitelisted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBanned(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWhitelisted(boolean arg0) {
		// TODO Auto-generated method stub

	}

}
