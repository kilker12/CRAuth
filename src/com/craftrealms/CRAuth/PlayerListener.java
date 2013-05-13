package com.craftrealms.CRAuth;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
	public HashMap<String, String> unauthed = new HashMap<String, String>();
	public HashMap<String, Location> playerloginloc = new HashMap<String, Location>();
	private CRAuth p;
	
	public PlayerListener(CRAuth plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		p = plugin;
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		unauthed.put(player.getName(), "0");
		playerloginloc.put(player.getName(), player.getLocation());
		player.teleport(player.getWorld().getSpawnLocation());
		Boolean newPlayer = null;
		try {
			newPlayer = p.pwds.isNew(player.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(newPlayer) {
			p.info("New player got on");
			player.sendMessage("Please register by using the command /register <password> <passwordagain>");
		} else {
			p.info("Old player got on");
			player.sendMessage("Please login with /login <password>");
		}
	}
	
	@EventHandler
	public void PlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(player.isOnline()) {
			event.setKickMessage("You are already logged on!");
			event.setResult(null);
		}
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
