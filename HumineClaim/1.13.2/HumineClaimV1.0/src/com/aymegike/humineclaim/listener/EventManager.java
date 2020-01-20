package com.aymegike.humineclaim.listener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.listener.events.InventoryListener;
import com.aymegike.humineclaim.menu.MenuClickEvent;
import com.aymegike.humineclaim.menu.PlayerAnswerEvent;

public class EventManager {
	
	public EventManager(HumineClaim pl) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryListener(), pl);
		pm.registerEvents(new MenuClickEvent(), pl);
		pm.registerEvents(new PlayerAnswerEvent(), pl);
	}

}
