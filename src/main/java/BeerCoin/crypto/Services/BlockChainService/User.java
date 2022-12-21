package BeerCoin.crypto.Services.BlockChainService;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class User {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public User() throws NoSuchAlgorithmException {
        generateKeys();
    }
    public User(String publicKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        authenticateUser(publicKey, privateKey);
    }
    private void authenticateUser(String publicKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.decodeBase64(publicKey);
        byte[] privateBytes = Base64.decodeBase64(privateKey);
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(publicBytes);
        PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(keySpecPublic);
        this.privateKey = keyFactory.generatePrivate(keySpecPrivate);
    }
    private void generateKeys()throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        this.privateKey = kp.getPrivate();
        this.publicKey = kp.getPublic();
    }
    public String Private(){
        return HashingService.encode(privateKey.getEncoded());
    }
    public PrivateKey getPrivateKey(){return this.privateKey;}
    public String Public(){
        return HashingService.encode(publicKey.getEncoded());
    }
    public String getAdress(){
        return Public();
    }
}
