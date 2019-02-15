package com.aymegike.humineclaim.utils.balises;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.balises.LocationBaliseAdaptor;

public class CornerBalise extends LocationBaliseAdaptor {

	public static final String NAME = "corner";
	
	private Location location;
	private int corner;
	
	public CornerBalise() {
		super(NAME);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		corner = Integer.parseInt(xmlFile.getScriptManager().compile(getContent(), 0));
	}

	@Override
	public MCBalise getInstance() {
		return new CornerBalise();
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap name) {}
	
	public int getCornerNumber() {
		return corner;
	}

}
