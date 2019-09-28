package com.aymegike.humineclaim.utils.balises;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aymegike.humineclaim.HumineClaim;
import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class ZonesClaimSettingsBalise extends MCBalise  {

	public static final String NAME = "zones-claim-settings";
	
	private ArrayList<String> limitedItems;
	private ArrayList<String> forbidenItems;
	private Map<String, Integer> limitedBlocsDeclarations;
	
	public ZonesClaimSettingsBalise()
	{
		super(NAME);
		limitedItems = new ArrayList<String>();
		forbidenItems = new ArrayList<String>();
		limitedBlocsDeclarations = new HashMap<String, Integer>();
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile)
	{
		for (MCBalise mcBalise : getChildrens())
		{
			if (mcBalise instanceof LimitedItemsBalise)
			{
				mcBalise.directExecute(player, xmlFile);
				limitedItems = ((LimitedItemsBalise)mcBalise).getItems();				
			}

			if (mcBalise instanceof ForbidenItemsBalise)
			{
				mcBalise.directExecute(player, xmlFile);
				forbidenItems = ((ForbidenItemsBalise)mcBalise).getItems();				
			}

			if (mcBalise instanceof LimitedBlocsBalise)
			{
				mcBalise.directExecute(player, xmlFile);
				limitedBlocsDeclarations = ((LimitedBlocsBalise)mcBalise).getLimitedBlocDeclarations();				
			}
		}		
		HumineClaim.getZoneClaimManager().setSettings(limitedItems, forbidenItems, limitedBlocsDeclarations);
	}

	@Override
	public MCBalise getInstance()
	{
		return new ZonesClaimSettingsBalise();
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap arg0)
	{		
	}

}
