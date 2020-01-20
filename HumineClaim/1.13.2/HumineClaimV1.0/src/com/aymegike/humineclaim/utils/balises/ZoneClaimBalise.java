package com.aymegike.humineclaim.utils.balises;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.w3c.dom.NamedNodeMap;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;
import com.aypi.utils.inter.SurchMcBalise;
import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.XMLFile;
import com.aypi.utils.xml.balises.IDBalise;
import com.aypi.utils.xml.balises.LocationBalise;

public class ZoneClaimBalise extends MCBalise {

	public static final String NAME = "zone-claim";
		
	private OfflinePlayer owner;
	private ArrayList<OfflinePlayer> members;
	private int price;
	private int ID;
	private String name;
	private Location signLocation;
	private Map<String, Integer> limitedBlocsCounters;
	private boolean shulkerIsPlaced;
	
	public ZoneClaimBalise() {
		super(NAME);
		ID = -1;
		price = 0;
		owner = null;
		members = new ArrayList<OfflinePlayer>();
		name = generateRandomName();
		signLocation = null;
		limitedBlocsCounters = new HashMap<String, Integer>();
		shulkerIsPlaced = false;
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

			if (mcBalise instanceof SignBalise) {
				mcBalise.directExecute(player, xmlFile);
				signLocation = ((SignBalise) mcBalise).GetLocation();
			}		
			else if (mcBalise instanceof LocationBalise) {
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
			}	
			else if (mcBalise instanceof GuestsBalise) {
				mcBalise.directExecute(player, xmlFile);
				members = ((GuestsBalise) mcBalise).getPlayers();
			}	
			else if (mcBalise instanceof OwnerBalise) {
				mcBalise.directExecute(player, xmlFile);
				owner = ((OwnerBalise) mcBalise).getPlayer();
			}	
			else if (mcBalise instanceof PriceBalise) {
				mcBalise.directExecute(player, xmlFile);
				price = ((PriceBalise) mcBalise).getPrice();
			}	
			else if (mcBalise instanceof NameBalise) {
				mcBalise.directExecute(player, xmlFile);
				name = ((NameBalise) mcBalise).getClaimName();
			}
			else if (mcBalise instanceof IDBalise) {
				mcBalise.directExecute(player, xmlFile);
				ID = ((IDBalise) mcBalise).getID();
			}
			else if (mcBalise instanceof LimitedBlocsBalise)
			{
				mcBalise.directExecute(player, xmlFile);
				limitedBlocsCounters = ((LimitedBlocsBalise) mcBalise).getLimitedBlocCounters();
			}
			else if (mcBalise instanceof PlacedShulkerBalise)
			{
				mcBalise.directExecute(player, xmlFile);
				shulkerIsPlaced = ((PlacedShulkerBalise) mcBalise).getShulkerNumber() > 0;
			}
		}
		
		HumineClaim.getZoneClaimManager().addZoneClaim(new ZoneClaim(ID, name, c1, c2, owner, members, price, signLocation, limitedBlocsCounters, shulkerIsPlaced, xmlFile));
		
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
