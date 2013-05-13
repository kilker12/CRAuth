package com.craftrealms.CRAuth;

import java.sql.Connection;
import java.util.List;

import net.db.MySQL;

import org.bukkit.plugin.java.JavaPlugin;

import com.craftrealms.CRAuth.PlayerListener;

public class CRAuth extends JavaPlugin {
	private String sqlhost;
	private String sqluser;
	private String sqlpass;
	private String sqldb;
	public String server;
	public Boolean bungee;
	public List<String> allowedcmds;
	MySQL MySQL;
	Connection c = null;
	public PasswordOps pwds;
	
    @Override
    public void onEnable(){
    	saveDefaultConfig();
    	reloadConfig();
    	sqlhost = getConfig().getString("mysql.host");
    	sqluser = getConfig().getString("mysql.user");
    	sqlpass = getConfig().getString("mysql.pass");
    	sqldb = getConfig().getString("mysql.db");
    	server = getConfig().getString("server");
    	bungee = getConfig().getBoolean("bungee");
    	allowedcmds = getConfig().getStringList("allowedCommands");
    	MySQL = new MySQL(sqlhost, "3306", sqldb, sqluser, sqlpass);
    	c = MySQL.open();
    	pwds = new PasswordOps(this);
    	PlayerListener l = new PlayerListener(this);
    	getCommand("login").setExecutor(new CommandLogin(this, l));
    	getCommand("register").setExecutor(new CommandRegister(this, l));
    }
    @Override
	public void onDisable() {
		
	}
	public void info(String msg) {
		getLogger().info(msg);
	}
}
