package com.craftrealms.CRAuth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.db.MySQL;

import org.bukkit.plugin.java.JavaPlugin;

import com.craftrealms.CRAuth.PlayerListener;

public class CRAuth extends JavaPlugin {
	private String sqlhost;
	private String sqluser;
	private String sqlpass;
	private String sqldb;
	public String server;
	MySQL MySQL;
	Connection c = null;
	
    @Override
    public void onEnable(){
    	saveDefaultConfig();
    	reloadConfig();
    	sqlhost = getConfig().getString("mysql.host");
    	sqluser = getConfig().getString("mysql.user");
    	sqlpass = getConfig().getString("mysql.pass");
    	sqldb = getConfig().getString("mysql.db");
    	server = getConfig().getString("server");
    	MySQL = new MySQL(sqlhost, "3306", sqldb, sqluser, sqlpass);
    	c = MySQL.open();
    	PlayerListener l = new PlayerListener(this);
    	getCommand("login").setExecutor(new CommandLogin(this, l));
    }
    @Override
	public void onDisable() {
		
	}
	public void info(String msg) {
		getLogger().info(msg);
	}
	public String getHash(String user) throws SQLException {
		String query = "SELECT password FROM authme WHERE username = '" + user + "'";
		Statement statement = c.createStatement();
		ResultSet r = statement.executeQuery(query);
		r.next();
		return r.getString("password");
	}
}
