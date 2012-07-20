package com.imdeity.deitynether;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.util.ChatUtils;

public class Language {

	private ChatUtils cu = new ChatUtils();
	YamlConfiguration lang = null;
	File file = null;
	
	public Language(File file){
		lang = YamlConfiguration.loadConfiguration(file);
		this.file = file;
	}

	public void addDefaultValue(String path, Object value){
		if(!lang.contains(path)){
			if(value instanceof String)
				lang.set(path, (String)value);
			else lang.set(path, value);
		}
	}
	
	public void save(){
		try {
			lang.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getHeader(){
		return cu.format(lang.getString("header"));
	}
	
	public String formatJoinMessage(){
		return cu.format(lang.getString("nether.join").replaceAll("%header", getHeader()));
	}
	
	public String formatLeaveMessage(){
		return cu.format(lang.getString("nether.leave").replaceAll("%header", getHeader()));
	}
	
	public String formatInvalidArguments(){
		return cu.format(lang.getString("nether.error.arguments").replaceAll("%header", getHeader()));
	}
	
	public String formatInvalidPermissions(){
		return cu.format(lang.getString("nether.error.permissions").replaceAll("%header", getHeader()));
	}
	
	public String formatTooMuchGold(){
		return cu.format(lang.getString("nether.error.too_much_gold").replaceAll("%header", getHeader()));
	}
	
	public String formatTooLittleGold(){
		return cu.format(lang.getString("nether.error.too_little_gold").replaceAll("%header", getHeader()));
	}
	
	public String formatInvalidItems(){
		return cu.format(lang.getString("nether.error.invalid_items").replaceAll("%header", getHeader()));
	}
	
	public String formatAlreadyInNether(){
		return cu.format(lang.getString("nether.error.already_in_nether").replaceAll("%header", getHeader()));
	}
	
	public String formatNotInNether(){
		return cu.format(lang.getString("nether.error.not_in_nether").replaceAll("%header", getHeader()));
	}
	
	public String formatPlayTimeLeft(Player p, int min, int sec){
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			return cu.format(lang.getString("nether.time.play.override").replaceAll("%header", getHeader()));
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			String m = "";
			String s = "";
			if(min != 0)
				m = min + "m";
			if(sec != 0)
				s = sec + "s";
			return cu.format(lang.getString("nether.time.play.time_left").replaceAll("%header", getHeader())
					.replaceAll("%numMin", m)
					.replaceAll("%numSec", s));
		}else{
			return cu.format(lang.getString("nether.time.play.perm_error").replaceAll("%header", getHeader()));
		}
	}
	
	public String formatWaitTimeLeft(Player p, int hour, int min, int sec){
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			return cu.format(lang.getString("nether.time.wait.override").replaceAll("%header", getHeader()));
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			String h = "";
			String m = "";
			String s = "";
			if(hour != 0)
				h = hour + "h";
			if(min != 0)
				m = min + "m";
			if(sec != 0)
				s = sec + "s";
			return cu.format(lang.getString("nether.time.wait.time_left").replaceAll("%header", getHeader()))
					.replaceAll("%numHour", h)
					.replaceAll("%numMin", m)
					.replaceAll("%numSec", s);
		}else{
			return cu.format(lang.getString("nether.time.wait.perm_error").replaceAll("%header", getHeader()));
		}
	}
	
	public String formatThanks(){
		return cu.format(lang.getString("nether.thanks").replaceAll("%header", getHeader()));
	}
	
	public String formatRegenStatus(boolean status){
		return cu.format(lang.getString("nether.regen").replaceAll("%header", getHeader())
				.replaceAll("%bool", "" + status));
	}

	public String formatNeedToWait(int hour, int min, int sec) {
		String h = "";
		String m = "";
		String s = "";
		if(hour != 0)
			h = hour + "h";
		if(min != 0)
			m = min + "m";
		if(sec != 0)
			s = sec + "s";
		return cu.format(lang.getString("nether.error.need_to_wait").replaceAll("%header", getHeader()))
				.replaceAll("%numHour", h)
				.replaceAll("%numMin", m)
				.replaceAll("%numSec", s);
		
	}
	
}
