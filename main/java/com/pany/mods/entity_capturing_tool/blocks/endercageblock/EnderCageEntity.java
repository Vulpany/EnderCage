package com.pany.mods.entity_capturing_tool.blocks.endercageblock;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.Helpers.ConfigHelper;
import com.pany.mods.entity_capturing_tool.Helpers.ContainedObject;
import com.pany.mods.entity_capturing_tool.Helpers.ContainmentHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

import static com.pany.mods.entity_capturing_tool.Helpers.MathStuff.DiffMathRandom;

public class EnderCageEntity extends BlockEntity {
    public EnderCageEntity(BlockPos pos, BlockState state) {
        super(EntityCapturingTool.EnderCageBlockEntity, pos, state);
        this.ContainedEntity = new ContainedObject(this);
    }
    private final static String BlockEntityTag = "BlockEntityTag";
    public ContainedObject ContainedEntity;
    public boolean ContainedIsHostile = false;
    private boolean EntityIsSilent = false;
    private boolean Silent = false;
    public SoundEvent Sound = null;
    public boolean Powered = false;
    private boolean Dirty = false;
    public int ticks = (int)(Math.random()*300);

    @Override
    public void writeNbt(NbtCompound Nbt) {
        super.writeNbt(Nbt);
        Nbt.putBoolean(ContainmentHandler.Keys.SilentKey,Silent);
        if (this.ContainedEntity.ContainsEntity()) {
            Nbt.put(ContainmentHandler.Keys.EntityContained,this.ContainedEntity.ToNbt());
        }
    }

    @Override
    public void readNbt(NbtCompound Nbt) {
        super.readNbt(Nbt);
        if (Nbt.contains(ContainmentHandler.Keys.SilentKey)) {
            this.Silent = Nbt.getBoolean(ContainmentHandler.Keys.SilentKey);
        }
        if (Nbt.contains(ContainmentHandler.Keys.EntityContained)) {
            this.ContainedEntity.ReadData(Nbt);
        }
        this.UpdateGenericData();
        markDirty();
        if (this.getWorld() != null ) {
            this.SyncBlock();
        }
        this.Dirty = true;
        //this.DiffRandom = (float)(Math.random()*(Math.PI*2));
    }

    public void UpdateGenericData() {
        this.EntityIsSilent = false;
        if (this.ContainedEntity.ContainsEntity() ) {
            NbtCompound entitynbt = this.ContainedEntity.GetEntityNbt();
            if (entitynbt != null && entitynbt.contains("Silent")) {
                this.EntityIsSilent = entitynbt.getBoolean("Silent");
            }
        }
    }
    public ContainedObject GetContainedEntity() {
        return this.ContainedEntity;
    }

    public void RemoveContainedEntity() {
        this.ContainedEntity.FlushData();
    }
    public boolean HasEntityContained() {
        return this.ContainedEntity.ContainsEntity();
    }
    public boolean GetIfSilent() {
        return (this.Silent || this.ContainedEntity.GetIfSilent());
    }

    public void SyncBlock() {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            this.markDirty();
            this.Dirty = false;
            this.getWorld().getServer().getPlayerManager().sendToAll(toUpdatePacket());
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound Nbt = super.toInitialChunkDataNbt();
        if (this.Silent) {
            Nbt.putBoolean(ContainmentHandler.Keys.SilentKey,true);
        }
        Nbt.put(ContainmentHandler.Keys.EntityContained,this.ContainedEntity.ToNbtOrEmpty());
        return Nbt;
    }

