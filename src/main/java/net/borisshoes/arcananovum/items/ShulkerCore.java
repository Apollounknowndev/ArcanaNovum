package net.borisshoes.arcananovum.items;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.borisshoes.arcananovum.augments.ArcanaAugments;
import net.borisshoes.arcananovum.gui.shulkercore.ShulkerCoreGui;
import net.borisshoes.arcananovum.gui.shulkercore.ShulkerCoreInventory;
import net.borisshoes.arcananovum.gui.shulkercore.ShulkerCoreInventoryListener;
import net.borisshoes.arcananovum.items.core.*;
import net.borisshoes.arcananovum.recipes.MagicItemIngredient;
import net.borisshoes.arcananovum.recipes.MagicItemRecipe;
import net.borisshoes.arcananovum.recipes.SoulstoneIngredient;
import net.borisshoes.arcananovum.utils.MagicItemUtils;
import net.borisshoes.arcananovum.utils.MagicRarity;
import net.borisshoes.arcananovum.utils.ParticleEffectUtils;
import net.borisshoes.arcananovum.utils.SoundUtils;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.borisshoes.arcananovum.cardinalcomponents.PlayerComponentInitializer.PLAYER_DATA;

public class ShulkerCore extends EnergyItem implements LeftClickItem, UsableItem, TickingItem {
   
   public ShulkerCore(){
      id = "shulker_core";
      name = "Shulker Core";
      rarity = MagicRarity.EXOTIC;
      categories = new ArcaneTome.TomeFilter[]{ArcaneTome.TomeFilter.EXOTIC, ArcaneTome.TomeFilter.ITEMS};
      initEnergy = 1000;
   
      ItemStack item = new ItemStack(Items.SHULKER_BOX);
      NbtCompound tag = item.getOrCreateNbt();
      NbtCompound display = new NbtCompound();
      NbtList loreList = new NbtList();
      NbtList enchants = new NbtList();
      enchants.add(new NbtCompound()); // Gives enchant glow with no enchants
      display.putString("Name","[{\"text\":\"Shulker Core\",\"italic\":false,\"color\":\"#ffff99\",\"bold\":true}]");
      loreList.add(NbtString.of("[{\"text\":\"Shulkers \",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\"possess a \",\"color\":\"gray\"},{\"text\":\"unique \",\"color\":\"dark_purple\"},{\"text\":\"ability to defy \",\"color\":\"gray\"},{\"text\":\"gravity\",\"color\":\"white\"},{\"text\":\".\",\"color\":\"gray\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"This \",\"italic\":false,\"color\":\"gray\"},{\"text\":\"Core \",\"color\":\"dark_purple\"},{\"text\":\"has \"},{\"text\":\"harnessed\",\"color\":\"yellow\"},{\"text\":\" that ability to allow \"},{\"text\":\"controlled\",\"color\":\"yellow\"},{\"text\":\" \"},{\"text\":\"levitation\",\"color\":\"white\"},{\"text\":\".\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"The \",\"italic\":false,\"color\":\"gray\"},{\"text\":\"Core\",\"color\":\"dark_purple\"},{\"text\":\" must be \",\"italic\":false,\"color\":\"gray\"},{\"text\":\"refilled \",\"color\":\"dark_aqua\"},{\"text\":\"with \",\"italic\":false,\"color\":\"gray\"},{\"text\":\"Shulkers\",\"color\":\"light_purple\"},{\"text\":\".\",\"color\":\"gray\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Right Click\",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\" to grant \",\"color\":\"gray\"},{\"text\":\"levitation\",\"color\":\"white\"},{\"text\":\".\",\"color\":\"gray\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Sneak Right Click\",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\" to change the \",\"color\":\"gray\"},{\"text\":\"speed \",\"color\":\"dark_aqua\"},{\"text\":\"of \",\"color\":\"gray\"},{\"text\":\"levitation\",\"color\":\"white\"},{\"text\":\".\",\"color\":\"gray\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Left Click a block \",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\"to \",\"color\":\"gray\"},{\"text\":\"refill \",\"color\":\"dark_aqua\"},{\"text\":\"the \",\"color\":\"gray\"},{\"text\":\"Core\",\"color\":\"dark_purple\"},{\"text\":\"\",\"color\":\"dark_purple\"},{\"text\":\".\",\"color\":\"gray\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"\",\"italic\":false,\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Shulkers Left\",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\" - \",\"color\":\"gray\"},{\"text\":\"1000\",\"color\":\"yellow\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"\",\"italic\":false,\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Exotic \",\"italic\":false,\"color\":\"aqua\",\"bold\":true},{\"text\":\"Magic Item\",\"italic\":false,\"color\":\"dark_purple\",\"bold\":false}]"));
      display.put("Lore",loreList);
      tag.put("display",display);
      tag.put("Enchantments",enchants);
   
      setBookLore(makeLore());
      setRecipe(makeRecipe());
      tag = addMagicNbt(tag);
      NbtCompound magicTag = tag.getCompound("arcananovum");
      magicTag.putInt("speed",1);
      magicTag.putInt("speedCD",0);
      magicTag.putBoolean("stone",true);
      prefNBT = tag;
      item.setNbt(prefNBT);
      prefItem = item;
   
   }
   
