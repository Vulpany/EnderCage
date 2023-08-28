package com.pany.mods.entity_capturing_tool.Helpers;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.binary.BaseNCodec;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigHelper {
    public static String BaseBannedEntities = "[minecraft:wither,\nminecraft:ender_dragon,\nminecraft:warden]";
    public static boolean HostilesAllowed = false;
    public static String BannedEntitiesSavedString = null;
    public static List<EntityType> BannedEntities = null;
    public static boolean RedstoneEnabled = true;
    public static boolean AllowSounds = true;


    public ConfigHelper() throws IOException {
        this.ReadConfig();
    }

    protected void ReadConfig() throws IOException {
        String configpath = FabricLoader.getInstance().getConfigDir() + "\\Endercage";
        File ConfigFile = new File(configpath );
        try {
            ConfigFile.createNewFile();
        } catch (Exception ignored) {}
        String Content = new String(Files.readAllBytes(Path.of(configpath)) );
        String FinishedContent = Content.toLowerCase().replaceAll("\n","<t>").replaceAll( String.valueOf(10),"<t>" ).replaceAll(" ","").replaceAll(",<t>",",");
        for (String StringValue : FinishedContent.split("<t>")) {
            String[] Split = StringValue.split("=");
            String VarName = Split[0].toLowerCase();
            if (Split.length > 1) {
                String Value = Split[1];
                if (StringToBoolean(Value) != null) {
                    boolean ActualValue = (Boolean)StringToBoolean(Value);
                    if (Objects.equals(VarName, ConfigKeys.CaptureOfHostiles.toLowerCase())) {
                        HostilesAllowed = ActualValue;
                        System.out.println("Set hostiles");
                    } else if (Objects.equals(VarName, ConfigKeys.Redstone.toLowerCase())) {
                        RedstoneEnabled = ActualValue;
                        System.out.println("Set redstone");
                    } else if (Objects.equals(VarName, ConfigKeys.Sounds.toLowerCase())) {
                        AllowSounds = ActualValue;
                        System.out.println("Set sounds");
                    }
                }
                if (Objects.equals(VarName, ConfigKeys.BannedEntities.toLowerCase())) {
                    String[] BannedList = StringToList(Value);
                    if (BannedList != null) {
                        BannedEntitiesSavedString = Value;
                        BannedEntities = new ArrayList<>();
                        for (String Name : BannedList) {
                            System.out.println(Name);
                            EntityType toadd = null;
                            try {
                                toadd = Registries.ENTITY_TYPE.get( new Identifier(Name));
                            } catch (Exception e) {}
                            if (toadd != null) {
                                BannedEntities.add(toadd);
                                BannedEntitiesSavedString = Value;
                            }
                        }
                    }
                }

            }
        }
        if (BannedEntities == null) {
            BannedEntities = new ArrayList<>();
            String[] BannedList = StringToList(BaseBannedEntities.replaceAll("\n",""));
            for (String Name : BannedList) {
                EntityType toadd = null;
                try {
                    toadd = Registries.ENTITY_TYPE.get( new Identifier(Name));
                } catch (Exception e) {}
                if (toadd != null) {
                    BannedEntities.add(toadd);
                    BannedEntitiesSavedString = BaseBannedEntities;
                }
            }
        }
        FileWriter Writer = new FileWriter(configpath);
        Writer.write(this.SettingsToString());
        Writer.close();
    }

    public String[] StringToList(String Input) {
        String[] ToReturn = null;
        if (Input.startsWith("[") && Input.endsWith("]")) {
            ToReturn = Input.substring(1).substring(0,Input.length()-2).split(",");
        }
        return ToReturn;
    }

    public String ListToString(String[] List) {
        StringBuilder Content = new StringBuilder("[");
        boolean First = true;
        for (String val : List) {
            if (!First) {
                Content.append("\n,");
            }
            Content.append(val);
            First = false;
        }
        Content.append("]");
        return Content.toString();
    }

    public Object StringToBoolean(String string) {
        if (string.equals("true") || string.equals("false")) {
            return string.equals("true");
        }
        return null;
    }

    public String SettingsToString() {
        StringBuilder Content = new StringBuilder("");
        Content.append(ConfigKeys.CaptureOfHostiles + "=" + (HostilesAllowed ? "true" : "false"));
        Content.append("\n");
        Content.append(ConfigKeys.Sounds + "=" + (AllowSounds ? "true" : "false"));
        Content.append("\n");
        Content.append(ConfigKeys.Redstone + "=" + (RedstoneEnabled ? "true" : "false"));
        Content.append("\n");
        Content.append(ConfigKeys.BannedEntities + "=" + (BannedEntitiesSavedString == null ? BaseBannedEntities : BannedEntitiesSavedString ));
        return Content.toString();
    }

    protected static record ConfigKeys() {
        public static String CaptureOfHostiles = "AllowCaptureOfHostile";
        public static String BannedEntities = "BannedFromCapturing";
        public static String Redstone = "RedstoneFunctionality";
        public static String Sounds = "AmbientEntitySoundsFromNonSilentCages";
    }

}
