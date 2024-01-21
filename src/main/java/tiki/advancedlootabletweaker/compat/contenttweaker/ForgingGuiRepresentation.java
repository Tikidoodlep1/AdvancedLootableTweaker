package tiki.advancedlootabletweaker.compat.contenttweaker;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;


import com.teamacronymcoders.contenttweaker.api.ctobjects.resourcelocation.CTResourceLocation;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tiki.advancedlootabletweaker.Alt;
import tiki.advancedlootableweapons.compat.crafttweaker.CrTForgingGuiRepresentation;
import tiki.advancedlootableweapons.compat.crafttweaker.ForgingGuiButtonRepresentation;
import tiki.advancedlootableweapons.compat.crafttweaker.ZenDynamicAlwResources;

@ZenClass("mods.advancedlootabletweaker.ForgingGuiRepresentation")
@ZenRegister
public class ForgingGuiRepresentation {

	public final HashSet<Point> slots = new HashSet<Point>();
	public final List<ForgingGuiButtonRepresentation> buttons = new ArrayList<ForgingGuiButtonRepresentation>();
	public final ResourceLocation textureLocation;
	public final Block block;
	private static int buttonIDIter = 102;
	
	public ForgingGuiRepresentation(ResourceLocation texture, int[] inputs, int[] output, Block block) {
		this.textureLocation = texture;
		this.slots.add(new Point(inputs[0], inputs[1]));
		this.slots.add(new Point(inputs[2], inputs[3]));
		this.slots.add(new Point(output[0], output[1]));
		this.block = block;
	}
	
	public HashSet<Point> getSlots() {
		return slots;
	}

	public ResourceLocation getTextureLocation() {
		return textureLocation;
	}
	
	@ZenMethod
	public CTResourceLocation getCTTextureLocation() {
		return CTResourceLocation.create(this.textureLocation.toString());
	}
	
	@ZenMethod
	public boolean addButton(String buttonName, @Nullable CTResourceLocation overlay, int defaultX, int defaultY, int overlayX, int overlayY) {
		return buttons.add(new ForgingGuiButtonRepresentation(buttonIDIter++, buttonName, defaultX, defaultY, overlay.getInternal(), overlayX, overlayY));
	}
	
	@ZenMethod
	public boolean addButton(String buttonName, int defaultX, int defaultY, int overlayX, int overlayY) {
		return buttons.add(new ForgingGuiButtonRepresentation(buttonIDIter++, buttonName, defaultX, defaultY, null, overlayX, overlayY));
	}
	
	@ZenMethod
	public boolean addButton(String buttonName, @Nullable CTResourceLocation overlay, int overlayX, int overlayY) {
		return buttons.add(new ForgingGuiButtonRepresentation(buttonIDIter++, buttonName, 0, 0, overlay.getInternal(), overlayX, overlayY));
	}
	
	@ZenMethod
	public boolean addButton(String buttonName, int overlayX, int overlayY) {
		return buttons.add(new ForgingGuiButtonRepresentation(buttonIDIter++, buttonName, 0, 0, null, overlayX, overlayY));
	}
	
	@ZenMethod
	public boolean register() {
		CrTForgingGuiRepresentation rep = new CrTForgingGuiRepresentation(this.textureLocation, this.slots, this.block.getRegistryName());
		if(!rep.buttons.addAll(this.buttons)) {
			Alt.logger.error("Cannot add buttons to GUI! Please report the to the mod author!", new IllegalStateException());
			return false;
		}
		ZenDynamicAlwResources.guiLists.put(this.block.getRegistryName(), rep);
		return true;
	}
}
