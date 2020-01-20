package com.aymegike.humineclaim.menu;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import com.aymegike.humineclaim.HumineClaim;

public class MenuClickEvent implements Listener
{
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		if(event.getView().getTitle().startsWith(ClaimMenuManager.CLAIM_MAIN_MENU_NAME))
		{
			if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
			{				
				if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Terrain :"))
				{
					String title[] = event.getCurrentItem().getItemMeta().getDisplayName().split(" : ");
					if (title.length > 1)
					{
						HumineClaim.getClaimMenuManager().openZoneMenuForZone((Player) event.getWhoClicked(), title[1]);
					}
				}

				if(event.getCurrentItem().isSimilar(ItemMenuBank.friendClaimsItem()))
				{
					HumineClaim.getClaimMenuManager().openFriendMenu((Player) event.getWhoClicked());
				}
				
				if(event.getCurrentItem().isSimilar(ItemMenuBank.itemQuit(true)))
				{
					HumineClaim.getClaimMenuManager().closeMenu((Player) event.getWhoClicked(), MENU_STEP.MAINMENU);
				}
			}
			event.setCancelled(true);
		}
		if(event.getView().getTitle().startsWith(ClaimMenuManager.FRIEND_MENU_NAME))
		{
			if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
			{
				if(event.getCurrentItem().isSimilar(ItemMenuBank.itemQuit(true)))
				{
					HumineClaim.getClaimMenuManager().closeMenu((Player) event.getWhoClicked(), MENU_STEP.FRIENDMENU);
				}
			}
			event.setCancelled(true);
		}		
		if(event.getView().getTitle().startsWith(ClaimMenuManager.CLAIM_ZONE_MENU_NAME))
		{
			if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
			{			
				if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Invité :"))
				{	
					NamespacedKey key = new NamespacedKey(HumineClaim.getPlugin(HumineClaim.class), "playerIDKey");
					String idString = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
					HumineClaim.getClaimMenuManager().openRemoveGuestMenu((Player) event.getWhoClicked(), UUID.fromString(idString));
				}
				
				if(event.getCurrentItem().isSimilar(ItemMenuBank.addGuestItem()))
				{
					HumineClaim.getClaimMenuManager().addGuestSelection((Player) event.getWhoClicked());
				}	
				
				if(event.getCurrentItem().isSimilar(ItemMenuBank.changeNameItem()))
				{
					HumineClaim.getClaimMenuManager().changeNameSelection((Player) event.getWhoClicked());
				}
				
				if(event.getCurrentItem().isSimilar(ItemMenuBank.itemQuit(false)))
				{
					HumineClaim.getClaimMenuManager().closeMenu((Player) event.getWhoClicked(), MENU_STEP.ZONEMENU);
				}
			}
			event.setCancelled(true);
		}	
		
		if(event.getView().getTitle().startsWith(ClaimMenuManager.REMOVE_GUEST_MENU_NAME))
		{
			if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
			{				
				if(event.getCurrentItem().isSimilar(ItemMenuBank.itemQuit(false)))
				{
					HumineClaim.getClaimMenuManager().closeMenu((Player) event.getWhoClicked(), MENU_STEP.GUESTMENU);
				}	
				//if(event.getCurrentItem().isSimilar(ItemMenuBank.cancelItem())) DON'T WORK ??
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Annuler"))
				{
					HumineClaim.getClaimMenuManager().closeMenu((Player) event.getWhoClicked(), MENU_STEP.GUESTMENU);
				}
				//if(event.getCurrentItem().isSimilar(ItemMenuBank.removeGuestItem())) DON'T WORK
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirmer la supression."))
				{
					HumineClaim.getClaimMenuManager().removeGuestValidation((Player) event.getWhoClicked(), event.getClickedInventory());
				}
			}
			event.setCancelled(true);
		}		
	}
}
