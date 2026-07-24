package nico.bloodframework.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import nico.bloodframework.Bloodframework;
import nico.bloodframework.command.custom.BloodTypeCommand;
import nico.bloodframework.command.custom.ListTypesCommand;

public class ModCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(Bloodframework.MOD_ID);

        builder.then(BloodTypeCommand.create());
        builder.then(ListTypesCommand.create());

        dispatcher.register(builder);
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register(ModCommands::register);
    }
}