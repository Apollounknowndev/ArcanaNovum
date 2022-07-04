package net.borisshoes.arcananovum.items;

import net.borisshoes.arcananovum.recipes.MagicItemRecipe;
import net.borisshoes.arcananovum.utils.MagicRarity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RunicMatrix extends MagicItem implements UsableItem{
   public RunicMatrix(){
      id = "runic_matrix";
      name = "Runic Matrix";
      rarity = MagicRarity.NONE;
   
      ItemStack item = new ItemStack(Items.END_CRYSTAL);
      NbtCompound tag = item.getOrCreateNbt();
      NbtCompound display = new NbtCompound();
      NbtList loreList = new NbtList();
      NbtList enchants = new NbtList();
      enchants.add(new NbtCompound()); // Gives enchant glow with no enchants
      display.putString("Name","[{\"text\":\"Runic Matrix\",\"italic\":false,\"color\":\"light_purple\",\"bold\":true}]");
      loreList.add(NbtString.of("[{\"text\":\"The \",\"italic\":false},{\"text\":\"Runes \",\"color\":\"light_purple\"},{\"text\":\"engraved on this \",\"color\":\"dark_purple\"},{\"text\":\"crystalline \",\"color\":\"aqua\"},{\"text\":\"structure\",\"color\":\"dark_purple\"},{\"text\":\" shift\",\"color\":\"blue\"},{\"text\":\" constantly.\",\"color\":\"dark_purple\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"They \",\"italic\":false,\"color\":\"dark_purple\"},{\"text\":\"slide\",\"color\":\"blue\"},{\"text\":\" to form different combinations of \"},{\"text\":\"runic \",\"color\":\"light_purple\"},{\"text\":\"equations\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"The \",\"italic\":false,\"color\":\"dark_purple\"},{\"text\":\"matrix \",\"color\":\"light_purple\"},{\"text\":\"allows for the \"},{\"text\":\"invokation \",\"color\":\"aqua\"},{\"text\":\"of many different \"},{\"text\":\"effects\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"\",\"italic\":false,\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Mundane\",\"italic\":false,\"color\":\"gray\",\"bold\":true},{\"text\":\" Magic Item\",\"italic\":false,\"color\":\"dark_purple\",\"bold\":false}]"));
      display.put("Lore",loreList);
      tag.put("display",display);
      tag.put("Enchantments",enchants);
   
      setBookLore(makeLore());
      //setRecipe(makeRecipe());
      prefNBT = addMagicNbt(tag);
   
      item.setNbt(prefNBT);
      prefItem = item;
   }
   
   private MagicItemRecipe makeRecipe(){
      //TODO make recipe
      return null;
   }
   
   private List<String> makeLore(){
      //TODO make lore
      ArrayList<String> list = new ArrayList<>();
      list.add("{\"text\":\" TODO \"}");
      return list;
   }
   
   @Override
   public boolean useItem(PlayerEntity playerEntity, World world, Hand hand){
      return false;
   }
   
   @Override
   public boolean useItem(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult result){
      return false;
   }
}
