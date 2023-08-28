package com.pany.mods.entity_capturing_tool.Helpers.SuggestionProviders;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.pany.mods.entity_capturing_tool.Helpers.CommandRegisterer;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class endercagehelp implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String helpwith = context.getArgument("help",String.class);
        if (helpwith != null) {
            helpwith = helpwith.toLowerCase();
            for (String val : CommandRegisterer.PossibleArguments) {
                if (helpwith.equals("") || val.toLowerCase().startsWith(helpwith)) {
                    builder.suggest(val);
                }
            }
        } else {
            for (String val : CommandRegisterer.PossibleArguments) {
                builder.suggest(val);
            }
        }
        return builder.buildFuture();
    }
}
