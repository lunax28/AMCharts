package com.equilibriummusicgroup.AMCharts.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.scene.control.Alert;
import org.apache.commons.codec.binary.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * This class is intended to make the API call to retrieve the JWT
 */
public class JsonQueryUtils {

    private int responseCode;
    private String responseTrimmed;
    private JsonObject jsonObject;
    //a Preferences object to store the token, avoiding repetitive calls
    private Preferences preferences = Preferences.userNodeForPackage(JsonQueryUtils.class);

    public JsonQueryUtils() {
        this.responseCode = 0;
        this.responseTrimmed = "";
        this.jsonObject = null;
    }


    private String getToken() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String secret = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg247w7fZYc27Dr/cBJc5laTJFwLJLgs9jQaSeUfVU1kygCgYIKoZIzj0DAQehRANCAATOLyYZpBnAweJPU/FG4j0oA/z/qLTS7OJ5P839h9Rtngfs564at6azXK7udrYsTkRVZcpCJ7H5P8cPELcF7uUQ";

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.ES256;
        byte[] publicBytes = Base64.decodeBase64(secret);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey prvKey = keyFactory.generatePrivate(keySpec);
        ECPrivateKey eckey = (ECPrivateKey)prvKey;


        long nowMillis = System.currentTimeMillis();
        System.out.println("nowMillis: " + nowMillis);
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + 3100100100L);

        long expiryPref = nowMillis + 3100100100L;

        preferences.getLong("expiry",0);
        preferences.putLong("expiry", expiryPref);

        System.out.println("EXP: " + expiry.toString());
        System.out.println("NOW: " + now.toString());

        //Setting the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setHeaderParam("alg","ES256")
                .setHeaderParam("kid","B93K9Z88NU")
                .setExpiration(expiry)
                .setIssuer("B68385H95A")
                .signWith(signatureAlgorithm, eckey);

        System.out.println("BUILDER: " + builder.compact());

        preferences.get("builderString","");
        preferences.put("builderString", builder.compact().toString());

        return builder.compact().toString();
    }


    public JsonObject getJson(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {

        String response = "";

        try {
            URL url = new URL(link);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            String basicAuth = "";

            long prefExpiry = preferences.getLong("expiry",0);

            System.out.println("prefExpiry: " + prefExpiry);
            System.out.println("System.currentTimeMillis(): " + System.currentTimeMillis());

            String prefBuilder = preferences.get("builderString","");

            System.out.println("preferences.get(builderString,0): " + preferences.get("builderString",""));

            if(preferences.getLong("expiry",0) < System.currentTimeMillis() || preferences.get("builderString","") == null) {
                basicAuth = "Bearer " + getToken();
            } else {
                basicAuth = "Bearer " + preferences.get("builderString","");
            }

            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Authorization", basicAuth);
            this.responseCode = httpCon.getResponseCode();

            System.out.println("RESPONSE CODE LINE 117: " + this.responseCode);
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            if (this.responseCode != 200) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setContentText("An error has occured. Rate limit probably reached");
                alert.showAndWait();
                //return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;

            while((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
            in.close();

        } catch (MalformedURLException ex) {
            System.out.println("MalformedURLException!!");
        } catch (ProtocolException ex) {
            System.out.println("ProtocolException!!");
        } catch (IOException ex) {
            System.out.println("IOException!!");
        }

        this.responseTrimmed = response.trim();
        this.jsonObject = new JsonParser().parse(responseTrimmed).getAsJsonObject();

        System.out.println("jsonobj: " + this.jsonObject.toString());

        return this.jsonObject;
    }
}