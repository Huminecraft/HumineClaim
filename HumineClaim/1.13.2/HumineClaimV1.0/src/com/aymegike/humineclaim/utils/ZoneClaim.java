package com.aymegike.humineclaim.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.Inventory;

import com.aymegike.humineclaim.HumineClaim;
import com.aypi.utils.Square;
import com.aypi.utils.Zone;
import com.aypi.utils.inter.ZoneListener;
import com.aypi.utils.xml.XMLFile;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ZoneClaim extends Zone {	
	
	private static final int priority = Zone.PRIORITY_ULTRA;
	
	private ArrayList<OfflinePlayer> members;
	private Location corner1;
	private Location corner2;
	private OfflinePlayer owner;
	private int price;
	private String name;
	private XMLFile xmlFile;
	private Location signLocation;
	private Map<String, Integer> limitedBlocsCounters;
	private boolean shulkerIsPlaced;
	
	public ZoneClaim(String name, Location c1, Location c2, OfflinePlayer owner, ArrayList<OfflinePlayer> members, int price, Location signLocation, Map<String, Integer> limitedBlocsCounters, boolean shulkerIsPlaced, XMLFile xmlFile) {
		super(new Square(c1, c2), new ZoneListener() {
			
			private boolean isAtSignLocation(Block sign) {
					return sign != null && sign.getLocation().equals(signLocation);
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
				ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(name);
				if (zc != null && !zc.isAGuest(player)) {
					player.sendMessage(ChatColor.RED + "Cette zone est claim, tu n'as pas le droit de casser ici !");
					e.setCancelled(true);
				}
				if (isShulker(block))
				{
					zc.UpdatePlacementShulker(false);	
				}
				
				if (HumineClaim.getZoneClaimManager().isForbidenItem(e.getBlock().getType().name()))
				{
					zc.RemoveBlocFromStock(e.getBlock().getType().name());
				}
			}
			
			@Override
			public void onPlayerTryToPlaceBlock(Player player, Block block, BlockPlaceEvent e) {
				ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(name);
				if (zc != null && !zc.isAGuest(player))
				{
					player.sendMessage(ChatColor.RED + "Cette zone est claim, tu n'as pas le droit de poser ici !");
					e.setCancelled(true);
					return;
				}
				if (isShulker(block))
				{
					if (zc.canPlaceShulker())
					{
						zc.UpdatePlacementShulker(true);						
					}
					else
					{
						if(block.getState() instanceof ShulkerBox)
						{
							ShulkerBox shulker = (ShulkerBox) block.getState();
				            Inventory inv = Bukkit.createInventory(null, 27, "Shulker Box");
				            inv.setContents(shulker.getInventory().getContents());
				            player.openInventory(inv);
				        }
						
						player.sendMessage(ChatColor.RED + "Interdiction de poser une shulker en zone claim.");
						e.setCancelled(true);	
					}					
				}
				
				if (block.getType() == Material.FIRE)
				{
					player.sendMessage("Attention, tu pourrais bruler quelqu'un ! Pas de ça ici.");
					e.setCancelled(true);
				}

				if (zc.canPlaceBlock(e.getBlock().getType().name()))
				{
					if (HumineClaim.getZoneClaimManager().isForbidenBlockToPlace(e.getBlock().getType().name()))
					{
						zc.AddBlocToStock(e.getBlock().getType().name());
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Tu ne peux plus poser ce type de bloc ici !");
					e.setCancelled(true);	
				}
			}
			
			@Override
			public void onPlayerTryToInteractEvent(Player player, PlayerInteractEvent e) {
				ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(name);
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					if (e.getItem() != null)
					{
						if (e.getItem().getType() == Material.LAVA_BUCKET)
						{
							player.sendMessage("Attention, tu pourrais brûler quelqu'un ! Pas de ça ici.");
							e.setCancelled(true);		
						}
						if (e.getItem().getType() == Material.COD_BUCKET
								|| e.getItem().getType() == Material.PUFFERFISH_BUCKET
								|| e.getItem().getType() == Material.SALMON_BUCKET
								|| e.getItem().getType() == Material.TROPICAL_FISH_BUCKET
								|| e.getItem().getType() == Material.WATER_BUCKET)
								{
									player.sendMessage(ChatColor.RED + "Cette zone est claim, pas touche !");
									e.setCancelled(true);							
								}
						if (e.getItem().getType() == Material.BUCKET && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.WATER)
						{
							player.sendMessage(ChatColor.RED + "Cette zone est claim, pas touche !");
							e.setCancelled(true);								
						}
					}
					
					if (e.getClickedBlock() != null)
					{
					if (e.getClickedBlock().getType() == Material.CHEST 
					|| e.getClickedBlock().getType() == Material.TRAPPED_CHEST
					|| e.getClickedBlock().getType() == Material.FURNACE 
					|| e.getClickedBlock().getType() == Material.DROPPER 
					|| e.getClickedBlock().getType() == Material.DISPENSER 
					|| e.getClickedBlock().getType() == Material.HOPPER
					|| e.getClickedBlock().getType() == Material.BREWING_STAND
					|| e.getClickedBlock().getType() == Material.ANVIL
					|| e.getClickedBlock().getType() == Material.CHIPPED_ANVIL
					|| e.getClickedBlock().getType() == Material.DAMAGED_ANVIL)
					{
						
						if (zc != null && !zc.isAGuest(player))
						{
							player.sendMessage(ChatColor.RED + "Cette zone est claim, pas touche !");
							e.setCancelled(true);
							return;
						}
					}
					}
					
					if (isSign(e.getClickedBlock()))
					{
						Block sign = e.getClickedBlock();
						if (isAtSignLocation(sign) && zc.getOwner() == null)
						{
							//if (has enough monneyyyy)
							//{
							int nbClaim = HumineClaim.getZoneClaimManager().getNbClaim(player.getName());
							int totalPrice = price * nbClaim;
							totalPrice = totalPrice > 0 ? totalPrice : price;
					        TextComponent message = new TextComponent( "Clique ici pour confirmer l'achat de cette parcelle pour " + totalPrice + " pixels." );
					        message.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
					        message.setItalic(true);
					        message.setBold(true);
					        message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/humineclaim " + name + " buy " + player.getName()) );
					        player.spigot().sendMessage( message );		
					        //end if enough money
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

			@Override
			public void onPortalCreate(PortalCreateEvent e) {
				e.setCancelled(true);				
			}		

			@Override
			public void onEntityDeath(Entity entity, EntityDeathEvent e)
			{
				if (entity instanceof Wither)
				{
					e.getDrops().clear();
				}
			}

			@Override
			public void onExplosionPrime(ExplosionPrimeEvent e)
			{
				if (e.getEntityType() == EntityType.WITHER || e.getEntityType() == EntityType.WITHER_SKULL)
				{
					e.setCancelled(true);
				}
			}
			
		}, priority);
		
		this.name = name;
		this.corner1 = c1;
		this.corner2 = c2;
		this.owner = owner;
		this.members = members;
		this.price = price;
		this.xmlFile = xmlFile;
		this.signLocation = signLocation;
		this.limitedBlocsCounters = limitedBlocsCounters;
		this.shulkerIsPlaced = shulkerIsPlaced;
	}

	public void UpdatePlacementShulker(boolean shulkerIsPlaced)
	{
		this.shulkerIsPlaced = shulkerIsPlaced;
		updateXMLFile();
	}
	
	public static boolean isSign(Block b)
	{
		Material m = b.getType();
		return (m == Material.ACACIA_SIGN ||
				m == Material.BIRCH_SIGN ||
				m == Material.DARK_OAK_SIGN ||
				m == Material.JUNGLE_SIGN ||
				m == Material.OAK_SIGN ||
				m == Material.SPRUCE_SIGN);
	}
	
	public boolean canPlaceShulker()
	{
		return !shulkerIsPlaced;
	}

	public boolean isForbidenBlock(String item)
	{
		return limitedBlocsCounters.containsKey(item);
	}
	
	public boolean canPlaceBlock(String item)
	{
		if (!HumineClaim.getZoneClaimManager().isForbidenBlockToPlace(item))
		{
			return true;
		}

		int maximum = HumineClaim.getZoneClaimManager().getMaximumAmountToPlace(item);
		int currentAmount = 0;
		if (isForbidenBlock(item))
		{
			currentAmount = limitedBlocsCounters.get(item);
		}

		if (currentAmount < maximum)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void AddBlocToStock(String item)
	{
		if (limitedBlocsCounters.containsKey(item))
		{
			int currentAmount = limitedBlocsCounters.get(item);			
			limitedBlocsCounters.replace(item, currentAmount, currentAmount+1);			
		}
		else
		{
			limitedBlocsCounters.put(item, 1);
		}
		updateXMLFile();
	}
	
	public void RemoveBlocFromStock(String item)
	{
		if (limitedBlocsCounters.containsKey(item))
		{
			int currentAmount = limitedBlocsCounters.get(item);			
			limitedBlocsCounters.replace(item, currentAmount, currentAmount-1 > 0 ? currentAmount-1 : 0);			
			updateXMLFile();
		}
	}
	
	public void buy(Player player) {

		this.owner = Bukkit.getOfflinePlayer(player.getName());
		player.sendMessage(ChatColor.GREEN + "Bravo ! Tu viens d'obtenir cette parcelle !");
		updateXMLFile();
	}
	
	public void updateXMLFile()
	{
		try {
	        // input the (modified) file content to the StringBuffer "input"
	        BufferedReader file = new BufferedReader(new FileReader("./plugins/HumineClaim/xml-reader/" + xmlFile.getFile().getName()));
	        StringBuffer inputBuffer = new StringBuffer();
	        String line;
	        while ((line = file.readLine()) != null)
	        {
	        	if (line.contains(name))
	        	{	      
		            line = "            <claim-name>'" + name + "'</claim-name>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');  

		            line = "            <location world=\"'" + corner1.getWorld().getName() + "'\" x=\"" + corner1.getBlockX() +  "\" y=\"" + corner1.getBlockY() + "\" z=\""+ corner1.getBlockZ() +  "\">"; // replace the line here
		            inputBuffer.append(line);
		            inputBuffer.append('\n');		

		            line = "				<corner>1</corner>\n";
		            inputBuffer.append(line);

		            line = "            </location>\n";
		            inputBuffer.append(line);	

		            line = "            <location world=\"'" + corner2.getWorld().getName() + "'\" x=\"" + corner2.getBlockX() +  "\" y=\"" + corner2.getBlockY() + "\" z=\""+ corner2.getBlockZ() +  "\">"; // replace the line here
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	
		            line = "				<corner>2</corner>\n";
		            inputBuffer.append(line);

		            line = "            </location>\n";
		            inputBuffer.append(line);

		            line = "            <owner>'" + ((owner == null) ? "" : owner.getName()) + "'</owner>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	 

		            line = "            <guests>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	

		            if (this.members != null && !this.members.isEmpty())
		            {
		            	for (OfflinePlayer guest : this.members)
		            	{
		            		if (guest != null)
		            		{
		    		            line = "				<guest>'" + guest.getName() + "'</guest>";
		    		            inputBuffer.append(line);
		    		            inputBuffer.append('\n');
		            		}
		            	}
		            }

		            line = "            </guests>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');
		            

		            line = "            <price>" + price + "</price>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	
		            

		            line = "            <sign world=\"'" + signLocation.getWorld().getName() + "'\" x=\"" + signLocation.getBlockX() +  "\" y=\"" + signLocation.getBlockY() + "\" z=\""+ signLocation.getBlockZ() +  "\"></sign>"; // replace the line here
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	
		            

		            line = "            <limited-blocs>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	

					for (Map.Entry<String, Integer> entry : this.limitedBlocsCounters.entrySet())
					{

					    line = "				<limited-bloc-counter bloc=\"'" + entry.getKey() + "'\" current-amount=\"" + entry.getValue() + "\"></limited-bloc-counter>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');
					}

		            line = "            </limited-blocs>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	

		            line = "            <placed-shulker>" + (shulkerIsPlaced ? 1 : 0) + "</placed-shulker>";
		            inputBuffer.append(line);
		            inputBuffer.append('\n');	
		            
		            while (!line.contains("</zone-claim>") && line != null)
		            {
		            	//permettre l'update des blocs limites
			            line = file.readLine(); 	
		            }				 		
	        	}
	        	
		        inputBuffer.append(line);
		        inputBuffer.append('\n');
	        }
	        file.close();
	        // write the new string with the replaced line OVER the same file
	        FileOutputStream fileOut = new FileOutputStream("./plugins/HumineClaim/xml-reader/" + xmlFile.getFile().getName());
	        fileOut.write(inputBuffer.toString().getBytes());
	        fileOut.close();
	        

	    } catch (Exception exception) {
	        System.out.println("Problem reading file : " + exception.getMessage() + " et " + exception);
	    }	
	}
	
	public boolean isAGuest(Player player) {		
		
		if (this.members.isEmpty())
		{
			for (OfflinePlayer op : this.members) {
				if (op.getName().equalsIgnoreCase(player.getName())) {
					return true;
				}
			}
		}
		return this.owner != null && this.owner.getName().equalsIgnoreCase(player.getName());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOwner(OfflinePlayer player) {
		this.owner = player;
		updateXMLFile();
	}
	
	public void setGuests(ArrayList<OfflinePlayer> guests) {
		members = guests;
		updateXMLFile();
	}
	
	public void setPrice(int price) {
		this.price = price;
		updateXMLFile();
	}

	public void setSignLocation(Location signLocation) {
		this.signLocation = signLocation;
		updateXMLFile();
	}
	
	public void setXMLFile(XMLFile xmlFile) {
		this.xmlFile = xmlFile;
	}
	
	public void addGuests(OfflinePlayer op) {
		getGuests().add(op);
		updateXMLFile();
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
		updateXMLFile();
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
	
	public Location getSignLocation() {
		return signLocation;
	}
	
	public XMLFile getXMLFile() {
		return this.xmlFile;
	}
	
	public boolean containPlayer(Player player) {
		if (!getGuests().isEmpty())
		{
			for (OfflinePlayer op : getGuests()) {
				if ( op !=null && player != null && op.getName().equalsIgnoreCase(player.getName())) {
					return true;
				}
			}
		}
		
		return owner != null && player != null && owner.getName().equalsIgnoreCase(player.getName());
	}
	
	public boolean containPlayer(String player) {
		if (!getGuests().isEmpty())
		{
			for (OfflinePlayer op : getGuests()) {
				if (op.getName().equalsIgnoreCase(player)) {
					return true;
				}
			}
		}
		
		return owner != null && owner.getName().equalsIgnoreCase(player);
	}
	
	public ArrayList<OfflinePlayer> getGuests() {
		return members;
	}

}
