package com.aymegike.humineclaim;

import org.bukkit.plugin.java.JavaPlugin;

import com.aymegike.humineclaim.listener.CommandManager;
import com.aymegike.humineclaim.listener.EventManager;
import com.aymegike.humineclaim.listener.command.LeaveClaimCommand;
import com.aymegike.humineclaim.listener.command.ShowMenuCommand;
import com.aymegike.humineclaim.menu.ClaimMenuManager;
import com.aymegike.humineclaim.utils.XMLManager;
import com.aymegike.humineclaim.utils.ZoneClaimManager;

public class HumineClaim extends JavaPlugin {
	
	private static ZoneClaimManager zoneClaimManager;
	private static ClaimMenuManager menuManager;
	
	public void onEnable() {
		super.onEnable();
		
		System.out.println("--------------------------------");
		System.out.println("[HumineClaim]: isLoading");
		System.out.println("--------------------------------");
		
		new EventManager(this);
		new XMLManager();
		new CommandManager(this);
		this.getCommand("claim").setExecutor(new ShowMenuCommand());
		this.getCommand("leave").setExecutor(new LeaveClaimCommand());
		zoneClaimManager = new ZoneClaimManager();
		menuManager = new ClaimMenuManager();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	public static ZoneClaimManager getZoneClaimManager() {
		return zoneClaimManager;
	}
	
	public static ClaimMenuManager getClaimMenuManager() {
		return menuManager;
	}
	
	public HumineClaim getPluginInstance() {
		return this;
	}
}