   @Override
   public int getMaxEnergy(ItemStack item){
      return Integer.MAX_VALUE;
   }
   
   @Override
   public ItemStack updateItem(ItemStack stack, MinecraftServer server){
      NbtCompound itemNbt = stack.getNbt();
      NbtCompound magicTag = itemNbt.getCompound("arcananovum");
      int speed = magicTag.getInt("speed");
      int speedCD = magicTag.getInt("speedCD");
      boolean stone = magicTag.getBoolean("stone");
      NbtCompound newTag = super.updateItem(stack,server).getNbt();
      newTag.getCompound("arcananovum").putInt("speed",speed);
      newTag.getCompound("arcananovum").putInt("speedCD",speedCD);
      newTag.getCompound("arcananovum").putBoolean("stone",stone);
      stack.setNbt(newTag);
      redoLore(stack);
      return stack;
   }
   
   @Override
   public void onTick(ServerWorld world, ServerPlayerEntity player, ItemStack item){
      NbtCompound itemNbt = item.getNbt();
      NbtCompound magicTag = itemNbt.getCompound("arcananovum");
      int speedCD = magicTag.getInt("speedCD");
      if(speedCD > 0){
         speedCD--;
         magicTag.putInt("speedCD",speedCD);
      }
   }
   
   public void redoLore(ItemStack stack){
      NbtCompound itemNbt = stack.getNbt();
      NbtList loreList = itemNbt.getCompound("display").getList("Lore", NbtType.STRING);
      loreList.set(7,NbtString.of("[{\"text\":\"Shulkers Left\",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\" - \",\"color\":\"gray\"},{\"text\":\""+getEnergy(stack)+"\",\"color\":\"yellow\"}]"));
   }
   
   @Override
   public boolean attackBlock(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction){
      ItemStack item = playerEntity.getStackInHand(hand);
      openGui(playerEntity,item);
      return true;
   }
   
   @Override
   public boolean useItem(PlayerEntity playerEntity, World world, Hand hand){
      if(playerEntity.isSneaking()){
         changeSpeed(playerEntity,world,hand);
      }else{
         levitate(playerEntity,world,hand);
      }
      return false;
   }
   
   @Override
   public boolean useItem(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult result){
      if(playerEntity.isSneaking()){
         changeSpeed(playerEntity,world,hand);
      }else{
         levitate(playerEntity,world,hand);
      }
      return false;
   }
   
   private void changeSpeed(PlayerEntity playerEntity, World world, Hand hand){
      ItemStack item = playerEntity.getStackInHand(hand);
      NbtCompound itemNbt = item.getNbt();
      NbtCompound magicTag = itemNbt.getCompound("arcananovum");
      int speed = magicTag.getInt("speed");
      int speedCD = magicTag.getInt("speedCD");
      boolean reabsorb = Math.max(0, ArcanaAugments.getAugmentOnItem(item,"levitative_reabsorption")) >= 1;
      int maxSpeed = reabsorb ? 11 : 9;
   
      if(speedCD == 0){
         // 1 3 5 7 9
         speed += 2;
      
         if(speed > maxSpeed){
            speed = 1;
         }else if(speed < 1){
            speed = maxSpeed;
         }
      
         magicTag.putInt("speed",speed);
         magicTag.putInt("speedCD",5);
         if(playerEntity instanceof ServerPlayerEntity player){
            if(speed == 11){
               player.sendMessage(Text.literal("Shulker Core Mode: Reabsorption").formatted(Formatting.LIGHT_PURPLE,Formatting.ITALIC),true);
               SoundUtils.playSongToPlayer(player, SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 0.5f, 1);
            }else{
               player.sendMessage(Text.literal("Shulker Core Speed: "+(speed/2+1)).formatted(Formatting.LIGHT_PURPLE,Formatting.ITALIC),true);
               float pitch = (float) (0.1875*speed+0.3125);
               SoundUtils.playSongToPlayer(player, SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.5f, pitch);
            }
         }
      }
   }
   
