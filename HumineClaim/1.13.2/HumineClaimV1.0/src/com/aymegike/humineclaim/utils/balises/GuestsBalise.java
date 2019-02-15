package com.aymegike.humineclaim.utils.balises;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class GuestsBalise extends MCBalise {

	public static final String NAME = "guests";
	
	private ArrayList<OfflinePlayer> players;
	
	public GuestsBalise() {
		super(NAME);
		players = new ArrayList<OfflinePlayer>();
	}

	@Override
	public MCBalise getInstance() {
		return new GuestsBalise();
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap node) {}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		for (MCBalise mcBalise : getChildrens()) {
			if (mcBalise instanceof GuestBalise) {
				GuestBalise gb = (GuestBalise) mcBalise;
				gb.directExecute(player, xmlFile);
				players.add(gb.getPlayer());
			}
		}
	}
	
	public ArrayList<OfflinePlayer> getPlayers() {
		return players;
	}

}
