package com.tiki.advancedlootableweapons.blocks.tileentities;

import java.util.Random;

import javax.annotation.Nullable;

import com.tiki.advancedlootableweapons.init.BlockInit;
import com.tiki.advancedlootableweapons.recipes.ShapelessOneSlotRecipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityJawCrusher extends TileEntity implements ITickable, IInventory {

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
	private String customName;
	private Random rand = new Random();
	
	public boolean crushContents() {
		ShapelessOneSlotRecipes recipe = findMatchingRecipe(this.inventory, this.getWorld());
		if(recipe != null) {
			NonNullList<ItemStack> leftovers = recipe.getRemainingItems(this.inventory);
			ItemStack result = recipe.getCraftingResult(this.inventory);
			int count = result.getCount() + rand.nextInt(3) - 1;
			int cost = 0;
			result.setCount(count < 1 ? 1 : count);
			if((result.getItem() == this.inventory.get(1).getItem() && this.inventory.get(1).getCount() + result.getCount() < this.getInventoryStackLimit()) || this.inventory.get(1) == ItemStack.EMPTY) {
				for(Ingredient i : recipe.getIngredients()) {
					for(ItemStack stack : i.getMatchingStacks()) {
						if(this.inventory.get(0).getItem() == stack.getItem()) {
							cost = stack.getCount();
							break;
						}
					}
				}
				
				for(int i = 0; i < this.inventory.size(); i++) {
					if(i == 1) {
						continue;
					}
					
					if(i == 0 && this.inventory.get(i).getCount() > cost) {
						this.inventory.get(i).setCount(this.inventory.get(i).getCount() - cost);
					}else {
						this.setInventorySlotContents(i, leftovers.get(i));
					}
				}
				
				if(this.inventory.get(1).getItem() == result.getItem()) {
					result.setCount(result.getCount() + this.inventory.get(1).getCount());
				}
				
				this.setInventorySlotContents(1, result);
				return true;
			}
		}
		
		return false;
	}
	
	@Nullable
    private ShapelessOneSlotRecipes findMatchingRecipe(NonNullList<ItemStack> craftMatrix, World worldIn)
    {
        for (IRecipe irecipe : CraftingManager.REGISTRY)
        {
            if (irecipe instanceof ShapelessOneSlotRecipes)
            {
            	if(((ShapelessOneSlotRecipes)irecipe).block == BlockInit.crusher && ((ShapelessOneSlotRecipes)irecipe).matches(craftMatrix, worldIn)) {
            		return (ShapelessOneSlotRecipes)irecipe;
            	}
            }
        }

        return null;
    }
	
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container_jaw_crusher";
	}
	
	public boolean hasCustomName() 
	{
		return this.customName != null && !this.customName.isEmpty();
	}
	
	public void setCustomName(String customName) 
	{
		this.customName = customName;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.jaw_crusher");
	}
	
	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack: this.inventory) {
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return (ItemStack)this.inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack)this.inventory.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.inventory.set(index, stack);
		
		if(stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if(index == 0 && !flag) {
			this.markDirty();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		
		if(compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory);
		
		if(this.hasCustomName()) compound.setString("CustomName", this.customName);
		return compound;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		if(this.world.getTileEntity(this.pos) != this) {
			return false; 
		}else if(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 1) {
			return false;
		}
		if(stack.getItem() == Item.getItemFromBlock(Blocks.STONE) && (stack.getItemDamage() == 1 || stack.getItemDamage() == 3)) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public void update() {}

}
