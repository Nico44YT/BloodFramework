package nico.bloodframework.command.custom;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import nico.bloodframework.data.BloodTypeDataLoader;

public class ListTypesCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> create() {
        return CommandManager.literal("list").executes(ListTypesCommand::listAll);
    }

    private static int listAll(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("List (" + BloodTypeDataLoader.getData().size() + ")"), false);
        BloodTypeDataLoader.getData().keySet().forEach(type -> {
            context.getSource().sendFeedback(() -> Text.literal("- " + type.toString()), false);
        });
        return 1;
    }
}
