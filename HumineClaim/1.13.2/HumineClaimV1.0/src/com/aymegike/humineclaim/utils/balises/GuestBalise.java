package com.aymegike.humineclaim.utils.balises;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class GuestBalise extends MCBalise {

	public static final String NAME = "guest";
	
	private OfflinePlayer player;
	
	public GuestBalise() {
		super(NAME);
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile)
	{
		String idString = getString(xmlFile.getScriptManager().compile(getContent(), 0), xmlFile);
		if (!idString.isEmpty())
		{
			UUID playerID = UUID.fromString(idString);
			this.player = Bukkit.getOfflinePlayer(playerID);
		}
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap node) {}

	@Override
	public MCBalise getInstance() {
		return new GuestBalise();
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}

}
