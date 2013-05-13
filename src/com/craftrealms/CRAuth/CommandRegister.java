package com.craftrealms.CRAuth;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRegister implements CommandExecutor {
	private CRAuth p;
	private PlayerListener l;

	public CommandRegister(CRAuth plugin, PlayerListener listen) {
		p = plugin;
		l = listen;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Boolean newPlayer = null;
		try {
			newPlayer = p.pwds.isNew(sender.getName());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(!newPlayer) {
			sender.sendMessage("This account has already been registered! Please login with /login <password>");
			return true;
		}
		String p1;
		String p2;
		try {
			p1 = args[0];
			p2 = args[1];
		} catch(ArrayIndexOutOfBoundsException e) {
			sender.sendMessage("Please enter your password twice!");
			return false;
		}
		if(p1.equals(p2)) {
			try {
				try {
					p.pwds.storeHash(p.pwds.generateHash(p1), sender.getName());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				l.unauthed.remove(sender.getName());
				p.getServer().getPlayer(sender.getName()).teleport(l.playerloginloc.get(sender.getName()));
				try {
					p.pwds.updateIp(sender.getName(), Bukkit.getPlayer(sender.getName()).getAddress().getHostName());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sender.sendMessage("Successfully registered an account! You have been logged in automatically");
				return true;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			sender.sendMessage("The passwords you entered do not match!");
		}
		return false;
	}

}
