package com.aymegike.humineclaim.menu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;

public class PlayerAnswerEvent implements Listener
{
	public static HashMap<OfflinePlayer, ZoneClaim> askingNewGuestForZoneClaim = new HashMap<OfflinePlayer, ZoneClaim>();
	public static HashMap<OfflinePlayer, ZoneClaim> askingNewNameForZoneClaim = new HashMap<OfflinePlayer, ZoneClaim>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerUseChat(PlayerChatEvent e)
	{
		OfflinePlayer player = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId());
		if (askingNewGuestForZoneClaim.containsKey(player))
		{
			e.setCancelled(true);
			ZoneClaim zc = askingNewGuestForZoneClaim.get(player);
			askingNewGuestForZoneClaim.remove(player);
			HumineClaim.getClaimMenuManager().newGuestEntry(e.getPlayer(), zc, e.getMessage());
		}
		if (askingNewNameForZoneClaim.containsKey(player))
		{
			e.setCancelled(true);
			ZoneClaim zc = askingNewNameForZoneClaim.get(player);
			askingNewNameForZoneClaim.remove(player);	
			HumineClaim.getClaimMenuManager().newNameEntry(e.getPlayer(), zc, e.getMessage());	
		}
	}
}
