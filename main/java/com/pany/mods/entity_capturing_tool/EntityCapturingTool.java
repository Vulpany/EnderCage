package com.pany.mods.entity_capturing_tool;

import com.pany.mods.entity_capturing_tool.Helpers.CommandRegisterer;
import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCage;
import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCageEntity;
import com.pany.mods.entity_capturing_tool.entities.Unknownentity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class EntityCapturingTool implements ModInitializer {
    // ;
    public static final EnderCage EnderCageBlock = new EnderCage(FabricBlockSettings.create().luminance(6).strength(2f,600f).requiresTool() );
    public static final BlockEntityType<EnderCageEntity> EnderCageBlockEntity = Registry.register(Registries.BLOCK_ENTITY_TYPE,new Identifier("endercage","endercageentity"),
            FabricBlockEntityTypeBuilder.create(EnderCageEntity::new,EnderCageBlock).build());
    public static Item EnderCageItem = null;

    public static Identifier EnderCageIdentifier(String Path) {
        return new Identifier("endercage",Path);
    }

    public static EntityType<Unknownentity> UnknownEntityType = Registry.register(Registries.ENTITY_TYPE,EnderCageIdentifier("unknownentitydisplay"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, Unknownentity::new ).dimensions(EntityDimensions.fixed((1f/16)*8,1)).build()
    );

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK,EnderCageIdentifier("endercage"),EnderCageBlock);
        Registry.register(Registries.ITEM,EnderCageIdentifier("endercage"),new BlockItem(EnderCageBlock, new FabricItemSettings().maxCount(16).rarity(Rarity.EPIC) ));
        EnderCageItem = Item.fromBlock(EnderCageBlock);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            content.add(Item.fromBlock(EnderCageBlock));
        } );
        CommandRegisterer.RegisterCommands();
        FabricDefaultAttributeRegistry.register(UnknownEntityType, Unknownentity.createMobAttributes());

    }


}
