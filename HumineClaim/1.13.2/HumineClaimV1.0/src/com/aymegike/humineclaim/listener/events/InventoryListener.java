package com.aymegike.humineclaim.listener.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onItemMove(InventoryMoveItemEvent e)
	{
		if (isInZoneClaim(e.getDestination().getLocation()) && isBlackList(e.getItem())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHopperSuck(InventoryPickupItemEvent e) {
		if (isInZoneClaim(e.getItem().getLocation())) {
			if (e.getInventory().getType() == InventoryType.HOPPER) {
				if (isBlackList(e.getItem().getItemStack())) {
					e.setCancelled(true);
				}
			}
		}
		
	}
	
	@EventHandler
	public void onItemDrag(InventoryClickEvent e)
	{
		if (e.getInventory().getType() != InventoryType.CRAFTING)
		{			
			if (isInZoneClaim(e.getWhoClicked().getLocation()) )
			{
				if (e.getInventory().getType() != InventoryType.SHULKER_BOX)
				{				
					if (e.getCurrentItem() != null
							&& (HumineClaim.getZoneClaimManager().isLimitedItem(e.getCurrentItem().getType().name())
							|| HumineClaim.getZoneClaimManager().isForbidenItem(e.getCurrentItem().getType().name()))
					)
					{
						Bukkit.getPlayer(e.getWhoClicked().getName()).sendMessage("Interdiction de poser ça ici !");
						e.setCancelled(true);
					}
				}
				else
				{
					if (e.getCurrentItem() != null && HumineClaim.getZoneClaimManager().isForbidenItem(e.getCurrentItem().getType().name()))
					{
						Bukkit.getPlayer(e.getWhoClicked().getName()).sendMessage("Interdiction de poser ça ici !");
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	private boolean isBlackList(ItemStack itemStack) {
		return isShulker(itemStack)
		|| itemStack.getType() == Material.DIAMOND_BLOCK
		|| itemStack.getType() == Material.GOLD_BLOCK
		|| itemStack.getType() == Material.IRON_BLOCK;
	}
	private boolean isShulker(ItemStack itemStack) {
		return itemStack.getType() == Material.SHULKER_BOX
				|| itemStack.getType() == Material.BLACK_SHULKER_BOX
				|| itemStack.getType() == Material.BLUE_SHULKER_BOX
				|| itemStack.getType() == Material.BROWN_SHULKER_BOX
				|| itemStack.getType() == Material.CYAN_SHULKER_BOX
				|| itemStack.getType() == Material.GRAY_SHULKER_BOX
				|| itemStack.getType() == Material.GREEN_SHULKER_BOX
				|| itemStack.getType() == Material.LIGHT_BLUE_SHULKER_BOX
				|| itemStack.getType() == Material.LIGHT_GRAY_SHULKER_BOX
				|| itemStack.getType() == Material.LIME_SHULKER_BOX
				|| itemStack.getType() == Material.MAGENTA_SHULKER_BOX
				|| itemStack.getType() == Material.ORANGE_SHULKER_BOX
				|| itemStack.getType() == Material.PINK_SHULKER_BOX
				|| itemStack.getType() == Material.PURPLE_SHULKER_BOX
				|| itemStack.getType() == Material.RED_SHULKER_BOX
				|| itemStack.getType() == Material.WHITE_SHULKER_BOX
				|| itemStack.getType() == Material.YELLOW_SHULKER_BOX;
	}
	private boolean isInZoneClaim(Location location) {
		for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZonesClaims()) {
			if (zc.containLocation(location)) {
				return true;
			}
		}
		return false;
	}
	
}
