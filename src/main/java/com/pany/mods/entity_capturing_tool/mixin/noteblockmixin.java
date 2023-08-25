package com.pany.mods.entity_capturing_tool.mixin;

import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCageEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NoteBlock.class)
public class noteblockmixin {
    @Redirect(method = "onSyncedBlockEvent",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V"))
    private void endercage$noteplay(World world, @Nullable PlayerEntity player, double x, double y, double z, RegistryEntry<SoundEvent> soundEventRegistryEntry, SoundCategory soundCategory, float volume, float pitch, long seed) {
        BlockEntity entity = world.getBlockEntity(new BlockPos( (int)(x-0.5),(int)(y-0.5-1),(int)(z-0.5) ));
        RegistryEntry<SoundEvent> sound = soundEventRegistryEntry;
        if (entity instanceof EnderCageEntity cageEntity) {
            SoundEvent soundEvent = cageEntity.Sound;
            if (soundEvent != null) {
                sound = Registries.SOUND_EVENT.getEntry(soundEvent);
            }
        }
        world.playSound(null, x, y, z, sound, SoundCategory.RECORDS, volume, pitch, seed);
    }
}
