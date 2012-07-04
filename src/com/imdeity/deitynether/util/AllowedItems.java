package com.imdeity.deitynether.util;

public enum AllowedItems {

	ARMOUR(new int[]{86, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319}),
	FOOD(new int[]{260, 282, 297, 319, 320, 322, 350, 357, 360, 363, 364, 365, 366, 367}),
	TOOLS(new int[]{256, 257, 258, 261, 262, });
	
	private int[] ids;
	
	private AllowedItems(int[] ids){
		this.ids = ids;
	}
	
	public int[] getIds(){
		return this.ids;
	}
	
	public static boolean contains(int id){
		for(AllowedItems item : AllowedItems.values()){
			for(int i : item.getIds()){
				if(id == i) return true;
			}
		}
		return false;
	}
	
}