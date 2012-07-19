package com.imdeity.deitynether.util;

import com.imdeity.deitynether.DeityNether;

public class NetherTime {

	public static final int NEEDED_WAIT_TIME = DeityNether.config.getWaitTime();
	
	public static String formatTime(int mins, int secs){
		if(secs == 0){
			return mins + "m";
		}else if(mins == 0){
			return secs + "s";
		}else if(secs == 0 && mins == 0){
			return "no time";
		}else{
			return mins + "m " + secs + "s ";
		}
	}
	
}
