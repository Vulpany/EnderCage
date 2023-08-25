package com.pany.mods.entity_capturing_tool.mixin;


import com.mojang.datafixers.DataFixer;
import com.pany.mods.entity_capturing_tool.Helpers.ConfigHelper;
import com.pany.mods.entity_capturing_tool.injectedinterfaces.confighandling;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.ApiServices;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class configloading implements confighandling {

    @Unique
    private ConfigHelper Config = null;

    @Override
    public ConfigHelper GetEndercageConfig() {
        return Config;
    }
    @Override
    public void GenerateConfig() {
        try {
            Config = new ConfigHelper();
        } catch (Exception e) {}
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void endercage$loadconfig(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        ( (MinecraftServer)(Object)this ).GenerateConfig();
    }

}
