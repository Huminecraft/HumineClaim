package com.aymegike.humineclaim.listener.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;

public class HumineclaimCommand implements CommandExecutor/*, TabCompleter*/ {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] arg)
	{		
		if (arg.length >= 2)
		{
			ZoneClaim zc = HumineClaim.getZoneClaimManager().getZoneClaim(Integer.parseInt(arg[0]));
			if (zc != null)
			{
				if ((arg[1].equalsIgnoreCase("buy")))
				{
					Player player = ((Player) sender).getPlayer();
					zc.buy(player);
					return true;
				}
				else
				{
					return false;
				}				
			}
			else
			{
				return false;
			}			
		}		
		return false;
	}

	/*@Override
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
	}*/
}
