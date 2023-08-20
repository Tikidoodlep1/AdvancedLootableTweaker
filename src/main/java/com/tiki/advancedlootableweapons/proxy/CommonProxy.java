package com.tiki.advancedlootableweapons.proxy;

import java.util.List;
import java.util.Random;
import java.util.Arrays;

import com.tiki.advancedlootableweapons.Alw;
import com.tiki.advancedlootableweapons.ModInfo;
import com.tiki.advancedlootableweapons.commands.SeeToolMatsCommand;
import com.tiki.advancedlootableweapons.handlers.ConfigHandler;
import com.tiki.advancedlootableweapons.handlers.GlobalDropsHandler;
import com.tiki.advancedlootableweapons.handlers.SoundHandler;
import com.tiki.advancedlootableweapons.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
	
	public void addColoredItemRenderer() {}
	
	public void registerItemRenderer(Item item, int meta, String id) {}
	
	public void registerModel(Item item, int metadata) {}
	
	public void registerEntityRenders() {}
	
	public void registerCustomMeshesAndStateStuff() {}
	
	public void registerTESRs() {}
	
	public void modelBake(final ModelBakeEvent event) {}
	
	public void registerCustomModelLoaders() {};
	
	public void onTooltip(ItemTooltipEvent event) {}
	
	public void registerCommands(final FMLServerStartingEvent e) {
		e.registerServerCommand(new SeeToolMatsCommand());
	}
	
	public void onBlockAttemptBreak(final LeftClickBlock event){
		BlockPos blockPos = event.getPos();
		Block block = event.getWorld().getBlockState(blockPos).getBlock();
		if(ConfigHandler.VALID_ANVILS.contains(block.getRegistryName().toString())) {
			EntityPlayer player = event.getEntityPlayer();
			if(ConfigHandler.VALID_HAMMERS.contains(player.inventory.getCurrentItem().getItem().getRegistryName().toString())) {
				player.openGui(Alw.instance, ModInfo.GUI_FORGE_WEAPON, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
				event.setCanceled(true);
			}
		}
	}
	
	public void onEntityDrops(final LivingDropsEvent event) {
		Random rand = new Random();
		boolean containsLeather = false;
		int leatherCount = 0;
		for(EntityItem e : event.getDrops()) {
			if(e.getItem().getItem() == Items.LEATHER || e.getItem().getItem() == Items.RABBIT_HIDE) {
				leatherCount = e.getItem().getCount();
				containsLeather = true;
				event.getDrops().remove(e);
				break;
			}
		}
		if(containsLeather) {
			event.getEntity().entityDropItem(new ItemStack(ItemInit.UNTRIMMED_HIDE, (int)(leatherCount*1.25)+rand.nextInt(event.getLootingLevel() + 1)), 0.25F);
		}
		if((rand.nextInt(100) + 1) <= (ConfigHandler.SHADOW_DROP_RATE*100) && GlobalDropsHandler.getEntityMap().get(event.getEntity().getClass())) {
			Entity entity = event.getEntity();
			if(event.getLootingLevel() > 0) {
				entity.entityDropItem(new ItemStack(ItemInit.SHADOW, rand.nextInt(event.getLootingLevel() + 1)), 0.25F);
				entity.playSound(SoundHandler.SHADOW_DROP, 1.0F, 1.0F);
			}else {
				entity.entityDropItem(new ItemStack(ItemInit.SHADOW, 1), 0.25F);
				entity.playSound(SoundHandler.SHADOW_DROP, 1.0F, 1.0F);
			}
		}
	}

	public void onBlockDrops(final HarvestDropsEvent event) {
		Block block = event.getState().getBlock();
		if(block instanceof BlockObsidian) {
			if(event.isSilkTouching()) {
				return;
			}
			
			Random rand = new Random();
			List<ItemStack> drops = event.getDrops();
			int count;
			int max = 4;
			drops.clear();
			count = rand.nextInt(max) + 1;
			drops.add(new ItemStack(ItemInit.SHARD_OBSIDIAN, count));
		}
	}
}
