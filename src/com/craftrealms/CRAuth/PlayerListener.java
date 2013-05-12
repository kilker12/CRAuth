package com.craftrealms.CRAuth;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
	public HashMap<String, String> unauthed = new HashMap<String, String>();
	
	public PlayerListener(CRAuth plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void PlayerLogin(PlayerLoginEvent event) {
		unauthed.put(event.getPlayer().getName(), "0");
		event.getPlayer().sendMessage("Please login using /login <password>");
	}
	
	@EventHandler
	public void PlayerMove(PlayerMoveEvent event) {
		if(unauthed.containsKey(event.getPlayer().getName())) {
			event.setTo(event.getFrom());
		}
	}
	
	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent event) {
		if(unauthed.containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}
}
