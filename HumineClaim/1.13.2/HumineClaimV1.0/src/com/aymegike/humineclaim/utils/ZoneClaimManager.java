package com.aymegike.humineclaim.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ZoneClaimManager {
		
	private ArrayList<ZoneClaim> m_zoneClaims;
	private HashMap<ZoneClaim, Location> m_safeExitPerZone;
	private ArrayList<Location> m_protectedLocations;
	
	//Settings
	private ArrayList<String> m_limitedItems;
	private ArrayList<String> m_forbidenItems;
	private Map<String, Integer> m_limitedBlocsDeclarations;
	
	// ----------------------------------
	// Constructor
	// ----------------------------------
	
	public ZoneClaimManager() {
		m_zoneClaims = new ArrayList<ZoneClaim>();
		m_safeExitPerZone = new HashMap<ZoneClaim, Location>();
		m_protectedLocations = new ArrayList<Location>();
		m_limitedItems = new ArrayList<String>();
		m_forbidenItems = new ArrayList<String>();
		m_limitedBlocsDeclarations = new HashMap<String, Integer>();
	}

	// ----------------------------------
	// Getters
	// ----------------------------------
	
	public ArrayList<ZoneClaim> getZonesClaims()
	{
		return m_zoneClaims;
	}

	public ArrayList<String> getForbiddenItems()
	{
		return m_forbidenItems;
	}

	public ZoneClaim getZoneClaim(int ID)
	{
		for (ZoneClaim zc : getZonesClaims())
		{
			if (zc.getID() == ID)
			{
				return zc;
			}
		}
		return null;
	}	
	
	//WARNING : USE THIS FUNCTION ONLY WHEN YOU ARE SUR THAT YOU HAVE THE CORRECT NAME 
	public ZoneClaim getZoneClaim(String name)
	{
		for (ZoneClaim zc : getZonesClaims())
		{
			if (zc.getName().equals(name))
			{
				return zc;
			}
		}
		return null;
	}
	
	public ArrayList<ZoneClaim> getZoneClaimForPlayer(Player p)
	{
		ArrayList<ZoneClaim> output = new ArrayList<ZoneClaim>();
		for (ZoneClaim zc : getZonesClaims())
		{
			if (zc != null)
			{
				if (zc.getOwner() != null && zc.getOwner().getUniqueId().equals(p.getUniqueId()))
				{
					output.add(zc);
				}
			}
		}
		return output;		
	}
	
	public ArrayList<ZoneClaim> getZoneClaimOfFriendForPlayer(Player p)
	{
		ArrayList<ZoneClaim> output = new ArrayList<ZoneClaim>();
		for (ZoneClaim zc : getZonesClaims())
		{
			if (zc != null)
			{
				for (OfflinePlayer offPlayer : zc.getGuests())
				{
					if (offPlayer.getUniqueId().equals(p.getUniqueId()))
					{
						output.add(zc);
					}
				}
			}
		}
		return output;		
	}	

	public ZoneClaim getZoneClaimThatContainsPlayer(Player player)
	{
		if (player == null)
		{
			return null;
		}
		
		for (ZoneClaim zc : m_zoneClaims)
		{
			if (zc.containLocation(player.getLocation()))
			{
				return zc;
			}
		}		
		return null;
	}

	public int getNbClaim(String playerName)
	{
		int nb = 0;
		for (ZoneClaim zc : getZonesClaims()) {
			if (zc != null && zc.getOwner() != null && zc.getOwner().getName().equalsIgnoreCase(playerName)) {
				nb++;
			}
		}
		return nb;
	}
	
	public int getMaximumAmountToPlace(String item)
	{
		if (isForbidenBlockToPlace(item))
		{
			return m_limitedBlocsDeclarations.get(item);
		}
		
		System.out.println("!! [ANORMAL] !! - GetMaximumToPlace a ete appele alors que le bloc n'est pas interdit !");
		return 500;
	}	
	
	// ----------------------------------
	// Setters
	// ----------------------------------

	
	public void addZoneClaim(ZoneClaim zoneClaim)
	{
		if (zoneClaim != null)
		{
			m_zoneClaims.add(zoneClaim);
			Location panelLocation = zoneClaim.getSignLocation();
			int x = panelLocation.getBlockX();
			int y = panelLocation.getBlockY();
			int z = panelLocation.getBlockZ();
			World w = panelLocation.getWorld();
			Location protectedLocation = null;
			
			if (!zoneClaim.containLocation(new Location(w, x+2, y, z)))
			{
				protectedLocation = new Location(w, x+2, y, z);
			}
			else if (!zoneClaim.containLocation(new Location(w, x-2, y, z)))
			{
				protectedLocation = new Location(w, x-2, y, z);
			}
			else if (!zoneClaim.containLocation(new Location(w, x, y, z+2)))
			{
				protectedLocation = new Location(w, x, y, z+2);
			}
			else if (!zoneClaim.containLocation(new Location(w, x, y, z-2)))
			{
				protectedLocation = new Location(w, x, y, z-2);	
			}
			if (protectedLocation != null)
			{
				m_safeExitPerZone.put(zoneClaim, protectedLocation);
				m_protectedLocations.add(protectedLocation);
				m_protectedLocations.add(new Location(protectedLocation.getWorld(), protectedLocation.getX(), protectedLocation.getY()-1, protectedLocation.getZ()));
				m_protectedLocations.add(new Location(protectedLocation.getWorld(), protectedLocation.getX(), protectedLocation.getY()-2, protectedLocation.getZ()));
			}
		}
	}
	
	public void removeZoneClaim(ZoneClaim zoneClaim)
	{
		if (zoneClaim != null)
		{
			m_zoneClaims.remove(zoneClaim);
			Location protectedLocation = m_safeExitPerZone.get(zoneClaim);
			m_safeExitPerZone.remove(zoneClaim);
			if (protectedLocation != null)
			{
				m_protectedLocations.remove(protectedLocation);
				m_protectedLocations.remove(new Location(protectedLocation.getWorld(), protectedLocation.getX(), protectedLocation.getY()-1, protectedLocation.getZ()));
				m_protectedLocations.remove(new Location(protectedLocation.getWorld(), protectedLocation.getX(), protectedLocation.getY()-2, protectedLocation.getZ()));
			}
		}
	}
	
	public void setSettings(ArrayList<String> limitedItems, ArrayList<String> forbidenItems, Map<String, Integer> limitedBlocsDeclarations)
	{
		m_limitedItems = limitedItems;
		m_forbidenItems = forbidenItems;
		m_limitedBlocsDeclarations = limitedBlocsDeclarations;
	}
	
	// ----------------------------------
	// Utils
	// ----------------------------------
	
	public boolean isLimitedItem(String item)
	{
		return m_limitedItems.contains(item);
	}
	
	public boolean isForbidenItem(String item)
	{
		return m_forbidenItems.contains(item);		
	}
	
	public boolean isForbidenBlockToPlace(String item)
	{
		return m_limitedBlocsDeclarations.containsKey(item);
	}
	
	public boolean isLocationInZoneClaim(Location loc)
	{
		for (ZoneClaim zc : m_zoneClaims)
		{
			if (zc.containLocation(loc))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isProtectedLocation(Location loc)
	{
		return m_protectedLocations.contains(loc);
	}
	
	public void applyLeaveCommandForPlayer(Player player)
	{
		if (player == null)
		{
			return;
		}
		
		ZoneClaim zc = getZoneClaimThatContainsPlayer(player);
		if (zc != null)
		{
			Location protectedLocation = m_safeExitPerZone.get(zc);
			if (protectedLocation != null)
			{
				player.teleport(protectedLocation);
			}
			else
			{
				System.out.println("On a pas trouve de zone protege ou tp le joueur ! :c");
			}
		}
		else
		{
			player.sendMessage("Tu n'es pas dans une zone claim, cette commande ne te sera d'aucune utilité ici !");
		}		
	}
}
