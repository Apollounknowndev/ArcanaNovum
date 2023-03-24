package net.borisshoes.arcananovum;

import net.borisshoes.arcananovum.callbacks.*;
import net.borisshoes.arcananovum.gui.WatchedGui;
import net.borisshoes.arcananovum.utils.ConfigUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static net.borisshoes.arcananovum.cardinalcomponents.WorldDataComponentInitializer.LOGIN_CALLBACK_LIST;

public class Arcananovum implements ModInitializer {
   
   private static final Logger logger = LogManager.getLogger("Arcana Novum");
   public static final ArrayList<TickTimerCallback> SERVER_TIMER_CALLBACKS = new ArrayList<>();
   public static final ArrayList<Pair<ServerWorld,TickTimerCallback>> WORLD_TIMER_CALLBACKS = new ArrayList<>();
   public static final HashMap<ServerPlayerEntity, WatchedGui> OPEN_GUIS = new HashMap<>();
   public static final boolean devMode = false;
   private static final String CONFIG_NAME = "ArcanaNovum.properties";
   public static ConfigUtils config;
   
   @Override
   public void onInitialize(){
      ServerTickEvents.END_WORLD_TICK.register(WorldTickCallback::onWorldTick);
      ServerTickEvents.END_SERVER_TICK.register(TickCallback::onTick);
      UseItemCallback.EVENT.register(ItemUseCallback::useItem);
      UseBlockCallback.EVENT.register(BlockUseCallback::useBlock);
      AttackBlockCallback.EVENT.register(BlockAttackCallback::attackBlock);
      PlayerBlockBreakEvents.BEFORE.register(BlockBreakCallback::breakBlock);
      ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(EntityKilledCallback::killedEntity);
      AttackEntityCallback.EVENT.register(EntityAttackCallback::attackEntity);
      ServerPlayConnectionEvents.JOIN.register(PlayerJoinCallback::onPlayerJoin);
      CommandRegistrationCallback.EVENT.register(CommandRegisterCallback::registerCommands);
      ServerEntityEvents.ENTITY_LOAD.register(EntityLoadCallbacks::loadEntity);
      ServerEntityEvents.ENTITY_UNLOAD.register(EntityLoadCallbacks::unloadEntity);
      ServerPlayerEvents.AFTER_RESPAWN.register(PlayerDeathCallback::afterRespawn);
   
      logger.info("Arcana Surges Through The Server!");
   
      config = new ConfigUtils(FabricLoader.getInstance().getConfigDir().resolve(CONFIG_NAME).toFile(), logger, Arrays.asList(new ConfigUtils.IConfigValue[] {
            new ConfigUtils.BooleanConfigValue("doConcentrationDamage", true,
                  new ConfigUtils.Command("Do Concentration Damage is %s", "Do Concentration Damage is now %s")),
            new ConfigUtils.BooleanConfigValue("announceAchievements", true,
                  new ConfigUtils.Command("Announce Achievements is %s", "Announce Achievements is now %s")),
      }));
   }
   
   public static boolean addTickTimerCallback(TickTimerCallback callback){
      return SERVER_TIMER_CALLBACKS.add(callback);
   }
   
   public static boolean addTickTimerCallback(ServerWorld world, TickTimerCallback callback){
      return WORLD_TIMER_CALLBACKS.add(new Pair<>(world,callback));
   }
   
   public static boolean addLoginCallback(LoginCallback callback){
      return LOGIN_CALLBACK_LIST.get(callback.getWorld()).addCallback(callback);
   }
   
   public static void devPrint(String msg){
      if(devMode){
         System.out.println(msg);
      }
   }
   
   /**
    * Uses built in logger to log a message
    * @param level 0 - Info | 1 - Warn | 2 - Error | 3 - Fatal | Else - Debug
    * @param msg  The {@code String} to be printed.
    */
   public static void log(int level, String msg){
      switch(level){
         case 0 -> logger.info(msg);
         case 1 -> logger.warn(msg);
         case 2 -> logger.error(msg);
         case 3 -> logger.fatal(msg);
         default -> logger.debug(msg);
      }
   }
}
