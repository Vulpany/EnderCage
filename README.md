# EnderCage
A Minecraft mod thats add a placeable cage that can capture entities.
Thats it. nothing more.


Features:

= General functionality:

-> Capture entities by rightclicking them with the cage.

-> Transfer entities between a placed cage and a cage in your hand by rightclicking them with it.

-> Entities inside the cage will play their ambient sound once in a while. You can mute a cage by rightclicking it with a feather and unmute it by rightclicking it with a noteblock.



= Redstone functionality:

-> If a cage is powered it will:

-->> Release the contained entity / capture the entity above if the block above is air.

-->> Steal from/give the contained entity to a cage above.

->  Noteblocks above cages will always play the ambient of the entity below, even if the cage is muted.

-> Comparators will output a redstone signal of 15 if the cage is filled and 0 if not.


= Misc:

-> If a filled endercage is placed in a itemframe it will just show the entitiy. If the itemframe is facing up the entity will stand normally.
