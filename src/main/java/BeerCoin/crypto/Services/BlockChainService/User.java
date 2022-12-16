package BeerCoin.crypto.Services.BlockChainService;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class User {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public User() throws NoSuchAlgorithmException {
        generateKeys();
    }
    public User(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        authenticateUser(publicKey);
    }
    private void authenticateUser(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println(0);
        byte[] publicBytes = Base64.decodeBase64(publicKey);
        System.out.println(1);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        System.out.println(2);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        System.out.println(3);
        this.publicKey = keyFactory.generatePublic(keySpec);
        this.privateKey = keyFactory.generatePrivate(keySpec);
    }
    private void generateKeys()throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        this.privateKey = kp.getPrivate();
        this.publicKey = kp.getPublic();
    }
    public String Private(){
        return privateKey.getEncoded().toString();
    }
    public PrivateKey getPrivateKey(){return this.privateKey;}
    public String Public(){
        System.out.println(privateKey.toString());
        return publicKey.toString();
    }
    public String getAdress(){
        return Public();
    }
}
