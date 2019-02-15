package com.aymegike.humineclaim.utils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.aypi.utils.Square;
import com.aypi.utils.Zone;
import com.aypi.utils.inter.ZoneListener;
import com.aypi.utils.xml.XMLFile;

public class ZoneClaim extends Zone {
	
	
	private static final int priority = Zone.PRIORITY_ULTRA;
	
	private ArrayList<OfflinePlayer> members;
	private OfflinePlayer owner;
	private int price;
	private String name;
	private XMLFile xmlFile;
	
	public ZoneClaim(String name, Square square, OfflinePlayer owner, ArrayList<OfflinePlayer> members, int price, XMLFile xmlFile) {
		super(square, new ZoneListener() {
			
			private boolean isAGuest(Player player) {
				for (OfflinePlayer op : members) {
					if (op.getName().equalsIgnoreCase(player.getName())) {
						return true;
					}
				}
					
				return owner != null && owner.getName().equalsIgnoreCase(player.getName());
			}
			private boolean isShulker(Block itemStack) {
				return itemStack.getType() == Material.SHULKER_BOX
						|| itemStack.getType() == Material.BLACK_SHULKER_BOX
						|| itemStack.getType() == Material.BLUE_SHULKER_BOX
						|| itemStack.getType() == Material.BROWN_SHULKER_BOX
						|| itemStack.getType() == Material.CYAN_SHULKER_BOX
						|| itemStack.getType() == Material.GRAY_SHULKER_BOX
						|| itemStack.getType() == Material.GREEN_SHULKER_BOX
						|| itemStack.getType() == Material.LIGHT_BLUE_SHULKER_BOX
						|| itemStack.getType() == Material.LIGHT_GRAY_SHULKER_BOX
						|| itemStack.getType() == Material.LIME_SHULKER_BOX
						|| itemStack.getType() == Material.MAGENTA_SHULKER_BOX
						|| itemStack.getType() == Material.ORANGE_SHULKER_BOX
						|| itemStack.getType() == Material.PINK_SHULKER_BOX
						|| itemStack.getType() == Material.PURPLE_SHULKER_BOX
						|| itemStack.getType() == Material.RED_SHULKER_BOX
						|| itemStack.getType() == Material.WHITE_SHULKER_BOX
						|| itemStack.getType() == Material.YELLOW_SHULKER_BOX;
			}
			
			@Override
			public void onPlayerTryToRemoveBlock(Player player, Block block, BlockBreakEvent e) {
				if (!isAGuest(player)) {
					e.setCancelled(true);
				}
			}
			
			@Override
			public void onPlayerTryToPlaceBlock(Player player, Block block, BlockPlaceEvent e) {
				if (!isAGuest(player) || isShulker(block)) {
					e.setCancelled(true);
				}
			}
			
			@Override
			public void onPlayerTryToInteractEvent(Player player, PlayerInteractEvent e) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (e.getClickedBlock().getType() == Material.CHEST 
					|| e.getClickedBlock().getType() == Material.TRAPPED_CHEST
					|| e.getClickedBlock().getType() == Material.FURNACE 
					|| e.getClickedBlock().getType() == Material.DROPPER 
					|| e.getClickedBlock().getType() == Material.DISPENSER 
					|| e.getClickedBlock().getType() == Material.HOPPER
					|| e.getClickedBlock().getType() == Material.BREWING_STAND
					|| e.getClickedBlock().getType() == Material.ANVIL
					|| e.getClickedBlock().getType() == Material.CHIPPED_ANVIL
					|| e.getClickedBlock().getType() == Material.DAMAGED_ANVIL) {
						
						if (!isAGuest(player)) {
							e.setCancelled(true);
						}
					}
				}
			}
			
			@Override
			public void onPlayerMoveInZone(Player arg0, PlayerMoveEvent arg1) {
				
			}
			
			@Override
			public void onPlayerExitZone(Player arg0) {
				
			}
			
			@Override
			public void onPlayerEnterZone(Player arg0) {
				
			}
			
			@Override
			public void onDamageByEntity(Entity arg0, EntityDamageByEntityEvent arg1) {
				
			}
			
			@Override
			public void onDamage(Entity arg0, EntityDamageEvent arg1) {
				
			}
		}, priority);
		
		this.name = name;
		this.owner = owner;
		this.members = members;
		this.price = price;
		this.xmlFile = xmlFile;
		
		System.out.println("Generate a zone-claim: " + name);
		if (owner != null)
			System.out.println("Owner: "+owner.getName());
		else
			System.out.println("Owner: no owner");
		String memberList = "";
		for (OfflinePlayer op : members) {
			memberList+=op.getName()+" ";
		}
		System.out.println("guests: "+memberList);
		System.out.println("price: "+price);
		if (getSquare() != null)
			System.out.println("zone: "+getSquare().getPos1().toString()+" "+getSquare().getPos2().toString());
		else
			System.out.println("zone: no zone");
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(OfflinePlayer op) {
		owner = op;
	}
	
	public void setGuests(ArrayList<OfflinePlayer> guests) {
		members = guests;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public void setXMLFile(XMLFile xmlFile) {
		this.xmlFile = xmlFile;
	}
	
	public void addGuests(OfflinePlayer op) {
		getGuests().add(op);
	}
	
	public void removeGuest(OfflinePlayer op) {
		removeGuest(op.getName());
	}
	
	public void removeGuest(String op) {
		String name = op;
		for (int i = 0 ; i < getGuests().size() ; i++) {
			
			if (getGuests().get(i).getName().equalsIgnoreCase(name)) {
				getGuests().remove(i);
				i--;
			}
			
		}
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isGuest(String name) {
		for (OfflinePlayer op : getGuests()) {
			if (op.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;	
	}
	
	public OfflinePlayer getOwner() {
		return owner;
	}
	
	public int getPrice() {
		return price;
	}
	
	public XMLFile getXMLFile() {
		return this.xmlFile;
	}
	
	public boolean containPlayer(Player player) {
		for (OfflinePlayer op : getGuests()) {
			if (op.getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		
		return owner.getName().equalsIgnoreCase(player.getName());
	}
	
	public boolean containPlayer(String player) {
		for (OfflinePlayer op : getGuests()) {
			if (op.getName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		
		return owner.getName().equalsIgnoreCase(player);
	}
	
	public ArrayList<OfflinePlayer> getGuests() {
		return members;
	}

}
