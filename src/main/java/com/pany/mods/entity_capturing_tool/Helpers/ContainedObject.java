package com.pany.mods.entity_capturing_tool.Helpers;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCageEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ContainedObject {
    private NbtCompound MobData = null;
    private String MobId = null;
    private NbtCompound MobNbt = null;
    private int UniqueId = -1;
    private boolean SilentEntity = false;
    private Object BoundTo = null;
    public ContainmentRenderingObject Renderer = null;

    public ContainedObject() {
        this.Renderer = new ContainmentRenderingObject();
    }


    public ContainedObject(EnderCageEntity BindTo) {
        this.Renderer = new ContainmentRenderingObject();
        this.BoundTo = BindTo;
    }

    public ContainedObject(NbtCompound Data) {
        this.Renderer = new ContainmentRenderingObject();
        this.ReadData(Data);
    }

    public ContainedObject(ItemStack stack) {
        this.Renderer = new ContainmentRenderingObject();
        this.ReadData(stack);
        this.BoundTo = stack;
    }

    public void ReadData(ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt == null) {
            nbt = new NbtCompound();
        }
        this.ReadData(nbt);
    }

    public void ReadData(NbtCompound Data) {
        Data = ContainmentHandler.GetThisOrContainedNbt(Data);
        boolean FlushHappened = false;
        if (Data != null && Data.contains(ContainmentHandler.Keys.MobType)) {
            this.MobId = Data.getString(ContainmentHandler.Keys.MobType);
            if (Data.contains(ContainmentHandler.Keys.MobNbt)) {
                this.MobNbt = ContainmentHandler.CleanMobNbt(Data.getCompound(ContainmentHandler.Keys.MobNbt));
            } else {
                this.MobNbt = null;
            }
            if (Data.contains(ContainmentHandler.Keys.UniqueKey)) {
                this.UniqueId = Data.getInt(ContainmentHandler.Keys.UniqueKey);
            } else {
                this.UniqueId = (int)Math.round(Math.random()*50000);
                Data.putInt(ContainmentHandler.Keys.UniqueKey,this.UniqueId);
            }
            this.MobData = Data;
        } else {
            FlushHappened = true;
            this.FlushData();
            Data = null;
        }
        if (!FlushHappened) {
            if (this.BoundTo instanceof ItemStack stack) {
                NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
                if (nbt == null) {
                    nbt = new NbtCompound();
                }
                nbt.put(ContainmentHandler.Keys.EntityContained,this.MobData);
                BlockItem.setBlockEntityNbt(stack,EntityCapturingTool.EnderCageBlockEntity,nbt);
            } else if (this.BoundTo instanceof EnderCageEntity cageEntity) {
                cageEntity.UpdateGenericData();
                cageEntity.SyncBlock();
            }
        }
        if (this.MobNbt != null) {
            this.SilentEntity = this.MobNbt.contains("Silent") && this.MobNbt.getBoolean("Silent");
        }
        if (this.BoundTo instanceof ItemStack stack) {
            stack.SetIfRenderDirty(false);
        }
        this.Renderer.ReadData(this);
    }

    public EntityType<?> GetEntityType() {
        if (this.MobId == null) {
            return null;
        }
        EntityType<?> type = null;
        try {
            type = Registries.ENTITY_TYPE.get(new Identifier(this.MobId));
        } catch (Exception ignored) {}
        return type;
    }

    public Entity GetBlankEntity(World world) {
        if (this.MobId == null) {
            return null;
        }
        Entity entity = null;
        EntityType<?> Type = this.GetEntityType();
        if (Type != null) {
            entity = Type.create(world);
        }
        return entity;
    }

    public boolean ContainsEntity() {
        if (this.MobId == null || this.GetEntityType() == null) {
            if (this.MobId != null && this.GetEntityType() == null) {
                this.FlushData();
            }
            return false;
        }
        return true;
    }

    @Nullable
    public NbtCompound GetEntityNbt() {
        return this.MobNbt;
    }

    @Nullable
    public String GetMobType() {
        return this.MobId;
    }

    public void SetBoundObject(Object bindto) {
        this.BoundTo = bindto;
    }


    public Object GetBoundObject() {
        return this.BoundTo;
    }

    public <WriteTo> boolean CaptureEntity(Entity entity, World world, @Nullable PlayerEntity player) {
        if (world != null && !world.isClient && entity != null && this.BoundTo != null && !this.ContainsEntity() && ContainmentHandler.IsContainmentAllowed(entity,world)) {
            NbtCompound nbt = ContainmentHandler.EntityToNbt(entity);
            if (nbt != null) {
                entity.discard();
                if (this.BoundTo instanceof ItemStack) {
                    this.TrySplitThenRead(player,nbt);
                } else {
                    this.ReadData(nbt);
                }
                if (this.BoundTo instanceof EnderCageEntity cageEntity) {
                    cageEntity.UpdateGenericData();
                    cageEntity.SyncBlock();
                }
                return true;
            }
        }
        return false;
    }

    public <WriteTo> Entity ReleaseEntity(double X,double Y, double Z,World world,@Nullable PlayerEntity player) {
        if (world != null && !world.isClient) {
            Entity entity = this.GetBlankEntity(world);
            if (entity == null) {
                return null;
            }
            if (this.MobNbt != null) {
                entity.readNbt(this.MobNbt);
            }
            entity.setPos(X,Y,Z);
            world.spawnEntity(entity);
            this.FlushData();
            if (this.BoundTo instanceof EnderCageEntity cageEntity) {
                cageEntity.UpdateGenericData();
                cageEntity.SyncBlock();
            }
            return entity;
        }
        return null;
    }

    public boolean TrySplitThenRead(PlayerEntity player,NbtCompound nbt) {
        ItemStack stack = (ItemStack)this.BoundTo;
        boolean NewStack = false;
        if (stack.getCount() > 1) {
            ItemStack split = stack.split(1);
            this.SetBoundObject(split);
            NewStack = true;
        }
        this.ReadData(nbt);
        if (NewStack) {
            player.getInventory().insertStack( (ItemStack)this.BoundTo );
        }
        return NewStack;
    }

    public boolean GetIfSilent() {
        return this.SilentEntity;
    }


    public <WriteTo> Entity ReleaseEntity(Vec3d pos,World world,@Nullable PlayerEntity player) {
        return this.ReleaseEntity(pos.getX(),pos.getY(),pos.getZ(),world,player);
    }

    public void FlushData() {
        this.MobId = null;
        this.MobNbt = null;
        this.UniqueId = -1;
        this.MobData = null;
        if (this.BoundTo instanceof ItemStack stack) {
            NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
            if (nbt == null) {
                nbt = new NbtCompound();
            }
            if (nbt.contains(ContainmentHandler.Keys.EntityContained)) {
                nbt.remove(ContainmentHandler.Keys.EntityContained);
            }
            BlockItem.setBlockEntityNbt(stack,EntityCapturingTool.EnderCageBlockEntity,nbt);
        } else if (this.BoundTo instanceof EnderCageEntity cageEntity) {
            cageEntity.UpdateGenericData();
            cageEntity.SyncBlock();
        }
    }

    public Entity GetRenderEntity(World world) {
        return this.Renderer.GetRenderEntity(world);
    }

    /*
    public Entity GetRenderEntity(World world) {
        if (this.BoundTo instanceof ItemStack stack) {
            if (stack.GetIfRenderDirty()) {
                this.ReadData(stack);
            }
        }
        Entity entity = this.CageAnim.RenderEntity;
        this.CageAnim.ticks++;
        if (entity == null || this.CageAnim.ticks % 5 == 0 && this.GetEntityType() != entity.getType() ) {
            entity = this.GetBlankEntity(world);
            if (this.MobNbt != null) {
                entity.readNbt(this.MobNbt);
            }
        } else if (this.CageAnim.ticks % 5 == 0) {
            if (this.GetEntityType() != null) {
                if (this.MobNbt != null) {
                    NbtCompound div = this.MobNbt.copy();
                    if (div.contains("Rotation")) {
                        div.remove("Rotation");
                    }
                    entity.readNbt(div);
                } else {
                    entity.readNbt(new NbtCompound());
                }
            } else {
                this.FlushData();
            }
        }
        this.CageAnim.RenderEntity = entity;
        return entity;
    }

     */

    @Nullable
    public NbtCompound ToNbt() {
        if (!this.ContainsEntity()) {
            return null;
        }
        NbtCompound nbt = new NbtCompound();
        nbt.putString(ContainmentHandler.Keys.MobType, this.MobId);
        nbt.put(ContainmentHandler.Keys.MobNbt, this.MobNbt == null ? new NbtCompound() : this.MobNbt);
        nbt.putInt(ContainmentHandler.Keys.UniqueKey,this.UniqueId);
        return nbt;
    }

    public NbtCompound ToNbtOrEmpty() {
        NbtCompound Return = this.ToNbt();
        if (Return == null) {
            Return = new NbtCompound();
            Return.putBoolean(ContainmentHandler.Keys.IsEmpty,true);
        }
        return Return;
    }

    public boolean TransferTo(ContainedObject object,@Nullable PlayerEntity player) {
        if (!object.ContainsEntity() && this.ContainsEntity()) {
            if (object.BoundTo instanceof ItemStack) {
                boolean Success = object.TrySplitThenRead(player,this.ToNbtOrEmpty());
            } else {
                object.ReadData(this.ToNbtOrEmpty());
            }
            this.FlushData();
            return true;
        }
        return false;
    }
}
