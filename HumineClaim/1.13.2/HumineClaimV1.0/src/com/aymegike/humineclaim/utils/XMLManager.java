package com.aymegike.humineclaim.utils;

import java.io.File;

import com.aymegike.humineclaim.utils.balises.CornerBalise;
import com.aymegike.humineclaim.utils.balises.ForbidenItemsBalise;
import com.aymegike.humineclaim.utils.balises.GuestBalise;
import com.aymegike.humineclaim.utils.balises.GuestsBalise;
import com.aymegike.humineclaim.utils.balises.LimitedBlocCounterBalise;
import com.aymegike.humineclaim.utils.balises.LimitedBlocDeclarationBalise;
import com.aymegike.humineclaim.utils.balises.LimitedBlocsBalise;
import com.aymegike.humineclaim.utils.balises.LimitedItemsBalise;
import com.aymegike.humineclaim.utils.balises.NameBalise;
import com.aymegike.humineclaim.utils.balises.OwnerBalise;
import com.aymegike.humineclaim.utils.balises.PlacedShulkerBalise;
import com.aymegike.humineclaim.utils.balises.PriceBalise;
import com.aymegike.humineclaim.utils.balises.SignBalise;
import com.aymegike.humineclaim.utils.balises.ZoneClaimBalise;
import com.aymegike.humineclaim.utils.balises.ZonesClaimSettingsBalise;
import com.aypi.Aypi;
import com.aypi.utils.xml.XMLFile;

public class XMLManager {
	
	public XMLManager() {
		
		if (Aypi.getXMLFileManager() == null)
		{
			System.out.println("NULL C'EST UN PROBLEME");
		}
		else
		{		
			Aypi.getXMLFileManager().addMCBalise(new ZoneClaimBalise());
			Aypi.getXMLFileManager().addMCBalise(new CornerBalise());	
			Aypi.getXMLFileManager().addMCBalise(new GuestsBalise());
			Aypi.getXMLFileManager().addMCBalise(new GuestBalise());
			Aypi.getXMLFileManager().addMCBalise(new OwnerBalise());
			Aypi.getXMLFileManager().addMCBalise(new PriceBalise());
			Aypi.getXMLFileManager().addMCBalise(new NameBalise());
			Aypi.getXMLFileManager().addMCBalise(new SignBalise());
			Aypi.getXMLFileManager().addMCBalise(new PlacedShulkerBalise());
			Aypi.getXMLFileManager().addMCBalise(new LimitedBlocsBalise());
			Aypi.getXMLFileManager().addMCBalise(new LimitedBlocDeclarationBalise());
			Aypi.getXMLFileManager().addMCBalise(new LimitedBlocCounterBalise());
	
			Aypi.getXMLFileManager().addMCBalise(new ZonesClaimSettingsBalise());
			Aypi.getXMLFileManager().addMCBalise(new LimitedItemsBalise());
			Aypi.getXMLFileManager().addMCBalise(new ForbidenItemsBalise());
					
			File file = new File("./plugins/HumineClaim/xml-reader/");
			file.mkdirs();
			System.out.println("[HumineClaim] "+file.listFiles().length+" file to load found !");
			for (File f : file.listFiles())
			{
				System.out.println("	- "+f.getName());
				XMLFile xmlFile = new XMLFile(f);
				xmlFile.load();
				Aypi.getXMLFileManager().addXMLFile(xmlFile);
			}
		}
	}
}
