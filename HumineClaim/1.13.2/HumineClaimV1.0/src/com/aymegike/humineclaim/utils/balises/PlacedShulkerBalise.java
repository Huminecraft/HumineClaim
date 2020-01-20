package com.aymegike.humineclaim.utils.balises;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class PlacedShulkerBalise extends MCBalise   {
	public static final String NAME = "placed-shulker";
	
	private int number;
	
	public PlacedShulkerBalise()
	{
		super(NAME);
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile)
	{
		number = Integer.parseInt(xmlFile.getScriptManager().compile(getContent(), 0));
	}
	
	@Override
	public MCBalise getInstance() {
		return new PlacedShulkerBalise();
	}
	
	@Override
	public void setUpCustomAttributes(NamedNodeMap name) {}
	
	public int getShulkerNumber() {
		return number;
	}
}
