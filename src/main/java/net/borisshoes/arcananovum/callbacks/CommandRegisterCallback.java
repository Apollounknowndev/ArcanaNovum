package net.borisshoes.arcananovum.callbacks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.elements.clickable.CubeHitboxHologramElement;
import eu.pb4.holograms.api.elements.clickable.EntityHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.EntityHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import net.borisshoes.arcananovum.ArcanaCommands;
import net.borisshoes.arcananovum.Arcananovum;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.borisshoes.arcananovum.Arcananovum.devMode;
import static net.minecraft.command.argument.EntityArgumentType.*;
import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRegisterCallback {
   public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
      dispatcher.register(literal("arcana")
            .then(literal("create").requires(source -> source.hasPermissionLevel(2))
                  .then(argument("id", string()).suggests(ArcanaCommands::getItemSuggestions)
                        .executes(ctx -> ArcanaCommands.createItem(ctx.getSource(), getString(ctx, "id")))))
            .then(literal("help").executes(ArcanaCommands::openGuideBook))
            .then(literal("guide").executes(ArcanaCommands::openGuideBook))
            .then(literal("uuids").requires(source -> source.hasPermissionLevel(2))
                  .then(argument("player",player()).executes(context -> ArcanaCommands.uuidCommand(context,getPlayer(context,"player")))))
            .then(literal("xp").requires(source -> source.hasPermissionLevel(2))
                  .then(literal("add")
                        .then(argument("targets", players())
                              .then(((argument("amount", integer())
                                    .executes(context -> ArcanaCommands.xpCommand(context,getPlayers(context,"targets"),getInteger(context,"amount"),false,true)))
                                    .then(literal("points")
                                          .executes(context -> ArcanaCommands.xpCommand(context,getPlayers(context,"targets"),getInteger(context,"amount"), false,true))))
                                    .then(literal("levels")
                                          .executes(context -> ArcanaCommands.xpCommand(context,getPlayers(context,"targets"),getInteger(context,"amount"),false,false))))))
                  .then(literal("set")
                        .then(argument("targets", players()).then(((argument("amount", integer(0))
                              .executes(context -> ArcanaCommands.xpCommand(context,getPlayers(context,"targets"),getInteger(context,"amount"),true,true)))
                              .then(literal("points")
                                    .executes(context -> ArcanaCommands.xpCommand(context,getPlayers(context,"targets"),getInteger(context,"amount"),true,true))))
                              .then(literal("levels")
                                    .executes(context -> ArcanaCommands.xpCommand(context,getPlayers(context,"targets"),getInteger(context,"amount"),true,false))))))
                  .then(literal("query")
                        .then(argument("targets",player())
                              .executes(context -> ArcanaCommands.xpCommandQuery(context, getPlayer(context,"targets"))))))
            .then(literal("boss")
                  .then(literal("start").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("dragon").executes(ArcanaCommands::startDragonBoss)))
                  .then(literal("abort").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::abortBoss))
                  .then(literal("clean").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::cleanBoss))
                  .then(literal("status").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::bossStatus))
                  .then(literal("announce").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("time", MessageArgumentType.message()).executes(ctx -> ArcanaCommands.announceBoss(ctx.getSource(), MessageArgumentType.getMessage(ctx, "time").getString()))))
                  .then(literal("begin").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::beginBoss))
                  .then(literal("teleport")
                        .executes(context -> ArcanaCommands.bossTeleport(context,context.getSource().getPlayer(),false))
                        .then(argument("player",player()).requires(source -> source.hasPermissionLevel(2))
                              .executes(context -> ArcanaCommands.bossTeleport(context,getPlayer(context,"player"),false)))
                        .then(literal("all").requires(source -> source.hasPermissionLevel(2))
                              .executes(context ->ArcanaCommands.bossTeleport(context,context.getSource().getPlayer(),true)))))
      );
   
      if(devMode){
         dispatcher.register(literal("arcana")
               .then(literal("test").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::testCommand))
               .then(literal("getbookdata").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::getBookData))
               .then(literal("getitemdata").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::getItemData))
               .then(literal("makerecipe").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::makeCraftingRecipe))
               .then(literal("boss")
                     .then(literal("test").requires(source -> source.hasPermissionLevel(2)).executes(ArcanaCommands::testBoss)))
         );
      }
   }
}
