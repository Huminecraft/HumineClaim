package com.aymegike.humineclaim.listener.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;

public class HumineclaimCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] arg) {
		
		if (arg.length >= 2) {
			ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(arg[0]);
			if (zc != null) {
				 
				if (arg[1].equalsIgnoreCase("info")) {
					
					if (sender instanceof Player) {
						Player player = ((Player) sender).getPlayer();
						player.sendMessage(ChatColor.DARK_PURPLE+"Liste des membres de "+ChatColor.WHITE+zc.getName());
						player.sendMessage(ChatColor.DARK_PURPLE+"~~~~~~~~~~~~~");
						if (zc.getOwner() != null)
						player.sendMessage(ChatColor.DARK_PURPLE+"propriétaire: "+ChatColor.WHITE+zc.getOwner().getName());
						player.sendMessage(ChatColor.DARK_PURPLE+"invité(e)s: ");
						if (zc.getGuests() != null)
							for (OfflinePlayer op : zc.getGuests()) {
								if (op.isOnline()) {
									player.sendMessage(ChatColor.GREEN+"    - "+ChatColor.WHITE+op.getName());
								} else {
									player.sendMessage(ChatColor.RED+"    - "+ChatColor.WHITE+op.getName());
								}
							}
						player.sendMessage(ChatColor.DARK_PURPLE+"~~~~~~~~~~~~~");
						
					} else {
						System.out.println("Liste des membres de "+zc.getName());
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						if (zc.getOwner() != null)
							System.out.println("propriétaire: "+zc.getOwner().getName());
						System.out.println("invité(e)s: ");
						if (zc.getGuests() != null)
						for (OfflinePlayer op : zc.getGuests()) {
							if (op.isOnline()) {
								System.out.println("	- "+op.getName());
							} else {
								System.out.println("	- "+op.getName());
							}
						}
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					}
					
					return true;
				} else if (arg[1].equalsIgnoreCase("add")) {
					
					if (arg.length == 3) {
						
						if (Bukkit.getPlayer(arg[2]) != null) {
							if (sender instanceof Player) {
								Player player = ((Player) sender).getPlayer();
								if (player.isOp() || zc.getOwner().getName().equalsIgnoreCase(player.getName())) {
									player.getPlayer().sendMessage(ChatColor.GREEN+"Tu viens d'ajouter "+arg[2]+" du claim "+zc.getName()+" !");
									if (!zc.containPlayer(Bukkit.getPlayer(arg[2])))
										zc.addGuests(Bukkit.getPlayer(arg[2]));
								} else {
									player.sendMessage(ChatColor.RED+"Tu n'est pas propriétaire du claim "+zc.getName()+". Demande à "+zc.getOwner().getName()+" si il veut bien inviter "+arg[2]+" pour toi.");
								}
							} else {
								System.out.println("Tu viens d'ajouter "+arg[2]+" du claim "+zc.getName()+" !");
								if (!zc.containPlayer(Bukkit.getPlayer(arg[2])))
									zc.addGuests(Bukkit.getPlayer(arg[2]));
							}
						} else {
							if (sender instanceof Player) {
								((Player) sender).getPlayer().sendMessage(ChatColor.RED+"Le joueur "+arg[2]+" n'as pas l'air d'être connecté.");
							} else {
								System.out.println("Le joueur "+arg[2]+" n'as pas l'air d'être connecté.");
							}
						}
						return true;
					} else {
						return false;
					}
					
				} else if (arg[1].equalsIgnoreCase("ban")) {
					
					if (arg.length == 3) {
						
						if (zc.containPlayer(arg[2])) {
							if (sender instanceof Player) {
								Player player = ((Player) sender).getPlayer();
								if (player.isOp() || zc.getOwner().getName().equalsIgnoreCase(player.getName())) {
									player.getPlayer().sendMessage(ChatColor.GREEN+"Tu viens de banir "+arg[2]+" du claim "+zc.getName()+" !");
									if (zc.containPlayer(arg[2]))
										zc.removeGuest(arg[2]);
								} else {
									player.sendMessage(ChatColor.RED+"Tu n'est pas propriétaire du claim "+zc.getName()+". Demande à "+zc.getOwner().getName()+" si il veut bien banir "+arg[2]+" pour toi.");
								}
							} else {
								System.out.println("Tu viens de banir "+arg[2]+" du claim "+zc.getName()+" !");
								if (zc.containPlayer(arg[2]))
									zc.removeGuest(arg[2]);
							}
						} else {
							if (sender instanceof Player) {
								((Player) sender).getPlayer().sendMessage(ChatColor.RED+"Le joueur "+arg[2]+" ne fait pas parti du claim "+zc.getName());
							} else {
								System.out.println("Le joueur "+arg[2]+" ne fait pas parti du claim "+zc.getName());
							}
						}
						return true;
					} else {
						return false;
					}
				} else if (arg[1].equalsIgnoreCase("setowner")) {
					if (arg.length == 3) {
						
						if (zc.containPlayer(arg[2])) {
							
							
							if (sender instanceof Player) {
								Player player = ((Player) sender).getPlayer();
								
								if (player.isOp() || zc.getOwner().getName().equalsIgnoreCase(player.getName())) {
									
									Player target = Bukkit.getPlayer(arg[2]);
									if (target != null) {
										player.getPlayer().sendMessage(ChatColor.GREEN+"Tu viens de donner les droits de propriété à "+arg[2]+" du claim "+zc.getName()+" !");
										if (zc.containPlayer(Bukkit.getPlayer(arg[2]))) {
											zc.addGuests(zc.getOwner());
											zc.setOwner(target);
											zc.removeGuest(target);
											target.sendMessage(ChatColor.GREEN+"Félicitation du posède les droits du claim "+zc.getName()+" !");
										}
									} else {
										player.sendMessage(ChatColor.RED+arg[2]+" n'est pas connecté.");
									}
									
									
								} else {
									player.sendMessage(ChatColor.RED+"Tu n'est pas propriétaire du claim "+zc.getName()+". Demande à "+zc.getOwner().getName()+" si il veut bien donner les droit de propriété à "+arg[2]+" pour toi.");
								}
								
								
								
							} else {
									
								Player target = Bukkit.getPlayer(arg[2]);
								if (target != null) {
									System.out.println("Tu viens de donner les droits de propriété à "+arg[2]+" du claim "+zc.getName()+" !");
									if (zc.containPlayer(Bukkit.getPlayer(arg[2]))) {
										zc.addGuests(zc.getOwner());
										zc.setOwner(target);
										zc.removeGuest(target);
										target.sendMessage(ChatColor.GREEN+"Félicitation du posède les droits du claim "+zc.getName()+" !");
									}
								} else {
										System.out.println(arg[2]+" n'est pas connecté.");
								}
							}
						
						} else {
							if (sender instanceof Player) {
								((Player) sender).getPlayer().sendMessage(ChatColor.RED+"Le joueur "+arg[2]+" ne fait pas parti du claim "+zc.getName());
							} else {
								System.out.println("Le joueur "+arg[2]+" ne fait pas parti du claim "+zc.getName());
							}
						}
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
				
			} else {
				return false;
			}
			
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] arg) {
		
		if (arg.length == 0 || arg.length == 1) {
			ArrayList<String> zoneList = new ArrayList<String>();
			if (!arg[0].equalsIgnoreCase("")) {
				
				if (sender instanceof Player) {
					for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZonesClaims()) {
						if (zc.containPlayer(((Player) sender).getPlayer())) {
							if (zc.getName().toLowerCase().startsWith(arg[0].toLowerCase())) {
								zoneList.add(zc.getName());
							}
						}
							
					}
				} else {
					for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZonesClaims()) {
						if (zc.getName().toLowerCase().startsWith(arg[0].toLowerCase())) {
							zoneList.add(zc.getName());
						}
					}
				}
				
			} else {
				if (sender instanceof Player) {
					for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZonesClaims()) {
						if (zc.containPlayer(((Player) sender).getPlayer())) {
							zoneList.add(zc.getName());
						}
							
					}
				} else {
					for (ZoneClaim zc : HumineClaim.getZoneClaimManager().getZonesClaims()) {
						zoneList.add(zc.getName());
					}
				}
			}
			return zoneList;
		} else if (arg.length == 2) {
			return getActions(arg[1]);
		} else if (arg.length == 3) {
			if (arg[2].equalsIgnoreCase("ban"))
			return getMembers(arg[0], arg[2]);
		}
		
		
		
		return null;
	}
	
	private ArrayList<String> getMembers(String zn, String start) {
		ArrayList<String> members = new ArrayList<String>();
		ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(zn);
		if (zc != null) {
			for (OfflinePlayer op : zc.getGuests()) {
				
				if(!start.equalsIgnoreCase("")) {
					if(op.getName().toLowerCase().startsWith(start.toLowerCase())) 
						members.add(op.getName());
				} else {
					members.add(op.getName());
				}
				
			}
		}
		return members;
	}
	
	private ArrayList<String> getActions(String start) {
		String[] args = {"info", "add", "ban", "setowner"};

		ArrayList<String> actions = new ArrayList<String>();
		for (String arg : args) {
			if (!start.equalsIgnoreCase("")) {
				if (arg.toLowerCase().startsWith(start.toLowerCase())) {
					actions.add(arg);
				}
			} else {
				actions.add(arg);
			}
		}
		
		return actions;
	}
	
	

}
