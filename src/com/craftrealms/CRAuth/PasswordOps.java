package com.craftrealms.CRAuth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PasswordOps {
	private CRAuth p;
	private static SecureRandom rnd = new SecureRandom();
	
	public PasswordOps(CRAuth plugin) {
		p = plugin;
	}
	public String getHash(String user) throws SQLException {
		String query = "SELECT password FROM authme WHERE username = '" + user + "'";
		Statement statement = p.c.createStatement();
		ResultSet r = statement.executeQuery(query);
		r.next();
		return r.getString("password");
	}
	public void storeHash(String hash, String player) throws SQLException {
		String query = "INSERT INTO  `auth`.`authme` (`id` ,`username` ,`password` ,`ip`) VALUES (NULL ,  '" + player + "',  '" + hash + "', '0')";
		Statement statement = p.c.createStatement();
		statement.execute(query);
	}
	public void updateIp(String player, String ip) throws SQLException {
		String query = "UPDATE  `auth`.`authme` SET  `ip` =  '" + ip + "' WHERE  `authme`.`username` ='" + player + "'";
		Statement statement = p.c.createStatement();
		statement.execute(query);
	}
	static final String HEXES = "0123456789ABCDEF";
	private static String getHex( byte [] raw ) {
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
	public String generateHash(String password) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		String sha256_password = null;
		String salt = createSalt(16);
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			sha256_password = getHex(md.digest()).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		sha256_password += salt;
		md.update(sha256_password.getBytes());
		String last = getHex(md.digest()).toLowerCase();
		return "$SHA$" + salt + "$" + last;
	}
	private static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] msg = new byte[40];
        rnd.nextBytes(msg);
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        byte[] digest = sha1.digest(msg);
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1,digest)).substring(0, length);
    }
	public Boolean isNew(String user) throws SQLException {
	    Statement stmt = null;
	    ResultSet rs = null;
	    stmt = p.c.createStatement();
	    rs = stmt.executeQuery("SELECT COUNT(*) FROM authme WHERE username = '" + user + "'");
	    rs.next();
	    int count = rs.getInt("COUNT(*)");
	    if(count > 0) {
	    	return false;
	    } else {
	    	return true;
	    }
	}
}
