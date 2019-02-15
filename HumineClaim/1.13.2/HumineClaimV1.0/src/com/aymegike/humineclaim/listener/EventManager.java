package com.aymegike.humineclaim.listener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.listener.events.InventoryListener;

public class EventManager {
	
	public EventManager(HumineClaim pl) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryListener(), pl);
	}

}
