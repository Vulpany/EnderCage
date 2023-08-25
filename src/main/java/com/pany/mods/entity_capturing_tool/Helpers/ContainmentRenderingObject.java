package com.pany.mods.entity_capturing_tool.Helpers;

import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCageEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ContainmentRenderingObject {
    public CageAnimation CageAnim = new CageAnimation();
    private Entity RenderEntity = null;
    private EntityType<?> RenderEntityType = null;
    private NbtCompound MobData = null;
    private String MobId = null;
    private NbtCompound MobNbt = null;
    private Object BoundTo = null;
    private BoundType BoundObjType = BoundType.Null;
    public boolean Dirty = false;
    public int ticks = 0;
    private static final List<String> NbtNotToInclude = new ArrayList<>();

    public ContainmentRenderingObject() {
        this.ticks = (int)(Math.random()*1000);
    }

    public ContainmentRenderingObject(NbtCompound nbtCompound) {
        this.ReadData(nbtCompound);
        this.ticks = (int)(Math.random()*1000);
    }

    public ContainmentRenderingObject(ItemStack stack) {
        this.SetBoundTo(stack);
        this.ReadData(stack);
        this.ticks = (int)(Math.random()*1000);
    }

    public ContainmentRenderingObject(EnderCageEntity cageEntity) {
        this.SetBoundTo(cageEntity);
        this.ReadData(cageEntity.ContainedEntity.ToNbtOrEmpty());
        this.ticks = (int)(Math.random()*1000);
    }

    public void ReadData(ItemStack stack) {
        ReadData(BlockItem.getBlockEntityNbt(stack));
    }
    public void ReadData(ContainedObject containedObject) {
        this.MobId = containedObject.GetMobType();
        this.SetNbt(containedObject.GetEntityNbt());
        this.MobData = containedObject.ToNbtOrEmpty();
        this.SetBoundTo(containedObject.GetBoundObject());
    }
    public void ReadData(NbtCompound nbt) {
        nbt = ContainmentHandler.GetThisOrContainedNbt(nbt);
        if (nbt != null) {
            if (nbt.contains(ContainmentHandler.Keys.MobType)) {
                this.MobId = nbt.getString(ContainmentHandler.Keys.MobType);
                if (GetEntityType(this.MobId) == null) {
                    this.FlushData();
                    return;
                }
                if (nbt.contains(ContainmentHandler.Keys.MobNbt)) {
                    this.SetNbt(nbt.getCompound(ContainmentHandler.Keys.MobNbt));
                }
                this.MobData = nbt;
                this.Dirty = true;
            }
        }
    }
    public String GetMobId() {
        return this.MobId;
    }

    public void FlushData() {
        this.RenderEntity = null;
        this.MobNbt = null;
        this.MobId = null;
        this.MobData = null;
        this.RenderEntityType = null;
        this.Dirty = true;
    }
    public void SetBoundTo(Object obj) {
        this.BoundTo = obj;
        this.BoundObjType = obj instanceof ItemStack ? BoundType.Item : obj instanceof EnderCageEntity ? BoundType.Block : BoundType.Null;
    }




    private boolean ContainsEntity() {
        return GetEntityType(this.MobId) != null;
    }

    public Entity GetRenderEntity(World world) {
        if (world == null) {
            return null;
        }
        ticks++;
        if (this.BoundObjType == BoundType.Item) {
            ItemStack stack = ((ItemStack)this.BoundTo);
            if (stack.GetIfRenderDirty()) {
                this.Dirty = true;
                stack.SetIfRenderDirty(false);
            }
        }
        Entity CurrentRenderEntity = this.RenderEntity;
        if ( (ticks % 10) == 0 || this.Dirty   ) {
            if ( (this.ContainsEntity() == (CurrentRenderEntity == null ) || this.ContainsEntity() && CurrentRenderEntity != null && GetEntityType(this.MobId) != this.RenderEntityType  ) ) {
                CurrentRenderEntity = GetEntity(this.MobId,world);
            }
            if (CurrentRenderEntity != null) {
                CurrentRenderEntity.readNbt(this.MobNbt);
            }
        }
        if (CurrentRenderEntity != null) {
            CurrentRenderEntity.setPitch(0);
            CurrentRenderEntity.setYaw(0);
            CurrentRenderEntity.setHeadYaw(0);
        }
        this.RenderEntity = CurrentRenderEntity;
        this.RenderEntityType = CurrentRenderEntity != null ? CurrentRenderEntity.getType() : null;
        return CurrentRenderEntity;
    }


    public void SetNbt(NbtCompound nbt) {
        if (nbt != null) {
            for (String avoided : NbtNotToInclude) {
                if (nbt.contains(avoided)) {
                    nbt.remove(avoided);
                }
            }
        }
        this.MobNbt = nbt;
        this.Dirty = true;
    }
    private static EntityType GetEntityType(String id) {
        try {
            return Registries.ENTITY_TYPE.get(new Identifier(id));
        } catch (Exception ignored) {}
        return null;
    }
    private static Entity GetEntity(String id, World world) {
        EntityType type = GetEntityType(id);
        if (type != null) {
            try {
                return type.create(world);
            } catch (Exception ignored) {}
        }
        return null;
    }
    static {
        NbtNotToInclude.add("Rotation");
        NbtNotToInclude.add("Position");
        NbtNotToInclude.add("Motion");
    }
    public static class CageAnimation {
        public Entity RenderEntity = null;
        public float LastEntityYaw = 0;
        public float AnimProgress = 0;
    }
    private static enum BoundType {
        Item,
        Block,
        Null
    }
}
