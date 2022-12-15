package BeerCoin.crypto.Services.BlockChainService;

import java.security.*;

public class User {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private void generateKeys()throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        this.privateKey = kp.getPrivate();
        this.publicKey = kp.getPublic();
    }
    public String Private(){
        return privateKey.toString();
    }
    public String Public(){
        return publicKey.toString();
    }
    public String getAdress(){
        return Public();
    }
}
