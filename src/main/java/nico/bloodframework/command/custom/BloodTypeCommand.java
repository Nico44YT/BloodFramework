package nico.bloodframework.command.custom;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nico.bloodframework.data.BloodTypeDataLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BloodTypeCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> create() {
        LiteralArgumentBuilder<ServerCommandSource> builder =
                CommandManager.literal("type")
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("blood_type", IdentifierArgumentType.identifier())
                                        .suggests(BloodTypeCommand::suggestBloodTypes)
                                        .then(CommandManager.argument("target", EntityArgumentType.entities())
                                                .executes(BloodTypeCommand::executeSet))
                                        .executes(BloodTypeCommand::executeSetSelf)
                                ))
                        .then(CommandManager.literal("get")
                                .then(CommandManager.argument("target", EntityArgumentType.entity())
                                        .executes(BloodTypeCommand::executeGet))
                                .executes(BloodTypeCommand::executeGetSelf)
                        );

        builder.executes(BloodTypeCommand::executeGet);

        return builder;
    }

    private static int executeGetSelf(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 1;

        Identifier bloodTypeId = player.bloodFramework$getBloodType();

        context.getSource().sendFeedback(() ->
                Text.translatable(
                        "command.blood_framework.get.feedback",
                        player.getDisplayName(),
                        Text.literal(bloodTypeId.toString())
                ), false);
        return 0;
    }

    private static int executeGet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = EntityArgumentType.getEntity(context, "target");
        if (!(entity instanceof LivingEntity livingEntity)) return 1;

        Identifier bloodTypeId = livingEntity.bloodFramework$getBloodType();
        context.getSource().sendFeedback(() ->
                Text.translatable(
                        "command.blood_framework.get.feedback",
                        livingEntity.getDisplayName(),
                        Text.literal(bloodTypeId.toString())
                ), false);
        return 0;
    }

    private static int executeSetSelf(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 1;

        Identifier type = IdentifierArgumentType.getIdentifier(context, "blood_type");

        if (!BloodTypeDataLoader.getData().containsKey(type)) {
            context.getSource().sendFeedback(() -> Text.translatable("command.blood_framework.blood_type_not_found", type.toString()), false);
            return 1;
        }

        player.bloodFramework$setBloodType(type);

        return 0;
    }

    private static int executeSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Identifier type = IdentifierArgumentType.getIdentifier(context, "blood_type");

        if (!BloodTypeDataLoader.getData().containsKey(type)) {
            context.getSource().sendFeedback(() -> Text.translatable("command.blood_framework.blood_type_not_found", type.toString()), false);
            return 1;
        }

        EntityArgumentType.getEntities(context, "target").forEach(entity -> {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.bloodFramework$setBloodType(type);
            }
        });

        return 0;
    }

    private static CompletableFuture<Suggestions> suggestBloodTypes(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        List<Suggestion> suggestions = new ArrayList<>();

        StringReader stringReader = new StringReader(suggestionsBuilder.getInput());
        stringReader.setCursor(suggestionsBuilder.getStart());


        BloodTypeDataLoader.getData().keySet().forEach(key -> {
            suggestions.add(new Suggestion(StringRange.between(stringReader.getCursor(), stringReader.getCursor() + key.toString().length()), key.toString()));
        });

        Collections.sort(suggestions);

        return CompletableFuture.supplyAsync(() -> new Suggestions(StringRange.at(stringReader.getCursor()), suggestions));
    }
}