   private void levitate(PlayerEntity playerEntity, World world, Hand hand){
      ItemStack item = playerEntity.getStackInHand(hand);
      NbtCompound itemNbt = item.getNbt();
      NbtCompound magicTag = itemNbt.getCompound("arcananovum");
      int speed = magicTag.getInt("speed");
      final int duration = 100;
   
      if(speed == 11){
         for(Map.Entry<StatusEffect, StatusEffectInstance> effectEntry : playerEntity.getActiveStatusEffects().entrySet()){
            StatusEffectInstance effect = effectEntry.getValue();
            if(effect.getEffectType() == StatusEffects.LEVITATION){
               playerEntity.removeStatusEffect(StatusEffects.LEVITATION);
               SoundUtils.playSound(world, playerEntity.getBlockPos(), SoundEvents.ENTITY_SHULKER_SHOOT, SoundCategory.PLAYERS, 1, 0.8f);
            }
         }
      }else{
         if(getEnergy(item) > 0){
            boolean hasBetter = false;
            for(Map.Entry<StatusEffect, StatusEffectInstance> effectEntry : playerEntity.getActiveStatusEffects().entrySet()){
               StatusEffectInstance effect = effectEntry.getValue();
               if(effect.getEffectType() == StatusEffects.LEVITATION && effect.getAmplifier() >= speed && !(effect.getDuration() < 10 || effect.getDuration() > duration)){
                  hasBetter = true;
               }
            }
            if(!hasBetter){
               StatusEffectInstance levit = new StatusEffectInstance(StatusEffects.LEVITATION, duration, speed, false, false, false);
               if(Math.random() >= .1 * Math.max(0, ArcanaAugments.getAugmentOnItem(item, "shulker_recycler")))
                  addEnergy(item, -(speed / 2 + 1));
               playerEntity.addStatusEffect(levit);
               SoundUtils.playSound(world, playerEntity.getBlockPos(), SoundEvents.ENTITY_SHULKER_SHOOT, SoundCategory.PLAYERS, 1, 0.8f);
               PLAYER_DATA.get(playerEntity).addXP(50 * (speed / 2 + 1)); // Add xp
               if(world instanceof ServerWorld serverWorld){
                  ParticleEffectUtils.shulkerCoreLevitate(serverWorld, playerEntity, duration);
               }
               redoLore(item);
            }
         }else{
            playerEntity.sendMessage(Text.literal("The Shulker Core is empty.").formatted(Formatting.YELLOW, Formatting.ITALIC), true);
            SoundUtils.playSound(world, playerEntity.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 0.8f);
         }
      }
   }
   
   public void openGui(PlayerEntity playerEntity, ItemStack item){
      if(!(playerEntity instanceof ServerPlayerEntity player))
         return;
      ShulkerCoreGui gui = new ShulkerCoreGui(ScreenHandlerType.HOPPER,player,this, item);
   
      NbtCompound itemNbt = item.getNbt();
      NbtCompound magicNbt = itemNbt.getCompound("arcananovum");
      boolean hasStone = magicNbt.getBoolean("stone");
   
      for(int i = 0; i < gui.getSize(); i++){
         gui.clearSlot(i);
      }
   
      Item pane = hasStone ? Items.MAGENTA_STAINED_GLASS_PANE : Items.BLACK_STAINED_GLASS_PANE;
      String paneText = hasStone ? getEnergy(item) + " Shulker Souls" : "No Soulstone Inserted";
      Formatting textColor = hasStone ? Formatting.YELLOW : Formatting.RED;
   
      gui.setSlot(0,new GuiElementBuilder(pane).setName(Text.literal(paneText).formatted(textColor)));
      gui.setSlot(1,new GuiElementBuilder(pane).setName(Text.literal(paneText).formatted(textColor)));
      gui.setSlot(3,new GuiElementBuilder(pane).setName(Text.literal(paneText).formatted(textColor)));
      gui.setSlot(4,new GuiElementBuilder(pane).setName(Text.literal(paneText).formatted(textColor)));
   
      ShulkerCoreInventory inv = new ShulkerCoreInventory();
      ShulkerCoreInventoryListener listener = new ShulkerCoreInventoryListener(this,gui,item);
      inv.addListener(listener);
      listener.setUpdating();
   
      gui.setSlotRedirect(2, new Slot(inv,0,0,0));
      if(hasStone){
         ItemStack stone = Soulstone.setSouls(Soulstone.setType(MagicItems.SOULSTONE.getNewItem(),EntityType.SHULKER),getEnergy(item));
         inv.setStack(0,stone);
         gui.validStone(stone);
      }else{
         gui.notValid();
      }
      gui.setTitle(Text.literal("Shulker Core"));
      listener.finishUpdate();
      
      gui.open();
   }
   
