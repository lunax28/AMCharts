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

public class JsonQueryUtils {

    private int responseCode;
    private String responseTrimmed;
    private JsonObject jsonObject;

    public JsonQueryUtils() {

        this.responseCode = 0;
        this.responseTrimmed = "";
        this.jsonObject = null;
    }




    private static String getToken() throws NoSuchAlgorithmException, InvalidKeySpecException {
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
        long expiryMillis = 1503890102;
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + 100000000);

        System.out.println("EXP: " + expiry.toString());
        System.out.println("NOW: " + now.toString());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setHeaderParam("alg","ES256")
                .setHeaderParam("kid","B93K9Z88NU")
                .setExpiration(expiry)
                .setIssuer("B68385H95A")
                .signWith(signatureAlgorithm, eckey);

        System.out.println("BUILDER: " + builder.compact());

        return builder.compact().toString();

    }


    public JsonObject getJson(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String response = "";
        try {

            URL url = new URL(link);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            /**
            if (this.tokenString.isEmpty()) {
                this.tokenString = this.getToken();
                System.out.println("###\nREQUESTED NEW TOKEN!!!\n###");
            }
             */

            String basicAuth = "Bearer " + "eyJhbGciOiJFUzI1NiIsImtpZCI6IkI5M0s5Wjg4TlUifQ.eyJpYXQiOjE1MDQyNjEyMTgsImV4cCI6MTUwNDM2MTIxOCwiaXNzIjoiQjY4Mzg1SDk1QSJ9.qsRFSK800LfDU1AZLLuuu0lD7uyW5mLnkDX2IPZyv9l5Mz4dLPz-1t4u6PTt1fxaUUOznBnktzeIUfk93TG-SA";
            getToken(); //"BQBERpBRRUeuZ4tjxtRBq__FrTpEaecFUmTCd9gwgvwmcGqie5SVMum-RQRATj5FMlsyeg5WuWj6W7qkUnjFwQ"; //getToken();//"BQD5PgY20-9WFB0xWoKAKF8Lip7z_it6HG__w0lxzdRaS8NGhtx-AfGhumYKK3sO5Zn3tEBjcBqWxxFRlum7bA"; //+ token;

            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Authorization", basicAuth);

            this.responseCode = httpCon.getResponseCode();
            System.out.println("RESPONSE CODE LINE 94: " + this.responseCode);
            if (this.responseCode != 200) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                //alert.setHeaderText("Alberto Vecchi");
                alert.setContentText("An error has occured. Rate limit probably reached");

                alert.showAndWait();

                return null;

            }
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);


            BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
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
