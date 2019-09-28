package com.aymegike.humineclaim.utils.balises;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.balises.ItemBalise;

public class ForbidenItemsBalise extends MCBalise   {

	public static final String NAME = "forbiden-items";

	private ArrayList<String> items;
	
	public ForbidenItemsBalise() {
		super(NAME);
		items = new ArrayList<String>();
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile)
	{
		for (MCBalise mcBalise : getChildrens())
		{
			if (mcBalise instanceof ItemBalise)
			{
				ItemBalise ib = (ItemBalise) mcBalise;
				ib.directExecute(player, xmlFile);
				items.add(ib.getItem());
			}
		}	
		
	}

	@Override
	public MCBalise getInstance()
	{
		return new ForbidenItemsBalise();
	}
	
	public ArrayList<String> getItems()
	{
		return items;
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap arg0)
	{
		
	}

}
