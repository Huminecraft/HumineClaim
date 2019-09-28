package com.aymegike.humineclaim.utils.balises;

import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.script.Variable;

public class LimitedBlocDeclarationBalise extends MCBalise {

	public static final String NAME = "limited-bloc";
	
	private String sBloc, sMaximum;
	
	private String bloc;
	private int maximum;
	
	public LimitedBlocDeclarationBalise() {
		super(NAME);
		maximum = 0;
	}
	
	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		bloc = getString(xmlFile.getScriptManager().compile(sBloc, 0), xmlFile);
		maximum = Integer.parseInt(xmlFile.getScriptManager().compile(sMaximum, 0));
		
		xmlFile.getScriptManager().addVariable(new Variable("%BLOC%", "'"+ bloc +"'"));		
		xmlFile.getScriptManager().addVariable(new Variable("%MAXIMUM%", ""+ maximum));	
	}

	@Override
	public MCBalise getInstance() {
		return new LimitedBlocDeclarationBalise();
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap namedNodeMap)
	{
		Node nBloc = namedNodeMap.getNamedItem("bloc");
		Node nMaximum = namedNodeMap.getNamedItem("maximum");
	
		if (nBloc != null && nMaximum != null)
		{
			sBloc = nBloc.getNodeValue();
			sMaximum = nMaximum.getNodeValue();
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
	
	public int getMaximum()
	{
		return maximum;
	}
}
