package com.aymegike.humineclaim.utils.balises;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class PriceBalise extends MCBalise {

	public static final String NAME = "price";
	
	private int price;
	
	public PriceBalise() {
		super(NAME);
		price = 0;
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		price = Integer.parseInt(xmlFile.getScriptManager().compile(getContent(), 0));
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap node) {}

	@Override
	public MCBalise getInstance() {
		return new PriceBalise();
	}
	
	public int getPrice() {
		return price;
	}
	
		

}
