package tiki.advancedlootabletweaker.compat.contenttweaker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.teamacronymcoders.contenttweaker.api.ctobjects.resourcelocation.CTResourceLocation;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tiki.advancedlootabletweaker.Alt;
import tiki.advancedlootableweapons.compat.crafttweaker.ZenDynamicAlwResources;

@ZenClass("mods.advancedlootabletweaker.AlwContentTweaker")
@ZenRegister
public class DynamicAltResources {
	
	public static JsonObject FORGE_MODEL;
	public static JsonObject ADVANCED_FORGE_MODEL;
	public static final short FORGE_MODEL_TYPE = 0;
	public static final short ADVANCED_FORGE_MODEL_TYPE = 1;
	
	@SideOnly(Side.CLIENT)
	public static void init() {
		try {
			InputStream ForgeFile = DynamicAltResources.class.getClassLoader().getResourceAsStream("assets/advancedlootableweapons/models/block/block_forge.json");
			FORGE_MODEL = new JsonParser().parse(new JsonReader(new BufferedReader(new InputStreamReader(ForgeFile, "Cp1252")))).getAsJsonObject();
		} catch (JsonIOException|JsonSyntaxException|UnsupportedEncodingException e) {
			Alt.logger.error("Unable to load forge model on initialization. This will result in ContentTweaker made forges having broken models. Please ensure you're using the latest version of ALW. If you are, please report this to the mod author!", e);
		}
		
		try {
			InputStream AdvForgeFile = DynamicAltResources.class.getClassLoader().getResourceAsStream("assets/advancedlootableweapons/models/block/block_advanced_forge.json");
			ADVANCED_FORGE_MODEL = new JsonParser().parse(new JsonReader(new BufferedReader(new InputStreamReader(AdvForgeFile, "Cp1252")))).getAsJsonObject();
		} catch (JsonIOException|JsonSyntaxException|UnsupportedEncodingException e) {
			Alt.logger.error("Unable to load advanced forge model on initialization. This will result in ContentTweaker made forges having broken models. Please ensure you're using the latest version of ALW. If you are, please report this to the mod author!", e);
		}
	}
	
	@ZenMethod
	public static ForgingGuiRepresentation setGuiForAnvil(String block, CTResourceLocation texture, int[] inputs, int[] output) {
		if(inputs.length != 4 || output.length != 2) {
			Alt.logger.error("An instance of forging gui has been given an insufficient amount of slot coordinates, not creating GUI.");
			return null;
		}
		
		Block forge = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
		if(forge == null) {
			Alt.logger.error("Unable to set gui for block {" + block + "}. {" + block + "} is not in the Forge Block registry.");
			return null;
		}
		
		ForgingGuiRepresentation gui = new ForgingGuiRepresentation(texture.getInternal(), inputs, output, forge);
		//ZenDynamicAlwResources.guiLists.put(forge, gui); // should happen when the GUI is registered now
		return gui;
	}
	
	@ZenMethod
	public static ForgingGuiRepresentation setGuiForAnvil(String block) {
		Block forge = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
		if(forge == null) {
			Alt.logger.error("Unable to set gui for block {" + block + "}. {" + block + "} is not in the Forge Block registry.");
			return null;
		}
		
		ForgingGuiRepresentation gui = new ForgingGuiRepresentation(ZenDynamicAlwResources.FORGE_WEAPON_TEXTURES, new int[] {56, 33, 56, 53}, new int[] {114, 43}, forge);
		//ZenDynamicAlwResources.guiLists.put(forge, gui); // should happen when the GUI is registered now
		return gui;
	}
	
	@ZenMethod
	public static ForgeRepresentation createForge(String name, boolean useOneSlot, boolean usesAdvancedForgeModel, boolean needsFuel, boolean needsIgnition) {
		ForgeRepresentation forge = new ForgeRepresentation(name, useOneSlot, usesAdvancedForgeModel, needsFuel, needsIgnition);
		forge.setTextureLocation(CTResourceLocation.create("contenttweaker:blocks/" + name + ".png"));
		return forge;
	}
	
	@ZenMethod
	public static int[] getButtonPosByNameInDefaultGui(String button) {
		if(button.equalsIgnoreCase("dagger")) {
			return new int[] {1, 1};
		}else if(button.equalsIgnoreCase("kabutowari")) {
			return new int[] {22, 1};
		}else if(button.equalsIgnoreCase("talwar")) {
			return new int[] {64, 1};
		}else if(button.equalsIgnoreCase("rapier")) {
			return new int[] {43, 1};
		}else if(button.equalsIgnoreCase("mace")) {
			return new int[] {1, 22};
		}else if(button.equalsIgnoreCase("cleaver")) {
			return new int[] {22, 22};
		}else if(button.equalsIgnoreCase("staff")) {
			return new int[] {43, 22};
		}else if(button.equalsIgnoreCase("longsword")) {
			return new int[] {64, 22};
		}else if(button.equalsIgnoreCase("kodachi")) {
			return new int[] {1, 43};
		}else if(button.equalsIgnoreCase("battleaxe")) {
			return new int[] {22, 43};
		}else if(button.equalsIgnoreCase("zweihander")) {
			return new int[] {43, 43};
		}else if(button.equalsIgnoreCase("nodachi")) {
			return new int[] {64, 43};
		}else if(button.equalsIgnoreCase("sabre")) {
			return new int[] {1, 64};
		}else if(button.equalsIgnoreCase("makhaira")) {
			return new int[] {22, 64};
		}else if(button.equalsIgnoreCase("spear")) {
			return new int[] {43, 64};
		}else if(button.equalsIgnoreCase("chain")) {
			return new int[] {22, 85};
		}else if(button.equalsIgnoreCase("armor plate")) {
			return new int[] {43, 85};
		}else if(button.equalsIgnoreCase("handle")) {
			return new int[] {64, 64};
		}else if(button.equalsIgnoreCase("forge weapon")) {
			return new int[] {1, 85};
		}
		return new int[] {232, 232};
	}
	
}