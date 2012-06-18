package com.imdeity.deitynether.util;

public enum AllowedItems {

	//267-279
	
	IRON_SHOVEL(256),
	IRON_PICKAXE(257),
	IRON_AXE(258),
	APPLE(260),
	BOW(261),
	ARROW(262),
	IRON_SWORD(267),
	WOODEN_SWORD(268),
	WOODEN_SHOVEL(269),
	WOODEN_PICKAXE(270),
	WOODEN_AXE(271),
	STONE_SWORD(272),
	STONE_SHOVEL(273),
	STONE_PICKAXE(2754),
	STONE_AXE(275),
	DIAMOND_SWORD(276),
	DIAMOND_SHOVEL(280),
	DIAMOND_PICKAXE(281),
	DIAMOND_AXE(282);
	
	private int id;
	
	private AllowedItems(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
}
