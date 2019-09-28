package com.aymegike.humineclaim.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZoneClaimManager {
	
	private ArrayList<ZoneClaim> zoneClaims;
	
	//Settings
	private ArrayList<String> limitedItems;
	private ArrayList<String> forbidenItems;
	private Map<String, Integer> limitedBlocsDeclarations;
	
	
	public ZoneClaimManager() {
		this.zoneClaims = new ArrayList<ZoneClaim>();
		limitedItems = new ArrayList<String>();
		forbidenItems = new ArrayList<String>();
		limitedBlocsDeclarations = new HashMap<String, Integer>();
	}
	
	public void addZoneClaim(ZoneClaim zoneClaim) {
		this.zoneClaims.add(zoneClaim);
	}
	
	public void removeZoneClaim(ZoneClaim zoneClaim) {
		this.zoneClaims.remove(zoneClaim);
	}
	
	public ArrayList<ZoneClaim> getZonesClaims() {
		return this.zoneClaims;
	}
	
	public ZoneClaim getZoneClaim(String name) {
		for (ZoneClaim zc : getZonesClaims()) {
			if (zc.getName().equalsIgnoreCase(name)) {
				return zc;
			}
		}
		return null;
	}
	
	public void updateZonesFromFiles()
	{
		
	}
	
	public void setSettings(ArrayList<String> limitedItems, ArrayList<String> forbidenItems, Map<String, Integer> limitedBlocsDeclarations)
	{
		this.limitedItems = limitedItems;
		this.forbidenItems = forbidenItems;
		this.limitedBlocsDeclarations = limitedBlocsDeclarations;
	}
	
	public boolean isLimitedItem(String item)
	{
		return limitedItems.contains(item);
	}
	
	public boolean isForbidenItem(String item)
	{
		return forbidenItems.contains(item);		
	}
	
	public boolean isForbidenBlockToPlace(String item)
	{
		return limitedBlocsDeclarations.containsKey(item);
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
			return limitedBlocsDeclarations.get(item);
		}
		
		System.out.println("!! [ANORMAL] !! - GetMaximumToPlace a ete appele alors que le bloc n'est pas interdit !");
		return 500;
	}
}
