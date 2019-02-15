package com.aymegike.humineclaim.utils;

import java.util.ArrayList;

public class ZoneClaimManager {
	
	private ArrayList<ZoneClaim> zoneClaims;
	
	public ZoneClaimManager() {
		this.zoneClaims = new ArrayList<ZoneClaim>();
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

}
