package com.flansmod.common;

import java.util.LinkedList;

import com.flansmod.common.guns.ItemGun;
import com.flansmod.common.teams.TeamsManager;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommonTickHandler 
{
	/** List for storing replacement EntityItemCustomRenderers. Stops concurrent modifications and messing up the entity list. */
	private LinkedList<EntityItemCustomRender> replacementItemEntities = new LinkedList<EntityItemCustomRender>();
	
	public CommonTickHandler()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent event)
	{
		switch(event.phase)
		{
		case START :
		{
			break;
		}
		case END :
		{
			FlansMod.playerHandler.clientTick();
			break;
		}		
		}
	}
	
	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event)
	{
		switch(event.phase)
		{
		case START :
		{
			//Handle all packets received since last tick
			FlansMod.getPacketHandler().handleServerPackets();

			//Spawn the replacement item entities for custom rendering
			while(replacementItemEntities.size() > 0)
			{
				EntityItemCustomRender entity = replacementItemEntities.remove();
				entity.worldObj.spawnEntityInWorld(entity);
			}

			break;
		}
		case END :
		{
			TeamsManager.getInstance().tick();
			FlansMod.playerHandler.serverTick();
			FlansMod.ticker++;
			break;
		}		
		}
	}

	
	public void onEntitySpawn(EntityJoinWorldEvent event) 
	{
		//Replace gun items with custom render gun items
		if(event.getEntity() instanceof EntityItem && !(event.getEntity() instanceof EntityItemCustomRender))
		{
			ItemStack stack = getEntityItem((EntityItem)event.getEntity());
			if(stack != null && stack.getItem() instanceof ItemGun && ((ItemGun)stack.getItem()).GetType().modelString != null)
			{
				//event.world.spawnEntityInWorld(new EntityItemCustomRender((EntityItem)event.entity));
				replacementItemEntities.add(new EntityItemCustomRender((EntityItem)event.getEntity()));
				event.setCanceled(true);
			}
		}			
	}
	
    public ItemStack getEntityItem(EntityItem entity)
    {
        return null;//TODO return entity.getDataWatcher().getWatchableObjectItemStack(10);
    }
}
