package com.flansmod.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class TileEntityItemHolder extends TileEntity implements IInventory//, ITickable
{
	private ItemStack stack;
	public ItemHolderType type;
	
	public TileEntityItemHolder()
	{
		
	}
	
	public TileEntityItemHolder(ItemHolderType type)
	{
		this.type = type;
	}
	
	@Override
	public String getName() { return "ItemHolder"; }

	@Override
	public boolean hasCustomName() { return false; }

	@Override
	public int getSizeInventory() { return 1; }

	@Override
	public ItemStack getStackInSlot(int index) { return getStack(); }

	@Override
	public ItemStack decrStackSize(int index, int count) { if(getStack() != null) { getStack().stackSize -= count; if(getStack().stackSize <= 0) setStack(null); } return getStack(); }

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) { this.setStack(stack); }

	@Override
	public int getInventoryStackLimit() { return 64; }

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) { return true; }

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) { return true; }

	@Override
	public int getField(int id) { return 0; }

	@Override
	public void setField(int id, int value) { }

	@Override
	public int getFieldCount() { return 0; }

	@Override
	public void clear() { }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		NBTTagCompound stackNBT = new NBTTagCompound();
		if(getStack() != null)
			getStack().writeToNBT(stackNBT);
		nbt.setTag("stack", stackNBT);		
		nbt.setString("type", type.shortName);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		setStack(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("stack")));
		type = ItemHolderType.getItemHolder(nbt.getString("type"));
	}

	/*@Override
	public void update() 
	{
		//why implementing it, when not using It?
	}*/
	
	@Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), nbt);
    }
	
	@Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, SPacketUpdateTileEntity packet)
    {
		readFromNBT(packet.getNbtCompound());
    }

	public ItemStack getStack() 
	{
		return stack;
	}

	public void setStack(ItemStack stack) 
	{
		this.stack = stack;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index){ return getStackInSlot(index); }
}
