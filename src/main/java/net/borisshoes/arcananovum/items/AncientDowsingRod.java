package net.borisshoes.arcananovum.items;

import net.borisshoes.arcananovum.ArcanaNovum;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.achievements.ArcanaAchievements;
import net.borisshoes.arcananovum.augments.ArcanaAugments;
import net.borisshoes.arcananovum.core.EnergyItem;
import net.borisshoes.arcananovum.core.polymer.MagicPolymerItem;
import net.borisshoes.arcananovum.recipes.arcana.MagicItemIngredient;
import net.borisshoes.arcananovum.recipes.arcana.MagicItemRecipe;
import net.borisshoes.arcananovum.utils.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.borisshoes.arcananovum.cardinalcomponents.PlayerComponentInitializer.PLAYER_DATA;

public class AncientDowsingRod extends EnergyItem {
   private static final String CHARGED_TXT = "item/ancient_dowsing_rod_charged";
   private static final String COOLDOWN_TXT = "item/ancient_dowsing_rod_cooldown";
   
   public AncientDowsingRod(){
      id = "ancient_dowsing_rod";
      name = "Ancient Dowsing Rod";
      rarity = MagicRarity.EMPOWERED;
      categories = new ArcaneTome.TomeFilter[]{ArcaneTome.TomeFilter.EMPOWERED, ArcaneTome.TomeFilter.ITEMS};
      vanillaItem = Items.BLAZE_ROD;
      item = new AncientDowsingRodItem(new FabricItemSettings().maxCount(1).fireproof());
      models = new ArrayList<>();
      models.add(new Pair<>(vanillaItem,CHARGED_TXT));
      models.add(new Pair<>(vanillaItem,COOLDOWN_TXT));
   
      ItemStack itemStack = new ItemStack(item);
      NbtCompound tag = itemStack.getOrCreateNbt();
      NbtCompound display = new NbtCompound();
      NbtList enchants = new NbtList();
      enchants.add(new NbtCompound()); // Gives enchant glow with no enchants
      display.putString("Name","[{\"text\":\"Ancient Dowsing Rod\",\"italic\":false,\"bold\":true,\"color\":\"dark_red\"}]");
      tag.put("display",display);
      tag.put("Enchantments",enchants);
      buildItemLore(itemStack, ArcanaNovum.SERVER);
   
      setBookLore(makeLore());
      setRecipe(makeRecipe());
      prefNBT = addMagicNbt(tag);
   
      itemStack.setNbt(prefNBT);
      prefItem = itemStack;
   }
   
