package com.aymegike.humineclaim.utils.balises;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class NameBalise extends MCBalise {

	public static final String NAME = "claim-name";
	
	private String name;
	
	public NameBalise() {
		super(NAME);
		name = null;
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		name = getString(xmlFile.getScriptManager().compile(getContent(), 0), xmlFile);
	}

	@Override
	public MCBalise getInstance() {
		return new NameBalise();
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap node) {}
	
	public String getClaimName() {
		return name;
	}
}
