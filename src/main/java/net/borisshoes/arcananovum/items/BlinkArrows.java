package net.borisshoes.arcananovum.items;

import net.borisshoes.arcananovum.recipes.MagicItemRecipe;
import net.borisshoes.arcananovum.utils.MagicRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.ArrayList;
import java.util.List;

public class BlinkArrows extends MagicItem{
   
   public BlinkArrows(){
      id = "blink_arrows";
      name = "Blink Arrows";
      rarity = MagicRarity.EXOTIC;
   
      ItemStack item = new ItemStack(Items.TIPPED_ARROW);
      NbtCompound tag = item.getOrCreateNbt();
      NbtCompound display = new NbtCompound();
      NbtList loreList = new NbtList();
      NbtList enchants = new NbtList();
      enchants.add(new NbtCompound()); // Gives enchant glow with no enchants
      display.putString("Name","[{\"text\":\"Runic Arrows - Blink\",\"italic\":false,\"bold\":true,\"color\":\"dark_aqua\"}]");
      loreList.add(NbtString.of("[{\"text\":\"Runic Arrows\",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\" make use of the \",\"color\":\"dark_purple\"},{\"text\":\"Runic Matrix\"},{\"text\":\" to create \",\"color\":\"dark_purple\"},{\"text\":\"unique effects\",\"color\":\"aqua\"},{\"text\":\".\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Runic Arrows\",\"italic\":false,\"color\":\"light_purple\"},{\"text\":\" will \",\"color\":\"dark_purple\"},{\"text\":\"only \",\"color\":\"dark_aqua\",\"italic\":true},{\"text\":\"activate their effect when fired from a \",\"color\":\"dark_purple\"},{\"text\":\"Runic Bow\"},{\"text\":\".\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"The \",\"italic\":false,\"color\":\"dark_purple\"},{\"text\":\"arrows\",\"color\":\"light_purple\"},{\"text\":\" can be refilled inside a \"},{\"text\":\"Runic Quiver\",\"color\":\"light_purple\"},{\"text\":\".\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"\",\"italic\":false,\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Blink Arrows:\",\"italic\":false,\"color\":\"dark_aqua\",\"bold\":true},{\"text\":\"\",\"italic\":false,\"color\":\"dark_purple\",\"bold\":false}]"));
      loreList.add(NbtString.of("[{\"text\":\"These \",\"italic\":false,\"color\":\"dark_green\"},{\"text\":\"Runic Arrows\",\"color\":\"light_purple\"},{\"text\":\" take after \"},{\"text\":\"Ender Pearls\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_green\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Upon \",\"italic\":false,\"color\":\"dark_green\"},{\"text\":\"impact \",\"color\":\"green\"},{\"text\":\"or \"},{\"text\":\"hitting a target\",\"color\":\"green\"},{\"text\":\" you get \"},{\"text\":\"teleported\",\"color\":\"dark_aqua\"},{\"text\":\" to the \"},{\"text\":\"arrow\",\"color\":\"light_purple\"},{\"text\":\".\"},{\"text\":\"\",\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"\",\"italic\":false,\"color\":\"dark_purple\"}]"));
      loreList.add(NbtString.of("[{\"text\":\"Exotic \",\"italic\":false,\"color\":\"aqua\",\"bold\":true},{\"text\":\"Magic Item\",\"italic\":false,\"color\":\"dark_purple\",\"bold\":false}]"));
      display.put("Lore",loreList);
      tag.put("display",display);
      tag.put("Enchantments",enchants);
      tag.putInt("CustomPotionColor",1404502);
      tag.putInt("HideFlags",127);
      item.setCount(64);
   
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
}