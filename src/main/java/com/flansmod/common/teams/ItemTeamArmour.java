package com.flansmod.common.teams;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.flansmod.common.FlansMod;
import com.flansmod.common.types.IFlanItem;
import com.flansmod.common.types.InfoType;
import com.google.common.collect.Multimap;

import net.fexcraft.mod.lib.api.item.IItem;
import net.fexcraft.mod.lib.util.entity.EntUtil;
import net.fexcraft.mod.lib.util.item.ItemUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTeamArmour extends ItemArmor implements ISpecialArmor, IFlanItem, IItem
{
	public ArmourType type;
	private static final UUID[] uuid = new UUID[] { UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() };
	
	public ItemTeamArmour(ArmourType t)
	{
		super(ItemArmor.ArmorMaterial.LEATHER, 0, EntUtil.getEquipmentSlotAl(t.type));
		type = t;
		type.item = this;
		setCreativeTab(FlansMod.tabFlanTeams);
		//GameRegistry.registerItem(this, type.shortName, FlansMod.MODID);
		ItemUtil.register(FlansMod.MODID, this);
		ItemUtil.registerRender(this);
	}
	
	public ItemTeamArmour(ItemArmor.ArmorMaterial armorMaterial, int renderIndex, int armourType) 
	{
		super(armorMaterial, renderIndex, EntUtil.getEquipmentSlotAl(armourType));
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) 
	{
		return new ArmorProperties(1, type.defence, Integer.MAX_VALUE);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) 
	{
		return (int)(type.defence * 20);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) 
	{
		//Do nothing to the armour. It should not break as that would leave the player's team ambiguous
	}

	//TODO @Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String s) 
	{
		return "flansmod:armor/" + type.armourTextureName + "_" + (type.type == 2 ? "2" : "1") + ".png";
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean b)
	{
		if(type.description != null)
		{
			Collections.addAll(lines, type.description.split("_"));
		}
		if(Math.abs(type.jumpModifier - 1F) > 0.01F)
			lines.add("\u00a73+" + (int)((type.jumpModifier - 1F) * 100F) + "% Jump Height");
		if(type.smokeProtection)
			lines.add("\u00a72+Smoke Protection");
		if(type.nightVision)
			lines.add("\u00a72+Night Vision");
		if(type.negateFallDamage)
			lines.add("\u00a72+Negates Fall Damage");
	}
	
	//TODO @Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return type.colour;
	}
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
       	Multimap map = super.getItemAttributeModifiers(equipmentSlot);
       	map.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getAttributeUnlocalizedName(), new AttributeModifier(uuid[type.type], "KnockbackResist", type.knockbackModifier, 0));
       	map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(uuid[type.type], "MovementSpeed", type.moveSpeedModifier - 1F, 2));
       	return map;
    }
    
    //TODO @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        return type.model;
    }
    
	@Override
	public InfoType getInfoType() 
	{
		return type;
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		if(type.nightVision && FlansMod.ticker % 25 == 0)
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("night_vision"), 250));
		if(type.jumpModifier > 1.01F && FlansMod.ticker % 25 == 0)
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("jump_boost"), 250, (int)((type.jumpModifier - 1F) * 2F), true, false));
		if(type.negateFallDamage)
			player.fallDistance = 0F;
	}

	@Override
	public String getName(){
		return type.shortName;
	}

	@Override
	public int getVariantAmount(){
		return default_variant;
	}
}
