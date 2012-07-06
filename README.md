DeityNether
=============

Nether plugin for ImDeity: Kingdoms

Config values are used througout the plugin. This is so I can test different features of the plugin. Plus, I don't like hardcoding things, so config values help me on that part.

Commands: (/n is an alias for /nether)

/nether join - enter the nether
/nether leave - leave the nether
/nether time - see how much time you have left in the nether, or how much time you need to wait
/nether info - plugin information, relatively the same as /mail info
/nether ?/help - displays help

Admin command: /nether regen [on/off] - overrides the current reset status. If set to 'on' the nether will be deleted on the next restart.

An enum is used that stores all values of items that can be brought into the nether. This enum can be found at src/com/imdeity/deitynether/util/AllowedItems.java

Each item is organized into three groups: Food, Armour, and Tools. This is purely cosmetic and has no effect on the plugin whatsoever.

DeityPlayer and DeityOfflinePlayer are relatively the same as Player and OfflinePlayer, but with some extra methods.