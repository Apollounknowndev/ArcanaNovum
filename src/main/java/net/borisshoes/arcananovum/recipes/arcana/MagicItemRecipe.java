package net.borisshoes.arcananovum.recipes.arcana;

import net.borisshoes.arcananovum.ArcanaNovum;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.blocks.forge.StarlightForgeBlockEntity;
import net.borisshoes.arcananovum.core.MagicItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class MagicItemRecipe {
   
   private final MagicItemIngredient[][] trueIngredients;
   private final ForgeRequirement forgeRequirement;
   
   public MagicItemRecipe(MagicItemIngredient[][] ingredients){
      this(ingredients,new ForgeRequirement());
   }
   
   public MagicItemRecipe(MagicItemIngredient[][] ingredients, ForgeRequirement forgeRequirement){
      this.trueIngredients = ingredients;
      this.forgeRequirement = forgeRequirement;
   }
   
   public boolean satisfiesRecipe(ItemStack[][] items, StarlightForgeBlockEntity forge){
      MagicItemIngredient[][] ingredients = getAlteredIngredients();
      if(ingredients.length != items.length || ingredients[0].length != items[0].length)
         return false;
      
      for(int i = 0; i < ingredients.length; i++){
         for(int j = 0; j < ingredients[0].length; j++){
            if(!ingredients[i][j].validStack(items[i][j]))
               return false;
         }
      }
      
      return forgeRequirement.forgeMeetsRequirement(forge, false, null);
   }
   
   public ItemStack[][] getRemainders(ItemStack[][] items, StarlightForgeBlockEntity forge, int resourceLvl){
      if(!satisfiesRecipe(items,forge))
         return null;
      ItemStack[][] remainders = new ItemStack[items.length][items[0].length];
      MagicItemIngredient[][] ingredients = getAlteredIngredients();
      for(int i = 0; i < ingredients.length; i++){
         for(int j = 0; j < ingredients[0].length; j++){
            remainders[i][j] = ingredients[i][j].getRemainder(items[i][j],resourceLvl);
         }
      }
      return remainders;
   }
   
   public MagicItemIngredient[][] getIngredients(){
      return getAlteredIngredients();
   }
   
   public ForgeRequirement getForgeRequirement(){
      return forgeRequirement;
   }
   
   public HashMap<String, Pair<Integer,ItemStack>> getIngredientList(){
      HashMap<String, Pair<Integer,ItemStack>> map = new HashMap<>();
      MagicItemIngredient[][] ingredients = getAlteredIngredients();
      for(int i = 0; i < ingredients.length; i++){
         for(int j = 0; j < ingredients[0].length; j++){
            ItemStack stack = ingredients[i][j].ingredientAsStack();
            if(!stack.isEmpty()){
               String ingred = ingredients[i][j].getName();
               Pair<Integer,ItemStack> pair;
               if(map.containsKey(ingred)){
                  int oldCount = map.get(ingred).getLeft();
                  pair = new Pair<>(ingredients[i][j].count+oldCount,stack);
               }else{
                  pair = new Pair<>(ingredients[i][j].count,stack);
               }
               map.put(ingred,pair);
            }
         }
      }
      return map;
   }
   
   public ArrayList<MagicItem> getForgeRequirementList(){
      ArrayList<MagicItem> list = new ArrayList<>();
      if(forgeRequirement.needsFletchery()){
         list.add(ArcanaRegistry.RADIANT_FLETCHERY);
      }
      if(forgeRequirement.needsAnvil()){
         list.add(ArcanaRegistry.TWILIGHT_ANVIL);
      }
      if(forgeRequirement.needsCore()){
         list.add(ArcanaRegistry.STELLAR_CORE);
      }
      if(forgeRequirement.needsEnchanter()){
         list.add(ArcanaRegistry.MIDNIGHT_ENCHANTER);
      }
      if(forgeRequirement.needsSingularity()){
         list.add(ArcanaRegistry.ARCANE_SINGULARITY);
      }
      return list;
   }
   
   private MagicItemIngredient[][] getAlteredIngredients(){
      int reduction = (int) ArcanaNovum.config.getValue("ingredientReduction");
      
      MagicItemIngredient[][] alteredIngredients = new MagicItemIngredient[trueIngredients.length][trueIngredients[0].length];
      for(int i = 0; i < trueIngredients.length; i++){
         for(int j = 0; j < trueIngredients[0].length; j++){
            int newCount = (int) Math.ceil((double) trueIngredients[i][j].getCount() / reduction);
            alteredIngredients[i][j] = trueIngredients[i][j].copyWithCount(newCount);
         }
      }
      
      return alteredIngredients;
   }
}
