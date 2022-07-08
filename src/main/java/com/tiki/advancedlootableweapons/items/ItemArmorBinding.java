package com.tiki.advancedlootableweapons.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorBinding extends ItemBase {

	private final int extraDur;
	
	public ItemArmorBinding(String name, int extraDur) {
		super(name);
		this.extraDur = extraDur;
	}
	
	public int getExtraDur() {
		return this.extraDur;
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.BLUE + "Extra Durability: " + this.extraDur);
    }

}
