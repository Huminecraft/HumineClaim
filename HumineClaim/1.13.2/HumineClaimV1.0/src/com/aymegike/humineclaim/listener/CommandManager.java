package com.aymegike.humineclaim.listener;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.listener.command.HumineclaimCommand;

public class CommandManager {
	
	public CommandManager(HumineClaim pl)
	{
		pl.getCommand("humineclaim").setExecutor(new HumineclaimCommand());
		//pl.getCommand("humineclaim").setTabCompleter(new HumineclaimCommand());
	}

}
