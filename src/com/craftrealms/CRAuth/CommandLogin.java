package com.craftrealms.CRAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandLogin implements CommandExecutor {
	private CRAuth p;
	private PlayerListener l;
	
	public CommandLogin(CRAuth plugin, PlayerListener l) {
		this.p = plugin;
		this.l = l;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String hash = null;
		String password = args[0];
		MessageDigest md = null;
		String sha256_password = null;
		String temp = null;
		try {
			hash = p.getHash(sender.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] sha_info = hash.split("\\$");
		if(sha_info[1].equals("SHA")) {
			try {
				md = MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes());
				sha256_password = getHex(md.digest()).toLowerCase();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			sha256_password += sha_info[2];
			md.update(sha256_password.getBytes());
			temp = getHex(md.digest()).toLowerCase();
			if(sha_info[3].equals(temp)) {
				p.info("Succesfull login was made!");
				l.unauthed.remove(sender.getName());
				return true;
			} else {
				sender.sendMessage("Bad password!");
			}
		}
		return false;
	}
	static final String HEXES = "0123456789ABCDEF";
	public static String getHex( byte [] raw ) {
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	}

}
