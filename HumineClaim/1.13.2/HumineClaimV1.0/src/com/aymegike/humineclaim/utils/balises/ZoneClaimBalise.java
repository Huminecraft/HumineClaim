package com.aymegike.humineclaim.utils.balises;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;
import com.aypi.utils.Square;
import com.aypi.utils.inter.SurchMcBalise;
import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.balises.LocationBalise;

public class ZoneClaimBalise extends MCBalise {

	public static final String NAME = "zone-claim";
	
	private Square square;
	
	private OfflinePlayer owner;
	private ArrayList<OfflinePlayer> members;
	private int price;
	private String name;
	
	public ZoneClaimBalise() {
		super(NAME);
		price = 0;
		owner = null;
		members = new ArrayList<OfflinePlayer>();
		square = null;
		name = generateRandomName();
	}

	@Override
	public void customExecute(Player player, XMLFile xmlFile) {
		Location c1 = null;
		Location c2 = null;
		
		
		
		for (MCBalise mcBalise : getChildrens()) {
			
			mcBalise.surchMcBalise(new LocationBalise(), new SurchMcBalise() {
				
				@Override
				public void execute(MCBalise mcBalise) {
					mcBalise.directExecute(player, xmlFile);
				}
				
			});
			
			
			if (mcBalise instanceof LocationBalise) {
				mcBalise.directExecute(player, xmlFile);
				for (MCBalise lb : mcBalise.getChildrens()) {
					
					if (lb instanceof CornerBalise) {
						CornerBalise cb = (CornerBalise) lb;
						if (cb.getCornerNumber() == 1) {
							c1 = cb.getLocation();
						} else if (cb.getCornerNumber() == 2) {
							c2 = cb.getLocation();
						}
					}
					
				}
				if (c1 != null && c2 != null)
					square = new Square(c1, c2);
			}
			
			if (mcBalise instanceof GuestsBalise) {
				mcBalise.directExecute(player, xmlFile);
				members = ((GuestsBalise) mcBalise).getPlayers();
			}
			
			if (mcBalise instanceof OwnerBalise) {
				mcBalise.directExecute(player, xmlFile);
				owner = ((OwnerBalise) mcBalise).getPlayer();
			}
			
			if (mcBalise instanceof PriceBalise) {
				mcBalise.directExecute(player, xmlFile);
				price = ((PriceBalise) mcBalise).getPrice();
			}
			
			if (mcBalise instanceof NameBalise) {
				mcBalise.directExecute(player, xmlFile);
				name = ((NameBalise) mcBalise).getClaimName();
			}
			
			
		}
		
		HumineClaim.getZoneClaimManager().addZoneClaim(new ZoneClaim(name, square, owner, members, price, xmlFile));
		
	}

	@Override
	public void setUpCustomAttributes(NamedNodeMap node) {
		
	}

	@Override
	public MCBalise getInstance() {
		return new ZoneClaimBalise();
	}
	
	private String generateRandomName() {
		
		String name = "";
		for (int i = 0 ; i < 25 ; i++) {
			Random r = new Random();
			name+=r.nextInt(9);
		}
		
		return name;
	}

}
