package com.aymegike.humineclaim.menu;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import com.aymegike.humineclaim.HumineClaim;
import com.aymegike.humineclaim.utils.ZoneClaim;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class ItemMenuBank {
	
	public static ItemStack claimOwnerItem(Player p) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(ChatColor.GOLD + "Terrains de " + p.getName());
		item.setItemMeta(meta);
		
		return item;
	}

	public static ItemStack friendClaimsItem() {
		ItemStack item = new ItemStack(Material.BIRCH_FENCE_GATE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Terrains des amis");
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack claimFriendItem(Player p) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(ChatColor.GOLD + "Terrains des amis de " + p.getName());
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack friendZoneItem(ZoneClaim zc) {
		ItemStack item = new ItemStack(Material.OAK_SIGN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Terrain : " + zc.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Appartenant à " + zc.getOwner().getName());
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack claimGuestItem(OfflinePlayer p) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Invité : " + p.getName());
		NamespacedKey key = new NamespacedKey(HumineClaim.getPlugin(HumineClaim.class), "playerIDKey");
		meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, p.getUniqueId().toString());
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack claimItem(ZoneClaim zc) {
		ItemStack item = new ItemStack(Material.OAK_SIGN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Terrain : " + zc.getName());
		item.setItemMeta(meta);
		
		return item;
	}

	public static ItemStack changeNameItem() {
		ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Changer le nom du terrain");
		item.setItemMeta(meta);
		
		return item;
	}
		
	public static ItemStack addGuestItem() {
		ItemStack item = applySkullTexture();
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Ajouter un invité");
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack applySkullTexture() {
		net.minecraft.server.v1_15_R1.ItemStack item = CraftItemStack.asNMSCopy(new ItemStack(Material.PLAYER_HEAD, 1));
	    NBTTagCompound tag;
	    if (item.hasTag()) {
	        tag = item.getTag();
	    } else {
	        tag = new NBTTagCompound();
	    }
	    NBTTagCompound skullOwner = new NBTTagCompound();
	    NBTTagCompound properties = new NBTTagCompound();
	    NBTTagList textures = new NBTTagList();
	    NBTTagCompound texture = new NBTTagCompound();

	    texture.setString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19=");
	    textures.add(texture);
	    properties.set("textures", textures);
	    skullOwner.set("Properties", properties);
	    tag.set("SkullOwner", skullOwner);

	    item.setTag(tag);
	    return CraftItemStack.asBukkitCopy(item);
	}
	 
	/*/give @p minecraft:player_head{display:{Name:"{\"text\":\"Starbucks Coffee\"}"},SkullOwner:{Id:"9f6c5ef2-14fb-4737-a2a1-2f4e07c83a21",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTU5ZmI5NTdiNzM2MzBiMTczYzIyNTJkNTZkODI5NzVmZGYwZThkZjVjMGMzZjNhNTE0MjU4NWExYjM3MDA5MyJ9fX0="}]}}} 1
	/give @p minecraft:player_head{display:{Name:"{\"text\":\"Green Plus\"}"},SkullOwner:{Id:"32ddfc8c-ca3a-44d2-a3f6-43b5e055b098",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19"}]}}} 1
	/give @p minecraft:player_head{display:{Name:"{\"text\":\"Lime\"}"},SkullOwner:"Lime"} 1
	*/
	public static ItemStack cancelItem() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner("Sushi");
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Annuler");
		item.setItemMeta(meta);
		
		return item;
	}

	public static ItemStack removeGuestItem() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner("sean1346");
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Confirmer la supression.");
		item.setItemMeta(meta);
		
		return item;
	}

	public static ItemStack itemQuit(boolean isMainmenu) {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + (isMainmenu ? "Quitter" : "Retour"));
		item.setItemMeta(meta);

		return item;
	}
}