    public void UpdateRedstoneState(boolean SetTo) {
        if (!ConfigHelper.RedstoneEnabled) {
            return;
        }
        if (!this.Powered && SetTo && this.getWorld() != null && !this.getWorld().isClient) {
            BlockPos pos = this.getPos();
            BlockState blockState = this.getWorld().getBlockState(pos.add(0,1,0));
            if (blockState.getBlock().equals(EntityCapturingTool.EnderCageBlock)) {
                EnderCageEntity cageEntity = (EnderCageEntity)this.getWorld().getBlockEntity(pos.add(0,1,0));
                if (!this.ContainedEntity.TransferTo(cageEntity.ContainedEntity,null)) {
                    boolean Success = cageEntity.ContainedEntity.TransferTo(this.ContainedEntity,null);
                    if (Success) {
                        this.getWorld().playSound(null,this.getPos(), SoundEvents.BLOCK_PISTON_CONTRACT,SoundCategory.BLOCKS,2,2);
                    }
                } else if ( !(this.ContainedEntity.ContainsEntity() && cageEntity.ContainedEntity.ContainsEntity()) ) {
                    this.getWorld().playSound(null,this.getPos(), SoundEvents.BLOCK_PISTON_EXTEND,SoundCategory.BLOCKS,2,2);
                }
            } else if (!blockState.isSolidBlock(this.getWorld(),pos.add(0,1,0))) {
                if (!this.ContainedEntity.ContainsEntity()) {
                    Predicate<MobEntity> entityPredicate = entity -> ContainmentHandler.IsContainmentAllowed(entity,this.getWorld());
                    List<MobEntity> Entities = this.getWorld().getEntitiesByClass(MobEntity.class,Box.of(pos.toCenterPos().add(0,1,0),1.1,1.1,1.1),entityPredicate);
                    if (Entities.size() > 0) {
                        boolean Success = this.ContainedEntity.CaptureEntity(Entities.get(0),this.getWorld(),null);
                        if (Success) {
                            this.getWorld().playSound(null,this.getPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN,SoundCategory.BLOCKS,2,1.5f);
                        }
                    }
                    //for (Entity entity : )
                } else {
                    Entity entity = this.ContainedEntity.ReleaseEntity(pos.getX()+0.5,pos.getY()+1.1,pos.getZ()+.5,this.getWorld(),null);
                    if (entity != null) {
                        this.getWorld().playSound(null,this.getPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN,SoundCategory.BLOCKS,2,1.75f);
                    }
                }
            }
            this.SyncBlock();
        }
        this.Powered = SetTo;
    }

    public void SetSilentProperty(boolean SetTo) {
        this.Silent = SetTo;
        this.SyncBlock();
    }

    public boolean GetSilentProperty() {
        return this.Silent;
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T cent) {
        if (cent instanceof EnderCageEntity cageEntity) {
            cageEntity.ticks++;
            if (cageEntity.getWorld() != null) {
                // Server Actions
                if (!cageEntity.getWorld().isClient) {
                    // Redstone update
                    cageEntity.UpdateRedstoneState(world.isReceivingRedstonePower(blockPos));
                    // Ambient Sound
                    if (ConfigHelper.AllowSounds && cageEntity.Sound != null && !cageEntity.GetIfSilent() && cageEntity.ticks%10 == 0 && Math.random() < 0.1) {
                        cageEntity.getWorld().playSound(null,cageEntity.getPos(),cageEntity.Sound, SoundCategory.BLOCKS,1f,1.25f);
                    }
                    // Get Sound & if entity is hostile
                    if (cageEntity.Dirty || (cageEntity.ticks % 10) == 0) {
                        cageEntity.Dirty = false;
                        Entity entity = cageEntity.ContainedEntity.GetBlankEntity(world);
                        if (entity != null) {
                            cageEntity.Sound = (entity instanceof MobEntity ? ((MobEntity) entity).GetAmbientPublic() : null );
                            cageEntity.ContainedIsHostile = entity instanceof HostileEntity;
                        } else {
                            cageEntity.ContainedIsHostile = false;
                            cageEntity.Sound = null;
                        }
                    }
                } else {
                    ClientWorld clientWorld = (ClientWorld)world;
                    if (cageEntity.ticks % 10 == 0) {
                        Vec3d pos = blockPos.toCenterPos();
                        Vec3d Offset = new Vec3d(DiffMathRandom(),DiffMathRandom(),DiffMathRandom());
                        float Multiplier = (float)Math.random()*3;
                        Vec3d SpawnAt = pos.add(Offset.multiply(Multiplier));
                        Vec3d Velocity = Offset.multiply(-Multiplier);
                        clientWorld.addParticle(ParticleTypes.PORTAL,SpawnAt.getX(),SpawnAt.getY(),SpawnAt.getZ(),-Velocity.getX(),-Velocity.getY(),-Velocity.getZ());
                        //clientWorld.addParticle(ParticleTypes.PORTAL,pos.getX()+Offset.getX()*Multiplier,pos.getY()+Offset.getY()*Multiplier,pos.getZ()+Offset.getZ()*Multiplier,-Offset.getX(),-Offset.getY()-Offset.getZ() );
                    }
                }
            }
        }
    }
}
