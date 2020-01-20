package com.aymegike.humineclaim.listener.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.aymegike.humineclaim.HumineClaim;

public class ShowMenuCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			System.out.println("Cette commande doit etre entree par un joueur !");
			return false;
		}
		
		Player player = (Player) sender;
		HumineClaim.getClaimMenuManager().openMainMenu(player);
		return true;
	}

}
