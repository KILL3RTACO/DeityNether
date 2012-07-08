package com.imdeity.deitynether.util;

public enum NetherFloor {

	FLOOR(new int[]{86, 87, 88, 89, 112, 113, 114, 115});
	
	private int[] ids;
	
	private NetherFloor(int[] ids){
		this.ids = ids;
	}
	
	private int[] getIds(){
		return this.ids;
	}
	
	public static boolean contains(int id){
		for(NetherFloor group : NetherFloor.values()){
			for(int i : group.getIds()){
				if(id == i) return true;
			}
		}
		return false;
	}
}
