DeityNether
=============

Nether plugin for ImDeity: Kingdoms

Config values are used througout the plugin. This is so I can test different features of the plugin. Plus, I don't like hardcoding things, so config values help me on that part.

Enter the nether - /nether join
Leave the nether - /nether leave

An enum is used that stores all values of items that can be brought into the nether. This enum can be found at src/com/imdeity/deitynether/util/AllowedItems.java

Each item is organized into three groups: Food, Armour, and Tools. This is purely cosmetic and has no effect on the plugin whatsoever.

A note about DeityPlayer:
   Yes, there is a lot of code in there that is implemented from the 'Player' interface. Using Eclipse's handy little 'add all unimplemented methods' tool, i did just that and 
   edited only the methods that are needed (eg. getInventory() or sendMessage()), to save time.
   So you may see some methods say 'return null' or 'return 0'