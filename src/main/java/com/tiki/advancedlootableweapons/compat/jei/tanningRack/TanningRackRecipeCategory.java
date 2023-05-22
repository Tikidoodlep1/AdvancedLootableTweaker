package com.tiki.advancedlootableweapons.compat.jei.tanningRack;

import com.tiki.advancedlootableweapons.ModInfo;
import com.tiki.advancedlootableweapons.compat.jei.RecipeCategories;
import com.tiki.advancedlootableweapons.init.BlockInit;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class TanningRackRecipeCategory extends AbstractTanningRackRecipeCategory<TanningRackRecipe> {

	private final IDrawable background;
	private final String name;
	private final IGuiHelper helper;
	
	public TanningRackRecipeCategory(IGuiHelper helper) {
		super(helper);
		this.helper = helper;
		background = helper.createDrawable(TEXTURES, 48, 24, 134 - 48, 57 - 24);
		name = "Tanning Rack";
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, TanningRackRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(input1, true, 5, 9);
		stacks.init(output, false, 59, 9);
		stacks.set(ingredients);
	}
	
	@Override
	public IDrawable getIcon() {
		return helper.createDrawableIngredient(new ItemStack(BlockInit.tanning_rack));
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public String getTitle() {
		return name;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
	}

	@Override
	public String getUid() {
		return RecipeCategories.TANNINGRACK;
	}

	@Override
	public String getModName() {
		return ModInfo.NAME;
	}

}
