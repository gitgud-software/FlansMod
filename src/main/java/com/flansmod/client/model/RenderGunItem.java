package com.flansmod.client.model;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderGunItem extends RenderEntityItem 
{
	private RenderGun gunRenderer;
	
	public RenderGunItem(RenderManager renderManager, RenderItem renderItem, RenderGun gunRenderer) 
	{
		super(renderManager, renderItem);
		this.gunRenderer = gunRenderer;
	}
	
	/*@Override
    public void func_177075_a(EntityItem entity, double x, double y, double z, float p_177075_8_, float partialTicks)
    {
        ItemStack stack = entity.getEntityItem();
        
        if(stack.getItem() instanceof ItemGun && ((ItemGun)stack.getItem()).GetType().model != null)
        {
        	GlStateManager.pushMatrix();
        	GlStateManager.translate(x, y + 0.25D, z);
        	GlStateManager.rotate(entity.ticksExisted + partialTicks, 0F, 1F, 0F);
        	
        	gunRenderer.renderItem(ItemRenderType.ENTITY, stack);
        	GlStateManager.popMatrix();
        }
        else
        {
        	super.func_177075_a(entity, x, y, z, p_177075_8_, partialTicks);
        } 
    }*/
}
