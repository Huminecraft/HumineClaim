package com.aymegike.humineclaim;

import org.bukkit.plugin.java.JavaPlugin;

import com.aymegike.humineclaim.listener.CommandManager;
import com.aymegike.humineclaim.listener.EventManager;
import com.aymegike.humineclaim.utils.XMLManager;
import com.aymegike.humineclaim.utils.ZoneClaimManager;

public class HumineClaim extends JavaPlugin {
	
	private static ZoneClaimManager zoneClaimManager;
	
	public void onEnable() {
		super.onEnable();
		
		System.out.println("--------------------------------");
		System.out.println("[HumineClaim]: isLoading");
		System.out.println("--------------------------------");
		
		new EventManager(this);
		new XMLManager();
		new CommandManager(this);
		zoneClaimManager = new ZoneClaimManager();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	public static ZoneClaimManager getZoneClaimManager() {
		return zoneClaimManager;
	}

}
