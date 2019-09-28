package com.aymegike.humineclaim.utils.balises;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aymegike.humineclaim.utils.LimitedBlocInfos;
import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;

public class LimitedBlocsBalise extends MCBalise  {

	public static final String NAME = "limited-blocs";

	private Map<String, Integer> blocsDeclarations;
	private Map<String, Integer> blocsCounters;	
	
	public LimitedBlocsBalise() {
		super(NAME);
		blocsDeclarations = new HashMap<String, Integer>();
		blocsCounters = new HashMap<String, Integer>();
	}

	public MCBalise getInstance() {
		return new LimitedBlocsBalise();	
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		for (MCBalise mcBalise : getChildrens())
		{
			if (mcBalise instanceof LimitedBlocDeclarationBalise)
			{
				LimitedBlocDeclarationBalise ib = (LimitedBlocDeclarationBalise) mcBalise;
				ib.directExecute(player, xmlFile);
				blocsDeclarations.put(ib.getBloc(), ib.getMaximum());
			}
			if (mcBalise instanceof LimitedBlocCounterBalise)
			{
				LimitedBlocCounterBalise ib = (LimitedBlocCounterBalise) mcBalise;
				ib.directExecute(player, xmlFile);
				blocsCounters.put(ib.getBloc(), ib.getCurrentAmount());
			}
		}		
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap arg0) {	
	}

	public Map<String, Integer> getLimitedBlocDeclarations()
	{
		return blocsDeclarations;
	}

	public Map<String, Integer> getLimitedBlocCounters()
	{
		return blocsCounters;
	}
}
