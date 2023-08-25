package com.pany.mods.entity_capturing_tool.Helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContainmentHandler {

    public static record Keys() {
        public final static String EntityContained = "containedentity";
        public final static String MobType = "mobid";
        public final static String MobNbt = "mobnbt";
        public final static String UniqueKey = "specialmobsavingkey";
        public final static String SilentKey = "IsSilent";
        public final static String IsEmpty = "NoMobDataPresent";
    }

    public static class StackHelper {

    }
    public static class BlockHelper {

    }

    // Returns the entity data compound or null if this nbt doesn't have it or the given nbt is null
    public static NbtCompound GetThisOrContainedNbt(@Nullable NbtCompound nbt) {
        return nbt == null ? null : nbt.contains(Keys.EntityContained) && !nbt.getCompound(Keys.EntityContained).contains(Keys.IsEmpty) ? nbt.getCompound(Keys.EntityContained) : nbt.contains(Keys.MobType) && !nbt.contains(Keys.IsEmpty) ? nbt : null;
    }

    // Gets entity without the nbt data stored
    public static Entity GetEmptyEntity(NbtCompound nbtCompound) {
        NbtCompound SearchIn = GetThisOrContainedNbt(nbtCompound);
        Entity entity = null;
        if (SearchIn != null) {

        }
        return entity;
    }

    public static boolean IsContainmentAllowed(Entity entity, World world) {
        if (entity == null) {
            return false;
        }
        ConfigHelper Config = world.getServer().GetEndercageConfig();
        if (entity instanceof PlayerEntity || !(entity instanceof MobEntity) || !Config.HostilesAllowed && (entity instanceof HostileEntity)) {
            return false;
        }
        if (Config.BannedEntities.contains(entity.getType())) {
            return false;
        }
        return true;
    }

    private static <Any> Object[] EasyMobNbtInstance(String Name) {
        Object[] ObjList = new Object[3];
        ObjList[0] = Name;
        ObjList[1] = null;
        return ObjList;
    }

    private static <Any> Object[] EasyMobNbtInstance(String Name,Any Default) {
        Object[] ObjList = new Object[2];
        ObjList[0] = Name;
        ObjList[1] = Default;
        return ObjList;
    }

    public static NbtCompound CleanMobNbt(NbtCompound nbtCompound) {
        List<Object[]> CheckedNbts = new ArrayList<>();
        CheckedNbts.add(EasyMobNbtInstance("FallFlying"));
        CheckedNbts.add(EasyMobNbtInstance("Motion"));
        CheckedNbts.add(EasyMobNbtInstance("FallDistance"));
        CheckedNbts.add(EasyMobNbtInstance("OnGround"));
        CheckedNbts.add(EasyMobNbtInstance("Pos"));
        CheckedNbts.add(EasyMobNbtInstance("Air"));
        CheckedNbts.add(EasyMobNbtInstance("PortalCooldown"));
        CheckedNbts.add(EasyMobNbtInstance("UUID"));
        // With default values
        CheckedNbts.add(EasyMobNbtInstance("Invulnerable",false));
        CheckedNbts.add(EasyMobNbtInstance("InLove",0));
        CheckedNbts.add(EasyMobNbtInstance("PersistenceRequired",false));
        CheckedNbts.add(EasyMobNbtInstance("Age",0));
        CheckedNbts.add(EasyMobNbtInstance("HurtTime",(short)0));
        CheckedNbts.add(EasyMobNbtInstance("ForcedAge",0));
        CheckedNbts.add(EasyMobNbtInstance("AbsorptionAmount",(float)0) );
        CheckedNbts.add(EasyMobNbtInstance("LeftHanded",false));
        CheckedNbts.add(EasyMobNbtInstance("Sheared",false));
        CheckedNbts.add(EasyMobNbtInstance("HurtByTimestamp",0));
        CheckedNbts.add(EasyMobNbtInstance("CanPickUpLoot",false));
        CheckedNbts.add(EasyMobNbtInstance("Fire",(short)-1 ));
        CheckedNbts.add(EasyMobNbtInstance("DeathTime",(short)0 ));
        for (Object[] BaseValues : CheckedNbts) {
            final String CheckKey = (String)BaseValues[0];
            if (BaseValues[1] == null && nbtCompound.contains(CheckKey)) {
                nbtCompound.remove(CheckKey);
            } else if (nbtCompound.contains(CheckKey)) {
                NbtElement CurrentValue = nbtCompound.get(CheckKey);
                Object BaseValue = BaseValues[1];
                boolean DeleteValue = false;
                if (CurrentValue.getType() == NbtElement.INT_TYPE) {
                    DeleteValue = nbtCompound.getInt(CheckKey) == (int) BaseValue;
                } else if (CurrentValue.getType() == NbtElement.DOUBLE_TYPE) {
                    DeleteValue = nbtCompound.getDouble(CheckKey) == (double) BaseValue;
                } else if (CurrentValue.getType() == NbtElement.FLOAT_TYPE) {
                    DeleteValue = nbtCompound.getFloat(CheckKey) == (float) BaseValue;
                } else if (CurrentValue.getType() == NbtElement.SHORT_TYPE) {
                    DeleteValue = nbtCompound.getShort(CheckKey) == (short) BaseValue;
                } else if (CurrentValue.getType() == NbtElement.BYTE_TYPE) {
                    DeleteValue = (nbtCompound.getByte(CheckKey) != 0) == (boolean) BaseValue;
                }
                if (DeleteValue) {
                    nbtCompound.remove(CheckKey);
                }
            }
        }
        return nbtCompound;
    }

    public static ItemStack ThisStackOrSplit(ItemStack stack, PlayerEntity player) {
        if (stack.getCount() > 1) {
            ItemStack newstack = stack.split(1);
            player.getInventory().insertStack(newstack);
            return newstack;
        }
        return stack;
    }

    public static NbtCompound EntityToNbt(Entity entity) {
        if (entity == null) {
            return null;
        } else {
            NbtCompound Nbt = new NbtCompound();
            Nbt.putString(Keys.MobType, Registries.ENTITY_TYPE.getId(entity.getType()).toString());
                Nbt.put(Keys.MobNbt,CleanMobNbt(entity.writeNbt(new NbtCompound())) );
            Nbt.putInt(Keys.UniqueKey,(int)Math.round( Math.random()*5000 ) );
            return Nbt;
        }
    }
}
