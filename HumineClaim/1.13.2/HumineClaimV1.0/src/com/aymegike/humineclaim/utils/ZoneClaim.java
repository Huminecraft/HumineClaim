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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aymegike.humineclaim.HumineClaim;
import com.aypi.utils.Button;
import com.aypi.utils.Menu;
import com.aypi.utils.Square;
import com.aypi.utils.Zone;
import com.aypi.utils.inter.MenuItemListener;
import com.aypi.utils.inter.ZoneListener;
import com.aypi.utils.xml.XMLFile;

public class ZoneClaim extends Zone {	
	
	private static final int m_priority = Zone.PRIORITY_ULTRA;
	
	private int m_ID;
	private ArrayList<OfflinePlayer> m_members;
	private Location m_corner1;
	private Location m_corner2;
	private OfflinePlayer m_owner;
	private int m_price;
	private String m_name;
	private XMLFile m_xmlFile;
	private Location m_signLocation;
	private Map<String, Integer> m_limitedBlocsCounters;
	private boolean m_shulkerIsPlaced;
	
	public ZoneClaim(int ID, String name, Location c1, Location c2, OfflinePlayer owner, ArrayList<OfflinePlayer> members, int price, Location signLocation, Map<String, Integer> limitedBlocsCounters, boolean shulkerIsPlaced, XMLFile xmlFile)
	{
		super(new Square(c1, c2), c1.getWorld(), new ZoneListener() {
			
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
			public void onPlayerTryToRemoveBlock(Player player, Block block, BlockBreakEvent e)
			{
				ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(ID);
				if (zc != null && !zc.isAGuest(player)) {
					player.sendMessage(ChatColor.RED + "Cette zone est claim, tu n'as pas le droit de casser ici !");
					e.setCancelled(true);
					return;
				}
				if (isShulker(block))
				{
					zc.setShulkerIsPlaced(false);	
				}
				
				if (HumineClaim.getZoneClaimManager().isForbidenBlockToPlace(e.getBlock().getType().name()))
				{
					zc.RemoveBlocFromStock(e.getBlock().getType().name());
				}
			}
			
			@Override
			public void onPlayerTryToPlaceBlock(Player player, Block block, BlockPlaceEvent e)
			{
				ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(ID);
				if (zc == null)
				{
					return;
				}
				if (!zc.isAGuest(player))
				{
					player.sendMessage(ChatColor.RED + "Cette zone est claim, tu n'as pas le droit de poser ça ici !");
					e.setCancelled(true);
					return;
				}
				if (isShulker(block))
				{
					if (zc.canPlaceShulker())
					{
						if(block.getState() instanceof ShulkerBox)
						{
							ShulkerBox shulker = (ShulkerBox) block.getState();
							for (String forbiddenItem : HumineClaim.getZoneClaimManager().getForbiddenItems())
							{
								if (shulker.getInventory().contains(Material.valueOf(forbiddenItem)))
								{
									player.sendMessage(ChatColor.RED + "Marchandise illicite détectée ! Tu ne peux pas poser ça ici !");
									player.sendMessage(ChatColor.RED + "Cette shulkerbox contient des items interdits en zone claim, tu ne peux pas la poser ici !");
									e.setCancelled(true);
									return;
								}
							}
						}
						zc.setShulkerIsPlaced(true);						
					}
					else
					{
						if(block.getState() instanceof ShulkerBox)
						{
							ShulkerBox shulker = (ShulkerBox) block.getState();
				            Inventory inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX);
				            inv.setContents(shulker.getInventory().getContents());
				            player.openInventory(inv);
				        }
						
						player.sendMessage(ChatColor.RED + "Interdiction de poser une shulker ici. Ouverture automatique de son inventaire.");
						e.setCancelled(true);	
						return;
					}					
				}

				if (block.getType() == Material.DRAGON_EGG)
				{
					player.sendMessage("Tu ne peux pas poser ça ici ! ça serait un peu trop facile non ?");
					e.setCancelled(true);
					return;
				}
				
				if (block.getType() == Material.FIRE)
				{
					player.sendMessage("Attention, tu pourrais brûler quelqu'un ! Pas de ça ici.");
					e.setCancelled(true);
					return;
				}

				if (block.getType() == Material.TNT || block.getType() == Material.TNT_MINECART)
				{
					player.sendMessage("Mais.. C'est dangereux ça ! Pas de ça ici.");
					e.setCancelled(true);
					return;
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
			public void onPlayerTryToInteractEvent(Player player, PlayerInteractEvent e)
			{
				ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(ID);
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					if (e.getItem() != null)
					{
						if (e.getItem().getType() == Material.LAVA_BUCKET)
						{
							player.sendMessage("Attention, tu pourrais brûler quelqu'un ! Pas de ça ici.");
							e.setCancelled(true);		
						}
						if (e.getItem().getType() == Material.TNT_MINECART)
						{
							player.sendMessage("Mais.. C'est dangereux ça ! Pas de ça ici.");
							e.setCancelled(true);						
						}	
						if (e.getItem().getType() == Material.COD_BUCKET
								|| e.getItem().getType() == Material.PUFFERFISH_BUCKET
								|| e.getItem().getType() == Material.SALMON_BUCKET
								|| e.getItem().getType() == Material.TROPICAL_FISH_BUCKET
								|| e.getItem().getType() == Material.WATER_BUCKET)
								{
									if (zc != null && !zc.isAGuest(player))
									{
										player.sendMessage(ChatColor.RED + "Cette zone est claim, pas touche !");
										e.setCancelled(true);	
									}
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
						|| e.getClickedBlock().getType() == Material.DAMAGED_ANVIL
						|| isShulker(e.getClickedBlock()))
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
						if (isAtSignLocation(sign))
						{
							if (zc.getOwner() == null)
							{								
								int nbClaim = HumineClaim.getZoneClaimManager().getNbClaim(player.getName());
								/* int totalPrice = price * nbClaim;*/ //Price for pixel
								int totalPrice = price + (nbClaim * 10);
								totalPrice = totalPrice > 0 ? totalPrice : price;
								if (player.getLevel() >= totalPrice)
								{
							        /*TextComponent message = new TextComponent( "Clique ici pour confirmer l'achat de cette parcelle pour " + totalPrice + " niveaux." );
							        message.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
							        message.setItalic(true);
							        message.setBold(true);
							        message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/humineclaim " + ID + " buy " + player.getName()) );
							        player.spigot().sendMessage( message );*/
									
									Menu menu = new Menu(player, ChatColor.DARK_GREEN+"- "+ ChatColor.WHITE + "Coût : " + totalPrice + " levels " +ChatColor.DARK_GREEN+" -", 3*9, false);
										
									ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
									ItemMeta glassM = glass.getItemMeta();
									glassM.setDisplayName(" ");
									glass.setItemMeta(glassM);
										
									menu.fillLine(glass, 1);
									menu.fillLine(glass, 3);
										
									glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
									glassM = glass.getItemMeta();
									glassM.setDisplayName(" ");
									glass.setItemMeta(glassM);
										
									menu.fillLine(glass, 2);
										
									ItemStack payer = new ItemStack(Material.EMERALD_BLOCK);
									ItemMeta payerm = payer.getItemMeta();										
									payerm.setDisplayName(ChatColor.GREEN+"Confirmer l'achat pour " + totalPrice + " levels");										
									payer.setItemMeta(payerm);
										
									menu.setButton(11, new Button(payer, new MenuItemListener()
									{			
										@Override
										public void onItemClick()
										{
											zc.buy(player);
											menu.closePlayerMenu();				
										}			
									}));
										
									ItemStack back = new ItemStack(Material.REDSTONE_BLOCK);
									ItemMeta backm = back.getItemMeta();
									backm.setDisplayName(ChatColor.RED+"Annuler");
									back.setItemMeta(backm);
									menu.setButton(15, new Button(back, new MenuItemListener()
									{			
										@Override
										public void onItemClick()
										{
											menu.closePlayerMenu();		
										}			
									}));
									
									menu.openMenu();
								}
								else
								{
									player.sendMessage("Tu n'as pas assez d'expérience pour acheter cette parcelle ! Elle coûte " + totalPrice + " niveaux..");
								}
					        	//end if enough money
							}
							else
							{
								player.sendMessage("Cette parcelle a déjà été achetée !");
							}
						}
					}
				}	
			}
			
			@Override
			public void onPlayerMoveInZone(Player arg0, PlayerMoveEvent arg1)
			{
				
			}
			
			@Override
			public void onPlayerExitZone(Player arg0) {
				
			}
			
			@Override
			public void onPlayerEnterZone(Player arg0) {
				
			}
			
			@Override
			public void onEntitySuffersDamages(Entity arg0, EntityDamageByEntityEvent arg1) {
				
			}
			
			@Override
			public void onEntityMakesDamages(Entity arg0, EntityDamageByEntityEvent arg1) {
				
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
			
		}, m_priority);

		if (c1.getWorld() != c2.getWorld())
		{
			System.out.println("ATTENTION !! Zone claim creee avec deux coins de deux dimensions differentes. La premiere a ete prise par defaut.");
		}
		if (ID == -1)
		{
			System.out.println("INVALID ID WHEN CREATING A ZONE CLAIM !!");
			return;
		}
		
		this.m_ID = ID;
		this.m_name = name;
		this.m_corner1 = c1;
		this.m_corner2 = c2;
		this.m_owner = owner;
		this.m_members = members;
		this.m_price = price;
		this.m_xmlFile = xmlFile;
		this.m_signLocation = signLocation;
		this.m_limitedBlocsCounters = limitedBlocsCounters;
		this.m_shulkerIsPlaced = shulkerIsPlaced;
	}
	
	// -------------------------------------------------------------------------------------------------
	//    GETTERS
	// -------------------------------------------------------------------------------------------------
	
	public int getID()
	{
		return m_ID;
	}

	public ArrayList<OfflinePlayer> getGuests() {
		return m_members;
	}
	
	public String getName() {
		return m_name;
	}
	
	public OfflinePlayer getOwner() {
		return m_owner;
	}
	
	public int getPrice() {
		return m_price;
	}
	
	public Location getSignLocation() {
		return m_signLocation;
	}
	
	public XMLFile getXMLFile() {
		return this.m_xmlFile;
	}
	
	public boolean canPlaceShulker()
	{
		return !m_shulkerIsPlaced;
	}
	
	// -------------------------------------------------------------------------------------------------
	//    SETTERS
	// -------------------------------------------------------------------------------------------------
	
	public void setShulkerIsPlaced(boolean shulkerIsPlaced)
	{
		this.m_shulkerIsPlaced = shulkerIsPlaced;
		updateXMLFile(m_name);
	}
	
	public void setName(String name) {
		String oldName = this.m_name;
		this.m_name = name;
		updateXMLFile(oldName);
	}
	
	public void setOwner(OfflinePlayer player) {
		this.m_owner = player;
		updateXMLFile(m_name);
	}
	
	public void setGuests(ArrayList<OfflinePlayer> guests) {
		m_members = guests;
		updateXMLFile(m_name);
	}
	
	public void setPrice(int price) {
		this.m_price = price;
		updateXMLFile(m_name);
	}

	public void setSignLocation(Location signLocation) {
		this.m_signLocation = signLocation;
		updateXMLFile(m_name);
	}
	
	public void setXMLFile(XMLFile xmlFile) {
		this.m_xmlFile = xmlFile;
	}

	// -------------------------------------------------------------------------------------------------
	//    UTILS
	// -------------------------------------------------------------------------------------------------
	
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
	
	public boolean isForbidenBlock(String item)
	{
		return m_limitedBlocsCounters.containsKey(item);
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
			currentAmount = m_limitedBlocsCounters.get(item);
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
		if (m_limitedBlocsCounters.containsKey(item))
		{
			int currentAmount = m_limitedBlocsCounters.get(item);			
			m_limitedBlocsCounters.replace(item, currentAmount, currentAmount+1);			
		}
		else
		{
			m_limitedBlocsCounters.put(item, 1);
		}
		updateXMLFile(m_name);
	}
	
	public void RemoveBlocFromStock(String item)
	{
		if (m_limitedBlocsCounters.containsKey(item))
		{
			int currentAmount = m_limitedBlocsCounters.get(item);			
			m_limitedBlocsCounters.replace(item, currentAmount, currentAmount-1 > 0 ? currentAmount-1 : 0);			
			updateXMLFile(m_name);
		}
	}
	
	public void buy(Player player) {

		if (m_owner == null)
		{
			int nbClaim = HumineClaim.getZoneClaimManager().getNbClaim(player.getName());
			/* int totalPrice = price * nbClaim;*/ //Price for pixel
			int totalPrice = m_price + (nbClaim * 10);
			totalPrice = totalPrice > 0 ? totalPrice : m_price;
			if (player.getLevel() >= totalPrice)
			{
				this.m_owner = Bukkit.getOfflinePlayer(player.getUniqueId());
				player.setLevel(player.getLevel()-totalPrice);
				player.sendMessage(ChatColor.GREEN + "Bravo ! Tu viens d'obtenir cette parcelle !");
				updateXMLFile(m_name);
			}
			else
			{
				player.sendMessage("Tu n'as plus ce qu'il faut pour payer, désolé !");
			}
		}
		else
		{
			player.sendMessage("Cette parcelle vient d'être achetée !");
		}
	}
	
	public void updateXMLFile(String oldName)
	{
		try {
	        // input the (modified) file content to the StringBuffer "input"
	        BufferedReader file = new BufferedReader(new FileReader("./plugins/HumineClaim/xml-reader/" + m_xmlFile.getFile().getName()));
	        StringBuffer inputBuffer = new StringBuffer();
	        String line;
	        while ((line = file.readLine()) != null)
	        {
	        	if (line.contains("<ID>" + m_ID + "</ID>"))
	        	{	      
	        		/*String[] splittedLine = line.split("'");
	        		if (splittedLine.length > 1 && splittedLine[1].equals(oldName))
	        		{ */      
			            line = "            <ID>" + m_ID + "</ID>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');  
			            
			            line = "            <claim-name>'" + m_name + "'</claim-name>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');  
	
			            line = "            <location world=\"'" + m_corner1.getWorld().getName() + "'\" x=\"" + m_corner1.getBlockX() +  "\" y=\"" + m_corner1.getBlockY() + "\" z=\""+ m_corner1.getBlockZ() +  "\">"; // replace the line here
			            inputBuffer.append(line);
			            inputBuffer.append('\n');		
	
			            line = "				<corner>1</corner>\n";
			            inputBuffer.append(line);
	
			            line = "            </location>\n";
			            inputBuffer.append(line);	
	
			            line = "            <location world=\"'" + m_corner2.getWorld().getName() + "'\" x=\"" + m_corner2.getBlockX() +  "\" y=\"" + m_corner2.getBlockY() + "\" z=\""+ m_corner2.getBlockZ() +  "\">"; // replace the line here
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
			            line = "				<corner>2</corner>\n";
			            inputBuffer.append(line);
	
			            line = "            </location>\n";
			            inputBuffer.append(line);
	
			            line = "            <owner>'" + ((m_owner == null) ? "" : m_owner.getUniqueId().toString()) + "'</owner>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	 
	
			            line = "            <guests>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
	
			            if (this.m_members != null && !this.m_members.isEmpty())
			            {
			            	for (OfflinePlayer guest : this.m_members)
			            	{
			            		if (guest != null)
			            		{
			    		            line = "				<guest>'" + guest.getUniqueId().toString() + "'</guest>";
			    		            inputBuffer.append(line);
			    		            inputBuffer.append('\n');
			            		}
			            	}
			            }
	
			            line = "            </guests>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');
			            
	
			            line = "            <price>" + m_price + "</price>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
			            
	
			            line = "            <sign world=\"'" + m_signLocation.getWorld().getName() + "'\" x=\"" + m_signLocation.getBlockX() +  "\" y=\"" + m_signLocation.getBlockY() + "\" z=\""+ m_signLocation.getBlockZ() +  "\"></sign>"; // replace the line here
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
			            
	
			            line = "            <limited-blocs>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
	
						for (Map.Entry<String, Integer> entry : this.m_limitedBlocsCounters.entrySet())
						{
	
						    line = "				<limited-bloc-counter bloc=\"'" + entry.getKey() + "'\" current-amount=\"" + entry.getValue() + "\"></limited-bloc-counter>";
				            inputBuffer.append(line);
				            inputBuffer.append('\n');
						}
	
			            line = "            </limited-blocs>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
	
			            line = "            <placed-shulker>" + (m_shulkerIsPlaced ? 1 : 0) + "</placed-shulker>";
			            inputBuffer.append(line);
			            inputBuffer.append('\n');	
			            
			            while (!line.contains("</zone-claim>") && line != null)
			            {
			            	//permettre l'update des blocs limites
				            line = file.readLine(); 	
			            }
	        		//}
	        	}
	        	
		        inputBuffer.append(line);
		        inputBuffer.append('\n');
	        }
	        file.close();
	        // write the new string with the replaced line OVER the same file
	        FileOutputStream fileOut = new FileOutputStream("./plugins/HumineClaim/xml-reader/" + m_xmlFile.getFile().getName());
	        fileOut.write(inputBuffer.toString().getBytes());
	        fileOut.close();
	        

	    } catch (Exception exception) {
	        System.out.println("Problem reading file : " + exception.getMessage() + " et " + exception);
	    }	
	}
	
	public boolean isAGuest(Player player) {		
		
		if (!m_members.isEmpty())
		{
			for (OfflinePlayer op : m_members) {
				if (op.getUniqueId().equals(player.getUniqueId())) {
					return true;
				}
			}
		}
		return m_owner != null && m_owner.getUniqueId().equals(player.getUniqueId());
	}
	
	
	
	public void addGuests(OfflinePlayer op) {
		getGuests().add(op);
		updateXMLFile(m_name);
	}
	
	public void removeGuest(OfflinePlayer op) {
		m_members.remove(op);
		updateXMLFile(m_name);
	}
	
	public boolean isGuest(String name) {
		for (OfflinePlayer op : getGuests()) {
			if (op.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;	
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
		
		return m_owner != null && player != null && m_owner.getName().equalsIgnoreCase(player.getName());
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
		
		return m_owner != null && m_owner.getName().equalsIgnoreCase(player);
	}
}
