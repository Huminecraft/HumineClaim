package com.aymegike.humineclaim.utils.balises;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.balises.ItemBalise;

public class LimitedItemsBalise extends MCBalise   {

	public static final String NAME = "limited-items";

	private ArrayList<String> items;
	
	public LimitedItemsBalise() {
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
		return new LimitedItemsBalise();
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
