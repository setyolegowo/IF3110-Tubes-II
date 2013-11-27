/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.if3110.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Setyo Legowo <setyo.legowo@live.com>
 */
public class Authentication {
    private String username;
    private String password;
    private int id_num;
    
    public Authentication(String username, String password)
    {
        this.id_num = Integer.MAX_VALUE;
        this.username = username;
        this.password = password;
    }
    
    protected boolean isValidInput()
    {
        boolean result;
        Pattern pattern = Pattern.compile("/^[a-zA-Z_.]+$/");
        if("".equals(username) || "".equals(password)) {
            result = false;
        } else {
            Matcher matcher = pattern.matcher(username);
            if(!matcher.find())
                result = false;
            else {
                result = (username.length() >= 8 && username.length() <= 16 &&
                        password.length() >= 6 && password.length() <= 18);
            }
        }
        return result;
    }
    
    protected boolean isMemberExist() throws Exception
    {
        boolean result = true;
        DBConnector dbCon = DBConnector.getInstance();
        Connection con = dbCon.getConnection();
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery("SELECT user_id FROM __user_login " +
                "WHERE nama_pengguna = '"+ username + "';");
        if(res.next()) {
            id_num = res.getInt("user_id");
        } else
            result = false;
        return result;
    }
    
    protected boolean isKeywordPass() throws Exception
    {
        boolean result;
        DBConnector dbCon = DBConnector.getInstance();
        Connection con = dbCon.getConnection();
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery("SELECT `kata_sandi` FROM __user_login " +
                "WHERE nama_pengguna = '"+ username + "';");
        if(res.next()) {
            String sandi = password_generator(password);
            String sandi_db = res.getString("kata_sandi");
            result = sandi.equals(sandi_db);
        } else
            result = false;
        return result;
    }
    
    protected String password_generator(String inputsandi)
            throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(inputsandi.getBytes());
        String encryptedString = new String(messageDigest.digest());
        return encryptedString;
    }
    
    public boolean CheckResultLogin() throws Exception
    {
        boolean result;
        if(isValidInput()) {
            if(isMemberExist()) {
                result = isKeywordPass();
            } else
                result = false;
        } else
            result = false;
        return result;
    }
    
    public ArrayList<String> getNameUser() throws Exception
    {
        ArrayList<String> result = null;
        if(id_num != Integer.MAX_VALUE)
        {
            DBConnector dbCon = DBConnector.getInstance();
            Connection con = dbCon.getConnection();
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT nama_lengkap, email "
                    + "FROM pelanggan_id WHERE user_id = " + id_num + ";");
            if(res.next()) {
                result = new ArrayList<String>();
                result.add(res.getString("nama_lengkap"));
                result.add(res.getString("email"));
            }
        }
        return result;
    }
    
    public int getUserID()
    {
        return id_num;
    }
}