   @Override
   public NbtList getItemLore(@Nullable ItemStack itemStack){
      NbtList loreList = new NbtList();
      loreList.add(NbtString.of("[{\"text\":\"Ancient civilizations\",\"italic\":false,\"color\":\"gold\"},{\"text\":\" in the \",\"color\":\"red\"},{\"text\":\"nether \",\"color\":\"dark_red\"},{\"text\":\"had ways of finding \",\"color\":\"red\"},{\"text\":\"netherite\",\"color\":\"dark_red\"},{\"text\":\".\",\"color\":\"red\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"This \",\"italic\":false,\"color\":\"red\"},{\"text\":\"dowsing rod \",\"color\":\"dark_red\"},{\"text\":\"is based on \"},{\"text\":\"ancient designs\",\"color\":\"gold\"},{\"text\":\" to locate \"},{\"text\":\"netherite scrap\",\"color\":\"dark_red\"},{\"text\":\".\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Right Click\",\"italic\":false,\"color\":\"gold\"},{\"text\":\" to search for \",\"color\":\"red\"},{\"text\":\"ancient debris\",\"color\":\"dark_red\"},{\"text\":\".\",\"color\":\"red\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      return loreList;
   }
   
   @Override
   public int getMaxEnergy(ItemStack item){
      // 30 second base recharge
      int lvl = Math.max(0,ArcanaAugments.getAugmentOnItem(item,ArcanaAugments.SONIC_REABSORPTION.id));
      return 30 - 5*lvl;
   }
   
   private MagicItemRecipe makeRecipe(){
      MagicItemIngredient d = new MagicItemIngredient(Items.ANCIENT_DEBRIS,4,null);
      MagicItemIngredient f = new MagicItemIngredient(Items.FIRE_CHARGE,64,null);
      MagicItemIngredient s = new MagicItemIngredient(Items.NETHERITE_SCRAP,4,null);
      MagicItemIngredient w = new MagicItemIngredient(Items.RED_NETHER_BRICKS,64,null);
      MagicItemIngredient b = new MagicItemIngredient(Items.BELL,4,null);
      MagicItemIngredient n = new MagicItemIngredient(Items.NETHERITE_INGOT,1,null);
      MagicItemIngredient g = new MagicItemIngredient(Items.GOLD_INGOT,64,null);
      MagicItemIngredient r = new MagicItemIngredient(Items.BLAZE_ROD,64,null);
   
      MagicItemIngredient[][] ingredients = {
            {d,f,s,w,d},
            {f,n,g,n,w},
            {s,r,b,r,s},
            {w,n,g,n,f},
            {d,w,s,f,d}};
      return new MagicItemRecipe(ingredients);
   }
   
   private List<String> makeLore(){
      ArrayList<String> list = new ArrayList<>();
      list.add("{\"text\":\" Ancient Dowsing Rod\\n\\nRarity: Empowered\\n\\nModern Piglins seem to be incapable of finding Netherite, but their bastions contain fragments of it.\\nI recovered some pieces of a tool used by their ancestors, perhaps I can reconstruct it.\"}");
      list.add("{\"text\":\" Ancient Dowsing Rod\\n\\nRight click the rod to send out a resonating signal that bounces of nearby Ancient Debris.\\n\\nThe sound's echo triggers a compass of flame to indicate how much debris is nearby. And a flaming arrow to show the nearest pile of debris.\"}");
      return list;
   }
   
   public class AncientDowsingRodItem extends MagicPolymerItem {
      public AncientDowsingRodItem(Settings settings){
         super(getThis(),settings);
      }
      
      @Override
      public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player){
         return getEnergy(itemStack) == getMaxEnergy(itemStack) ? ArcanaRegistry.MODELS.get(CHARGED_TXT).value() : ArcanaRegistry.MODELS.get(COOLDOWN_TXT).value();
      }
      
      @Override
      public ItemStack getDefaultStack() {
         return prefItem;
      }
      
      @Override
      public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipContext context, @Nullable ServerPlayerEntity player){
         return super.getPolymerItemStack(itemStack, context, player);
      }
      
      @Override
      public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected){
         if(!MagicItemUtils.isMagic(stack)) return;
         if(!(world instanceof ServerWorld)) return;
         if(world.getServer().getTicks() % 20 == 0){
            addEnergy(stack, 1); // Recharge
         }
      }
      
