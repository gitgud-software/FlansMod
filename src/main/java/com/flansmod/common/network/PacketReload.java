package com.flansmod.common.network;

import com.flansmod.common.FlansMod;
import com.flansmod.common.PlayerData;
import com.flansmod.common.PlayerHandler;
import com.flansmod.common.guns.GunType;
import com.flansmod.common.guns.ItemGun;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//When the client receives one, it "reloads". Basically to stop client side recoil effects when the gun should be in a reload animation
//When the server receives one, it is interpreted as a forced reload
public class PacketReload extends PacketBase 
{
	public boolean isOffHand;
	public boolean isForced;
	
	public PacketReload() {}
	
	public PacketReload(boolean isOffHand, boolean isForced) 
	{
		this.isOffHand = isOffHand;
		this.isForced = isForced;
	}
		
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		data.writeBoolean(isOffHand);
		data.writeBoolean(isForced);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		isOffHand = data.readBoolean();
		isForced = data.readBoolean();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		PlayerData data = PlayerHandler.getPlayerData(playerEntity);
		ItemStack stack = playerEntity.getHeldItemMainhand();
		if(isOffHand && data.offHandGunSlot != 0)
		{
			stack = playerEntity.inventory.getStackInSlot(data.offHandGunSlot - 1);
			playerEntity.inventory.currentItem = data.offHandGunSlot - 1;
		}
		if(data != null && stack != null && stack.getItem() instanceof ItemGun)
		{
			GunType type = ((ItemGun)stack.getItem()).GetType();
			
			if(((ItemGun)stack.getItem()).Reload(stack, playerEntity.worldObj, playerEntity, playerEntity.inventory, isOffHand, data.offHandGunSlot != 0, isForced, playerEntity.capabilities.isCreativeMode))
			{
				//Set the reload delay
				data.shootTimeRight = data.shootTimeLeft = type.reloadTime;
				if(isOffHand)
					data.reloadingLeft = true;
				else data.reloadingRight = true;
				//Play reload sound
				if(type.reloadSound != null)
					PacketPlaySound.sendSoundPacket(playerEntity.posX, playerEntity.posY, playerEntity.posZ, FlansMod.soundRange, playerEntity.dimension, type.reloadSound, false);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		FlansMod.log("Recieved reload packet on client!");
	}
}
