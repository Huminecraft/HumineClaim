package com.aymegike.humineclaim.utils.balises;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.script.Variable;

public class LimitedBlocCounterBalise extends MCBalise {

	public static final String NAME = "limited-bloc-counter";
	
	private String sBloc, sCurrentAmount;
	
	private String bloc;
	private int currentAmount;
	
	public LimitedBlocCounterBalise() {
		super(NAME);
		currentAmount = 0;
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		bloc = getString(xmlFile.getScriptManager().compile(sBloc, 0), xmlFile);
		currentAmount = Integer.parseInt(xmlFile.getScriptManager().compile(sCurrentAmount, 0));
		
		xmlFile.getScriptManager().addVariable(new Variable("%BLOC%", "'"+ bloc +"'"));		
		xmlFile.getScriptManager().addVariable(new Variable("%CURRENTAMOUNT%", ""+ currentAmount));
	}

	@Override
	public MCBalise getInstance() {
		return new LimitedBlocCounterBalise();
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap namedNodeMap)
	{
		Node nBloc = namedNodeMap.getNamedItem("bloc");
		Node nCurrentAmount = namedNodeMap.getNamedItem("current-amount");
	
		if (nBloc != null && nCurrentAmount != null)
		{
			sBloc = nBloc.getNodeValue();
			sCurrentAmount = nCurrentAmount.getNodeValue();
		}
		else
		{
			System.out.println("[HUMINECLAIM] : ERROR argument for limited-bloc balise...");
		}
	}
	
	public String getBloc()
	{
		return bloc;
	}
	
	public int getCurrentAmount()
	{
		return currentAmount;
	}
}
