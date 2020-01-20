package com.aymegike.humineclaim.menu;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;

enum MENU_STEP {MAINMENU, ZONEMENU, FRIENDMENU, GUESTMENU};

public class ClaimMenuManager {

	public static final String CLAIM_MAIN_MENU_NAME = "Terrains : Menu principal";
	public static final String CLAIM_ZONE_MENU_NAME = "Gérer le terrain";
	public static final String REMOVE_GUEST_MENU_NAME = "Retirer cet invité ?";
	public static final String FRIEND_MENU_NAME = "Terrains des amis";
		
	public ClaimMenuManager()
	{
	}
	
	public void openMainMenu(Player player)
	{
		if (player == null)
		{
			return;
		}
		
		Inventory inv;
		inv = Bukkit.createInventory(null, (9*5), CLAIM_MAIN_MENU_NAME);	
		
		inv.setItem(4, ItemMenuBank.claimOwnerItem(player));
		int nbClaim = 0;
		for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZoneClaimForPlayer(player))
		{
			inv.setItem(9+nbClaim, ItemMenuBank.claimItem(zc));
			nbClaim++;
		}
		
		inv.setItem(31, ItemMenuBank.friendClaimsItem());
		
		inv.setItem(inv.getSize() - 9, ItemMenuBank.itemQuit(true));
		inv.setItem(inv.getSize() - 1, ItemMenuBank.itemQuit(true));
		player.openInventory(inv);
	}
	
	public void openFriendMenu(Player player)
	{
		if (player == null)
		{
			return;
		}
		
		Inventory inv;
		inv = Bukkit.createInventory(null, (9*5), FRIEND_MENU_NAME);	
		
		inv.setItem(4, ItemMenuBank.claimFriendItem(player));
		int nbClaim = 0;
		for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZoneClaimOfFriendForPlayer(player))
		{
			inv.setItem(9+nbClaim, ItemMenuBank.friendZoneItem(zc));
			nbClaim++;
		}
		inv.setItem(inv.getSize() - 9, ItemMenuBank.itemQuit(true));
		inv.setItem(inv.getSize() - 1, ItemMenuBank.itemQuit(true));
		player.openInventory(inv);
	}
	
	public void openZoneMenuForZone(Player player, String zoneName)
	{
		if (player == null)
		{
			return;
		}
		
		ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(zoneName);
		if (zc == null)
		{
			System.out.println("Tentative d'ouverture de menu pour un menu invalide " + zoneName);
			return;
		}
		Inventory inv;
		inv = Bukkit.createInventory(null, (9*5), CLAIM_ZONE_MENU_NAME);	
		
		inv.setItem(4, ItemMenuBank.claimItem(zc));
		int nbGuest = 0;
		for (OfflinePlayer guest : zc.getGuests())
		{
			ItemStack currentGuest = ItemMenuBank.claimGuestItem(guest);			
			inv.setItem(9+nbGuest, currentGuest);
			nbGuest++;
		}
		
		inv.setItem(30, ItemMenuBank.changeNameItem());
		inv.setItem(32, ItemMenuBank.addGuestItem());

		inv.setItem(inv.getSize() - 9, ItemMenuBank.itemQuit(false));
		inv.setItem(inv.getSize() - 1, ItemMenuBank.itemQuit(false));
		player.openInventory(inv);		
	}	

	public void openRemoveGuestMenu(Player player, UUID guestID)
	{
		if (player == null)
		{
			return;
		}
		// !!!! warning : need to test if we can create a bug trying to close the inv when wlicking on button

		if (player.getOpenInventory() == null)
		{
			System.out.println("On a essaye de remove un guest alors que l'on avait plus d'inventaire ouvert");
			return;
		}

		ItemStack zoneItem = player.getOpenInventory().getTopInventory().getItem(4);
		String title[] = zoneItem.getItemMeta().getDisplayName().split(" : ");
		if (title.length <= 1)
		{
			System.out.println("Titre d'item incomplet ???");
			return;
		}
		
		Inventory inv;
		inv = Bukkit.createInventory(null, (9*5), REMOVE_GUEST_MENU_NAME);	
		
		inv.setItem(4, ItemMenuBank.claimItem(HumineClaim.getZoneClaimManager().getZoneClaim(title[1])));

		OfflinePlayer guest = Bukkit.getOfflinePlayer(guestID);
		inv.setItem(13, ItemMenuBank.claimGuestItem(guest));
		
		inv.setItem(30, ItemMenuBank.removeGuestItem());
		inv.setItem(32, ItemMenuBank.cancelItem());

		inv.setItem(inv.getSize() - 9, ItemMenuBank.itemQuit(false));
		inv.setItem(inv.getSize() - 1, ItemMenuBank.itemQuit(false));
		player.openInventory(inv);		
	}
	
	public void addGuestSelection(Player player)
	{
		if (player == null)
		{
			return;
		}
		if (player.getOpenInventory() == null)
		{
			System.out.println("On a essaye d'add un guest alors que l'on avait plus d'inventaire ouvert");
			return;
		}
		ItemStack zoneItem = player.getOpenInventory().getTopInventory().getItem(4);
		String title[] = zoneItem.getItemMeta().getDisplayName().split(" : ");
		if (title.length > 1)
		{
			ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(title[1]);		
			player.closeInventory();
			player.sendMessage("Nom du nouvel invité ?");		
			PlayerAnswerEvent.askingNewGuestForZoneClaim.put(Bukkit.getOfflinePlayer(player.getUniqueId()), zc);
		}
	}
	
	public void newGuestEntry(Player player, ZoneClaim zc, String newGuestName)
	{
		Player newGuest = null;
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getName().equals(newGuestName))
			{
				newGuest = p;
			}
		}
		if (newGuest == null)
		{
			player.sendMessage("Ce joueur n'existe pas ou n'est pas connecté !");
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 5, 5);
			openZoneMenuForZone((Player) player, zc.getName());
			return;
		}
		if (player.getName().equals(newGuestName))
		{
			player.sendMessage("Mais.. C'est toi ! Tu essaies de faire quoi au juste ?");
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 5, 5);
			openZoneMenuForZone((Player) player, zc.getName());
			return;
		}
		OfflinePlayer offlineGuest = Bukkit.getOfflinePlayer(newGuest.getUniqueId());
		if (zc.getGuests().contains(offlineGuest))
		{
			player.sendMessage("Cet invité est déjà ajouté !");
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 5, 5);
			openZoneMenuForZone((Player) player, zc.getName());
			return;
		}
		newGuest.sendMessage("Tu as été ajouté en tant qu'invité sur le terrain " + zc.getName() + " de " + player.getName());
		newGuest.playSound(newGuest.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
		player.sendMessage("Nouvel invité bien ajouté !");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
		zc.addGuests(offlineGuest);
	}
	
	public void changeNameSelection(Player player)
	{
		if (player == null)
		{
			return;
		}
		if (player.getOpenInventory() == null)
		{
			System.out.println("On a essaye de changer de nom alors que l'on avait plus d'inventaire ouvert");
			return;
		}
		ItemStack zoneItem = player.getOpenInventory().getTopInventory().getItem(4);
		String title[] = zoneItem.getItemMeta().getDisplayName().split(" : ");
		if (title.length > 1)
		{
			ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(title[1]);		
			player.closeInventory();
			player.sendMessage("Nouveau nom du terrain ?");		
			PlayerAnswerEvent.askingNewNameForZoneClaim.put(Bukkit.getOfflinePlayer(player.getUniqueId()), zc);
		}	
	}
	
	public void newNameEntry(Player player, ZoneClaim zc, String newZoneName)
	{
		String[] getSpace = newZoneName.split(" ");
		if(getSpace.length != 1)
		{
			player.sendMessage("Nom de terrain invalide, réessaie !");
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 5, 5);
			openZoneMenuForZone((Player) player, zc.getName());
			return;
		}
		if (zc == null)
		{
			System.out.println("UNE ZONE CLAIM NULLE ???");
			return;
		}
		if (zc.getName().equals(newZoneName))
		{
			player.sendMessage("Ton terrain s'appelle déjà, réveille toi Dory.");
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 5, 5);
			openZoneMenuForZone((Player) player, zc.getName());
			return;
		}
		if (HumineClaim.getZoneClaimManager().getZoneClaim(newZoneName) != null)
		{
			player.sendMessage("Ce nom de terrain est déjà pris, réessaie !");
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 5, 5);
			openZoneMenuForZone((Player) player, zc.getName());
			return;
		}
		player.sendMessage("Nom de terrain bien modifié !");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
		zc.setName(newZoneName);
	}
	
	public void closeMenu(Player player, MENU_STEP stepToClose)
	{
		if (player == null)
			return;
		
		switch(stepToClose)
		{
		case MAINMENU:
			player.closeInventory();
			break;
		case FRIENDMENU:
			openMainMenu(player);			
			break;
		case ZONEMENU:
			openMainMenu(player);
			break;
		case GUESTMENU:
			if (player.getOpenInventory() == null)
			{
				System.out.println("On a essaye de revenir en arriere alors que l'on avait plus d'inventaire ouvert");
				return;
			}
			ItemStack zoneItem = player.getOpenInventory().getTopInventory().getItem(4);
			String title[] = zoneItem.getItemMeta().getDisplayName().split(" : ");
			if (title.length > 1)
			{
				openZoneMenuForZone((Player) player, title[1]);
			}
			break;
		default:
			player.closeInventory();
			break;			
		}
	}
	
	public void removeGuestValidation(Player player, Inventory inv)
	{
		if (player == null || inv == null)
			return;

		ItemStack zoneItem = player.getOpenInventory().getTopInventory().getItem(4);
		String title[] = zoneItem.getItemMeta().getDisplayName().split(" : ");
		if (title.length > 1)
		{
			ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(title[1]);
			if (zc != null)
			{
				ItemStack guestItem = player.getOpenInventory().getTopInventory().getItem(13);
				NamespacedKey key = new NamespacedKey(HumineClaim.getPlugin(HumineClaim.class), "playerIDKey");
				String idString = guestItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
				UUID guestID = UUID.fromString(idString);
				zc.removeGuest(Bukkit.getOfflinePlayer(guestID));
				openZoneMenuForZone((Player) player, title[1]);
			}
		}		
	}
}
