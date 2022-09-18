package Util;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TokenHandler {
    private String token;
    private static String secret_key;
    //default CONSTRUCTOR:

    public TokenHandler(){
    }

    //METHOD TO READ THE SECRET KEY FROM A FILE
    private static String getSecret_key(){
        try {
            File file = new File("src/main/resources/KEY");
            Scanner scanner = null;
            scanner = new Scanner(file);
            secret_key = scanner.next();
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong while reading getting the secretkey");
            System.out.println(e);
        }
        return secret_key;
    }

    //METHOD TO ENCODE THE USERNAME into JWT
    //Will be called when a user logs in or creates a new account
    public String encoder(String username){
        //Initialize all the objects needed to encode a token.
        Signer signer = HMACSigner.newSHA256Signer(getSecret_key());
        JWT jwt = new JWT().setSubject(username);
        //Encode the jwt into a String object containing the key and return it.
        return JWT.getEncoder().encode(jwt, signer);
    }

    //METHOD TO DECODE THE JWT AND VERIFY
    public String verify(String token){
        if (token == null){
            return null;
        }
        Verifier verifier = HMACVerifier.newVerifier(getSecret_key());
        try {
            JWT jwt = JWT.getDecoder().decode(token, verifier);
            return jwt.subject;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}