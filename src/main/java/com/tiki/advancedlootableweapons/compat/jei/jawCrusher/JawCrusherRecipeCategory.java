package com.tiki.advancedlootableweapons.compat.jei.jawCrusher;

import com.tiki.advancedlootableweapons.ModInfo;
import com.tiki.advancedlootableweapons.compat.jei.RecipeCategories;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;

public class JawCrusherRecipeCategory extends AbstractJawCrusherRecipeCategory<JawCrusherRecipe> {

	private final IDrawable background;
	private final String name;
	
	public JawCrusherRecipeCategory(IGuiHelper helper) {
		super(helper);
		background = helper.createDrawable(TEXTURES, 40, 22, 97, 60);
		name = "Jaw Crusher";
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JawCrusherRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(input1, true, 13, 20);
		stacks.init(output, false, 67, 20);
		stacks.set(ingredients);
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
		return RecipeCategories.JAWCRUSHER;
	}

	@Override
	public String getModName() {
		return ModInfo.NAME;
	}

}