      @Override
      public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
         ItemStack item = playerEntity.getStackInHand(hand);
         if (playerEntity instanceof ServerPlayerEntity player){
            int curEnergy = getEnergy(item);
            if(curEnergy == getMaxEnergy(item)){
               setEnergy(item,0);
               final int scanRange = 25 + 5*Math.max(0,ArcanaAugments.getAugmentOnItem(item,ArcanaAugments.ENHANCED_RESONANCE.id));
               BlockPos curBlock = playerEntity.getBlockPos();
               SoundUtils.playSound(world, curBlock, SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1f, .5f);
               
               List<BlockPos> debris = new ArrayList<>();
               for(BlockPos block : BlockPos.iterateOutwards(curBlock,scanRange,scanRange/2,scanRange)){
                  if(world.getBlockState(block).getBlock() == Blocks.ANCIENT_DEBRIS){
                     debris.add(new BlockPos(block));
                  }
               }
               if(world instanceof ServerWorld serverWorld){
                  if(!debris.isEmpty()) ArcanaAchievements.progress(player,ArcanaAchievements.ARCHEOLOGIST.id,debris.size());
                  
                  ArcanaNovum.addTickTimerCallback(new GenericTimer(30, () -> SoundUtils.playSound(world, playerEntity.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1f, .5f)));
                  ArcanaNovum.addTickTimerCallback(new GenericTimer(140, () -> {
                     int[] locations = new int[8]; // N, NE, E, SE, S, SW, W, NW
                     final double t1 = Math.tan(Math.toRadians(45*3.0/2));
                     final double t2 = Math.tan(Math.toRadians(45/2.0));
                     final Vec3d playerPos = playerEntity.getPos();
                     int count = 0;
                     
                     for(BlockPos b : debris){
                        Vec3d rPos = new Vec3d( b.getX() - playerPos.x, b.getY() - playerPos.y,b.getZ() - playerPos.z);
                        double ratio = rPos.x == 0 ? 100 : rPos.z / rPos.x;
                        int ind = 0;
                        
                        if(ratio < 0 && ratio > -t2){
                           ind = 0;
                        }else if(ratio > -t1 && ratio < -t2){
                           ind = 1;
                        }else if(ratio < -t1 || ratio > t1){
                           ind = 2;
                        }else if(ratio < t1 && ratio > t2){
                           ind = 3;
                        }else if(ratio > 0 && ratio < t2){
                           ind = 4;
                        }
                        if(rPos.z < 0){
                           ind += 4;
                        }
                        if(ind > locations.length-1){
                           ind = 0;
                        }
                        locations[ind]++;
                        
                        if(count < 12)
                           ParticleEffectUtils.dowsingRodEmitter(serverWorld,new Vec3d(b.getX(),b.getY(),b.getZ()),1,100 + 33*Math.max(0,ArcanaAugments.getAugmentOnItem(item,ArcanaAugments.HARMONIC_FEEDBACK.id)));
                        count++;
                     }
                     double radius = 1.5;
                     for(int i = 0; i < locations.length; i++){
                        Vec3d pPos = switch(i){
                           case 0 -> new Vec3d(-radius, 0, 0);
                           case 1 -> new Vec3d(-radius, 0, radius);
                           case 2 -> new Vec3d(0, 0, radius);
                           case 3 -> new Vec3d(radius, 0, radius);
                           case 4 -> new Vec3d(radius, 0, 0);
                           case 5 -> new Vec3d(radius, 0, -radius);
                           case 6 -> new Vec3d(0, 0, -radius);
                           case 7 -> new Vec3d(-radius, 0, -radius);
                           default -> new Vec3d(0,0,0);
                        };
                        for(int n = 0; n < locations[i]; n++){
                           double mod = Math.min(n*.6 , 6/radius);
                           if (mod == 6/radius)
                              break;
                           Vec3d parPos = playerPos.add(pPos.multiply(1+mod)).add(0,0.7,0);
                           player.getServerWorld().spawnParticles(ParticleTypes.DRIPPING_LAVA,parPos.x,parPos.y,parPos.z,15,.12,.12,.12,1);
                        }
                        
                     }
                     if(!debris.isEmpty()){
                        BlockPos closest = debris.get(0);
                        Vec3d eyePos = playerEntity.getEyePos();
                        Vec3d blockPos = new Vec3d(closest.getX()+.5,closest.getY()+0.5,closest.getZ()+0.5);
                        Vec3d start = eyePos.add(blockPos.subtract(eyePos).normalize().multiply(1.5));
                        Vec3d end = eyePos.add(blockPos.subtract(eyePos).normalize().multiply(1.5+3));
                        ParticleEffectUtils.dowsingRodArrow(player.getServerWorld(),start,end,1);
                        
                        PLAYER_DATA.get(player).addXP(15*debris.size()); // Add xp
                        SoundUtils.playSound(world, playerEntity.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1f, .5f);
                        
                        if(debris.size() >= 10){
                           ArcanaAchievements.grant(player,ArcanaAchievements.MOTHERLOAD.id);
                        }
                     }else{
                        SoundUtils.playSound(world, playerEntity.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS,1,.5f);
                     }
                  }));
               }
               
            }else{
               playerEntity.sendMessage(Text.translatable("Dowsing Rod Recharging: "+(curEnergy*100/getMaxEnergy(item))+"%").formatted(Formatting.GOLD),true);
               SoundUtils.playSongToPlayer(player,SoundEvents.BLOCK_FIRE_EXTINGUISH,1,.5f);
            }
         }
         
         return TypedActionResult.success(item);
      }
   }
}
