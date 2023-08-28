package com.pany.mods.entity_capturing_tool.Helpers;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.pany.mods.entity_capturing_tool.Helpers.SuggestionProviders.endercagehelp;
import com.sun.jdi.connect.Connector;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRegisterer {
    public static List<Object[]> Messages = new ArrayList<>();
    public static List<String> PossibleArguments = new ArrayList<>();
    public static MutableText Empty = null;
    protected static String Icon = "\uD83D\uDD12";

    public static void RegisterCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("endercage")
                            .then(argument("help", StringArgumentType.greedyString())
                                    .suggests(new endercagehelp())
                                    .executes(context -> {
                                        String text = context.getArgument("help",String.class);
                                        if (text != null) {
                                            MutableText Return = GetEntry(text);
                                            if (Return != null) {
                                                context.getSource().sendMessage(Return);
                                            }
                                        }
                                        return 1;
                                    })
                            )
                    .executes(context -> {
                        context.getSource().sendMessage(Empty);
                        return 1;
                    })
            );

        });
    }

    public static MutableText GetEntry(String EntryName) {
        for (Object[] Data : Messages) {
            String name = (String)Data[0];
            if (EntryName.toLowerCase().equals(name)) {
                return (MutableText)Data[1];
            }
        }
        return null;
    }

    protected static void RegisterMessage(String Name,MutableText text) {
        Object[] Data = new Object[2];
        Data[0] = Name.toLowerCase();
        Data[1] = text;
        Messages.add(Data);
        PossibleArguments.add(Name.toLowerCase());
    }
    protected static MutableText CreateBaseText() {
        MutableText text = Text.literal("<").setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
        text.append( Text.literal(Icon).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)) );
        text.append( Text.literal("> ").setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)) );
        return text;
    }
    protected static Style EasyStyle(Formatting formatting,boolean italics,boolean bold) {
        return Style.EMPTY.withColor(formatting).withItalic(italics).withBold(bold);
    }


    static {

        MutableText RedstoneText = CreateBaseText();
        RedstoneText.append( Text.translatable("text.endercage.command.redstone").setStyle(EasyStyle(Formatting.LIGHT_PURPLE,false,false)) );
        RegisterMessage("redstone",RedstoneText);

        MutableText AmbientText = CreateBaseText();
        AmbientText.append( Text.translatable("text.endercage.command.silence").setStyle(EasyStyle(Formatting.LIGHT_PURPLE,false,false)) );
        RegisterMessage("silencing",AmbientText);

        MutableText DisplayText = CreateBaseText();
        AmbientText.append( Text.translatable("text.endercage.command.display").setStyle(EasyStyle(Formatting.LIGHT_PURPLE,false,false)) );
        RegisterMessage("displaying",AmbientText);

        Empty = CreateBaseText();
        Empty.append( Text.translatable("text.endercage.command.empty")
                .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        boolean First = true;
        for (String arg : PossibleArguments) {
            if (First) {
                Empty.append(Text.literal(arg).setStyle(EasyStyle(Formatting.LIGHT_PURPLE,false,false)));
            } else {
                Empty.append(Text.literal(", " + arg).setStyle(EasyStyle(Formatting.LIGHT_PURPLE,false,false)));
            }
            First = false;
        }

    }
}
