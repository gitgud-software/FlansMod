package com.flansmod.common.teams;

import com.flansmod.common.CraftingInstance;
import com.flansmod.common.FlansMod;
import com.flansmod.common.teams.ArmourBoxType.ArmourBoxEntry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockArmourBox extends Block
{
	public ArmourBoxType type;
	
	public BlockArmourBox(ArmourBoxType t) 
	{
		super(Material.WOOD);
		type = t;

		setUnlocalizedName(type.shortName);
		setHardness(2F);
		setResistance(4F);
		GameRegistry.registerBlock(this, type.shortName);
		setCreativeTab(FlansMod.tabFlanTeams);
		type.block = this;
		type.item = Item.getItemFromBlock(this);
	}
	
	public void buyArmour(String shortName, int piece, InventoryPlayer inventory)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			FlansMod.proxy.buyArmour(shortName, piece, type);
		} 
		ArmourBoxEntry entryPicked = null;
		for(ArmourBoxEntry page : type.pages)
		{
			if(page.shortName.equals(shortName))
				entryPicked = page;
		}
		
		ItemStack resultStack = new ItemStack(entryPicked.armours[piece].item);
		
		CraftingInstance crafting = new CraftingInstance(inventory, entryPicked.requiredStacks[piece], resultStack);
		if(crafting.canCraft())
		{
			crafting.craft(inventory.player);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(entityplayer.isSneaking())
			return false;
		entityplayer.openGui(FlansMod.INSTANCE, 11, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