   public boolean hasStone(ItemStack item){
      if(MagicItemUtils.identifyItem(item) instanceof ShulkerCore){
         NbtCompound itemNbt = item.getNbt();
         NbtCompound magicNbt = itemNbt.getCompound("arcananovum");
         return magicNbt.getBoolean("stone");
      }
      return false;
   }
   
   public ItemStack getStone(ItemStack item){
      if(MagicItemUtils.identifyItem(item) instanceof ShulkerCore){
         NbtCompound itemNbt = item.getNbt();
         NbtCompound magicNbt = itemNbt.getCompound("arcananovum");
         if(magicNbt.getBoolean("stone")){
            return Soulstone.setSouls(Soulstone.setType(MagicItems.SOULSTONE.getNewItem(),EntityType.SHULKER),getEnergy(item));
         }
      }
      return null;
   }
   
   public void setStone(ItemStack item, ItemStack stone){
      NbtCompound itemNbt = item.getNbt();
      NbtCompound magicNbt = itemNbt.getCompound("arcananovum");
      if(stone == null){
         magicNbt.putBoolean("stone",false);
         setEnergy(item,0);
      }else{
         magicNbt.putBoolean("stone",true);
         setEnergy(item,Soulstone.getSouls(stone));
      }
   }
   
   @Override
   public ItemStack forgeItem(Inventory inv){
      // Souls n stuff
      ItemStack soulstoneStack = inv.getStack(12); // Should be the Soulstone
      ItemStack newMagicItem = null;
      if(MagicItemUtils.identifyItem(soulstoneStack) instanceof Soulstone){
         newMagicItem = getNewItem();
         setStone(newMagicItem,soulstoneStack);
         redoLore(newMagicItem);
      }
      return newMagicItem;
   }
   
   private MagicItemRecipe makeRecipe(){
      SoulstoneIngredient t = new SoulstoneIngredient(Soulstone.tiers[4],false,true, false,EntityType.getId(EntityType.SHULKER).toString());
      MagicItemIngredient s = new MagicItemIngredient(Items.SHULKER_SHELL,64,null);
      MagicItemIngredient o = new MagicItemIngredient(Items.GLOWSTONE,64,null);
      MagicItemIngredient n = new MagicItemIngredient(Items.NETHER_STAR,4,null);
      MagicItemIngredient m = new MagicItemIngredient(Items.PHANTOM_MEMBRANE,32,null);
   
      ItemStack p1 = new ItemStack(Items.POTION);
      MagicItemIngredient p = new MagicItemIngredient(Items.POTION,1, PotionUtil.setPotion(p1, Potions.LONG_SLOW_FALLING).getNbt());
      
      MagicItemIngredient[][] ingredients = {
            {o,s,m,s,o},
            {s,n,p,n,s},
            {m,p,t,p,m},
            {s,n,p,n,s},
            {o,s,m,s,o}};
      return new MagicItemRecipe(ingredients);
   }
   
   private List<String> makeLore(){
      ArrayList<String> list = new ArrayList<>();
      list.add("{\"text\":\"     Shulker Core\\n\\nRarity: Exotic\\n\\nShulkers are fascinating creatures, their unique levitation effect could be a precursor to true flight if I combined a bit of their essence... er... a lot of their essence... Whats a bit of genocide anyways?\"}");
      list.add("{\"text\":\"     Shulker Core\\n\\nAfter a massacre that took too long to comprehend, I have enough essence to control their power.\\n\\nRight click to grant levitation.\\nSneak right click to change the speed.\\nLeft click to swap out the Soulstone inside.\"}");
      return list;
   }
}
