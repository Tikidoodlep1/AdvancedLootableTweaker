package com.tiki.advancedlootableweapons.tools;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tiki.advancedlootableweapons.Alw;
import com.tiki.advancedlootableweapons.IHasModel;
import com.tiki.advancedlootableweapons.armor.ArmorBonusesBase;
import com.tiki.advancedlootableweapons.init.ItemInit;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolSlashSword extends ItemSword implements IHasModel{
	
	private float attackDamage;
	private double attackSpeed;
	private final ToolMaterial material;
	private double bonusDamage;
	private float reach;
	private boolean rand;
	private float randDamage;
	private float tempRandDamage;
	private String[] randName1 = new String[] {"Repuslor", "Balmung", "Gram", "Arondight", "Caladbolg", "Chandrahas", "Colada", "Mors", "Durendal", "Ecke", "Hauteclere", "Mimung", "Naegling", "Tizona", "Tyrfing", "Zulfiqar"};
	private String[] randName2 = new String[] {"Lucent", "Lambent", "Dark", "Dusk", "Aphotic", "Radiant", "Scintillant", "Vacuous", "Nixing", "Abnegating", "Collector of Heads,", "Triumphant"};
	private Random randGen = new Random();
	private NBTTagCompound nbt = new NBTTagCompound();
	
	public ToolSlashSword(String name, ToolMaterial material, String type) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Alw.AlwTab);
		
		ItemInit.items.add(this);
		
		this.material = material;
		this.setMaxDamage(material.getMaxUses());
		this.maxStackSize = 1;
		this.bonusDamage = 0;
		this.getAttributes(type, material);
		this.randDamage = (((float)randGen.nextInt(14)) * (this.material.getAttackDamage() / 100)) + randGen.nextFloat();
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		NBTTagCompound tag = new NBTTagCompound();
		tag = stack.getTagCompound();
		int durability;
		if(tag == null || this.nbt == null || (this.material.getMaxUses() - tag.getInteger("maxDurability")) <= 0) {
			durability = 1;
			return durability;
		}else {
			durability = tag.getInteger("maxDurability");
			return this.material.getMaxUses() - durability;
		}
	}
	
	public void setMaximumDamage(ItemStack stack, int maxDamage) {
		NBTTagCompound tag = stack.getTagCompound();
		this.nbt.setInteger("maxDurability", maxDamage);
		System.out.println("durability is: " + this.nbt.getInteger("maxDurability") + ", " + maxDamage);
		System.out.println("Tag durability is: " + tag.getInteger("reducedDurability"));
		System.out.println(tag.getKeySet());
		System.out.println("total durability is: " + (this.material.getMaxUses() + this.nbt.getInteger("maxDurability")));
		stack.setTagCompound(this.nbt);
	}
	
	private void getAttributes(String type, ToolMaterial material) {		
		switch(type){
			case "longsword":
				this.attackSpeed = -1.4D;
				this.attackDamage = 3.5F + material.getAttackDamage();
				this.reach = 6.0F;
				break;
			case "kodachi":
				this.attackSpeed = 0.0D;
				this.attackDamage = 0.75F + material.getAttackDamage();
				this.reach = 4.8F;
				break;
			case "battleaxe":
				this.attackSpeed = -1.7D;
				this.attackDamage = 4.0F + material.getAttackDamage();
				this.reach = 6.2F;
				break;
			case "zweihander":
				this.attackSpeed = -2.4D;
				this.attackDamage = 3.75F + material.getAttackDamage();
				this.reach = 6.4F;
				break;
			case "nodachi":
				this.attackSpeed = -1.8D;
				this.attackDamage = 3.65F + material.getAttackDamage();
				this.reach = 5.8F;
				break;
			case "sabre":
				this.attackSpeed = -0.4D;
				this.attackDamage = 3.0F + material.getAttackDamage();
				this.reach = 5.4F;
				break;
			case "makhaira":
				this.attackSpeed = -0.8D;
				this.attackDamage = 2.75F + material.getAttackDamage();
				this.reach = 5.6F;
		}
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn){
		this.rand = randGen.nextBoolean();
		NBTTagCompound nbt = new NBTTagCompound();
		
		if(this.rand == true) {
			stack.setStackDisplayName(TextFormatting.AQUA + randName2[randGen.nextInt(12)] + " " +  randName1[randGen.nextInt(16)]);
		}else {
			stack.setStackDisplayName(TextFormatting.AQUA + randName1[randGen.nextInt(16)]);
		}
		
		if(randGen.nextBoolean() == true) {
			this.randDamage = (((float)randGen.nextInt(14)) * (this.material.getAttackDamage() / 100)) + randGen.nextFloat();
		}else{
			this.randDamage = (((float)randGen.nextInt(14)) * (this.material.getAttackDamage() / 100)) + randGen.nextFloat();
			this.randDamage += (this.randDamage * 2);
		}
		
		nbt.setFloat("bonusDamage", this.randDamage);
		this.tempRandDamage = nbt.getFloat("bonusDamage");
		stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(Alw.BONUS_ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.tempRandDamage, 0), EntityEquipmentSlot.MAINHAND);
		stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)(this.getAttackDamage() + 1), 0), EntityEquipmentSlot.MAINHAND);
		stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0), EntityEquipmentSlot.MAINHAND);
        stack.addAttributeModifier(Alw.ATTACK_RANGE.getName(), new AttributeModifier(Alw.ATTACK_RANGE_MODIFIER, "weapon modifier", (double)this.getReach() - 5.0D, 0), EntityEquipmentSlot.MAINHAND);
        stack.setTagCompound(this.nbt);
	}
	
	public void generateNameAndModifiers(ItemStack stack, double reducedDamage) {
		this.rand = randGen.nextBoolean();
		
		if(this.rand == true) {
			stack.setStackDisplayName(TextFormatting.AQUA + randName2[randGen.nextInt(12)] + " " +  randName1[randGen.nextInt(16)]);
		}else {
			stack.setStackDisplayName(TextFormatting.AQUA + randName1[randGen.nextInt(16)]);
		}
		
		if(randGen.nextBoolean() == true) {
			this.randDamage = (((float)randGen.nextInt(14)) * (this.material.getAttackDamage() / 100)) + randGen.nextFloat();
		}else{
			this.randDamage = (((float)randGen.nextInt(14)) * (this.material.getAttackDamage() / 100)) + randGen.nextFloat();
			this.randDamage += (this.randDamage * 2);
		}
		
		this.nbt.setFloat("bonusDamage", this.randDamage);
		this.tempRandDamage = this.nbt.getFloat("bonusDamage");
		stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)((this.getAttackDamage() + 1) - reducedDamage), 0), EntityEquipmentSlot.MAINHAND);
		stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0), EntityEquipmentSlot.MAINHAND);
		stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(Alw.BONUS_ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.tempRandDamage, 0), EntityEquipmentSlot.MAINHAND);
		stack.addAttributeModifier(Alw.ATTACK_RANGE.getName(), new AttributeModifier(Alw.ATTACK_RANGE_MODIFIER, "weapon modifier", (double)this.getReach() - 5.0D, 0), EntityEquipmentSlot.MAINHAND);
		stack.setTagCompound(this.nbt);
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.GRAY + "" + (this.attackSpeed + 4) + " Attack Speed");
		
    }

	@Override
	public void registerModels() {
		Alw.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0));
            multimap.put(Alw.ATTACK_RANGE.getName(), new AttributeModifier(Alw.ATTACK_RANGE_MODIFIER, "weapon modifier", (double)this.getReach() - 5.0D, 0));
        }
        
        return multimap;
    }
	
	public float getAttackDamage(){
		float x = this.attackDamage;
        return x;
    }
	
	public float getReach() {
		return reach;
	}
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
		Iterable<ItemStack> armorlist = attacker.getArmorInventoryList();
		ArmorBonusesBase armor;
		for(ItemStack i: armorlist) {
			if(i.getItem() instanceof ArmorBonusesBase) {
				armor = (ArmorBonusesBase)i.getItem();
				if(armor.armorType == EntityEquipmentSlot.CHEST) {
					this.bonusDamage = armor.getBonusAttackDamage();
					target.setHealth(target.getHealth() - (float)this.bonusDamage);
					break;
				}
			}else {
				this.bonusDamage = 0.0D;
				target.setHealth(target.getHealth() - (float)this.bonusDamage);
			}
		}
		
        stack.damageItem(1, attacker);
        //target.onDeath(DamageSource.causePlayerDamage((EntityPlayer)attacker));
        return true;
    }
	
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemStack mat = this.material.getRepairItemStack();
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }
	
	public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }
	
	public String getToolMaterialName()
    {
        return this.material.toString();
    }
	
	public ToolMaterial getToolMaterial() {
		return this.material;
	}
	
	@SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
}
