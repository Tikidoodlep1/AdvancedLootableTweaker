package com.tiki.advancedlootableweapons.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.tiki.advancedlootableweapons.Alw;
import com.tiki.advancedlootableweapons.IHasModel;
import com.tiki.advancedlootableweapons.compat.oreDictionary.OreDictionaryCompat;
import com.tiki.advancedlootableweapons.init.BlockInit;
import com.tiki.advancedlootableweapons.init.EnchantmentInit;
import com.tiki.advancedlootableweapons.init.EntityInit;
import com.tiki.advancedlootableweapons.init.FluidInit;
import com.tiki.advancedlootableweapons.init.ItemInit;
import com.tiki.advancedlootableweapons.world.WorldGenCustomOres;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import scala.actors.threadpool.Arrays;

@EventBusSubscriber
public class RegistryHandler {
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {		
		event.getRegistry().registerAll(ItemInit.items.toArray(new Item[0]));
		Alw.proxy.registerCustomModelLoaders();
		OreDictionaryCompat.registerOres();
		
		ItemInit.generateAcceptedForgeItems();
		ItemInit.createRecipes();
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(BlockInit.blocks.toArray(new Block[0]));
		TileEntityHandler.registerTileEntities();
		Alw.proxy.registerCustomMeshesAndStateStuff();
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item: ItemInit.items) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		
		for(Block block: BlockInit.blocks) {
			if(block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
		
		Alw.proxy.registerTESRs();
	}
	
	@SubscribeEvent
	public static void onEnchantmentRegister(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().registerAll(EnchantmentInit.enchantments.toArray(new Enchantment[0]));
		
	}
	
	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		FluidInit.registerFluids();
		EntityInit.registerEntities();
		Alw.proxy.registerEntityRenders();
		ConfigHandler.registerConfig(event);
		ItemInit.checkConfigOptions();
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
	}
	
	public static void initRegistries(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Alw.instance, new GuiHandler());
		
		
		SoundHandler.registerSounds();
		GlobalDropsHandler.registerDrops();
		Alw.proxy.addColoredItemRenderer();
	}
	
	public static void postInitRegistries(FMLPostInitializationEvent event)
	{
		//Dumping furnace recipes to json files for the alloy furnace
//		Gson gson = new Gson();
//		for(Entry<ItemStack, ItemStack> e : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
//			if(e.getKey().getItem() instanceof ItemArmor) {
//				continue;
//			}else if(e.getValue().getItem() instanceof ItemFood) {
//				continue;
//			}
//			
//			JsonObject file = new JsonObject();
//			file.addProperty("type", "advancedlootableweapons:alloy_furnace");
//			file.addProperty("group", "advancedlootableweapons:alloy_furnace");
//			JsonArray ingrs = new JsonArray();
//			JsonObject item1 = new JsonObject();
//			item1.addProperty("item", e.getKey().getItem().getRegistryName().toString());
//			if(e.getKey().getCount() > 1) {
//				item1.addProperty("count", e.getKey().getCount());
//			}
//			if(e.getKey().getItemDamage() > 0) {
//				item1.addProperty("data", e.getKey().getItemDamage());
//			}
//			ingrs.add(item1);
//			ingrs.add(item1);
//			file.add("ingredients", ingrs);
//			file.addProperty("exp", FurnaceRecipes.instance().getSmeltingExperience(e.getValue()));
//			
//			JsonObject result = new JsonObject();
//			result.addProperty("item", e.getValue().getItem().getRegistryName().toString());
//			result.addProperty("count", e.getValue().getCount() * 2);
//			
//			if(e.getValue().getItemDamage() > 0) {
//				result.addProperty("data", e.getValue().getItemDamage());
//			}
//			file.add("result", result);
//			try {
//				String path = "C:/Users/alexx/Documents/GitHub/AdvancedLootableWeapons/testrecipes/alloying_" + e.getValue().getItem().getRegistryName().getResourcePath() + ".json";
//				File makeFile = new File(path);
//				if(!makeFile.exists()) {
//					makeFile.createNewFile();
//				}
//				PrintWriter writer = new PrintWriter(new FileWriter(path));
//				writer.write(gson.toJson(file));
//				writer.close();
//			}catch(IOException exc) {
//				exc.printStackTrace();
//			}
//		}
	}
	
	public static void serverRegistries(FMLServerStartingEvent event)
	{
		Alw.proxy.registerCommands(event);
	}
}
