package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import info.debatty.java.lsh.LSH;
import info.debatty.java.lsh.LSHMinHash;

import java.math.MathContext;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

abstract class HashingService {
    public static byte[] Sign(PrivateKey privateKey, byte[] data) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        SecureRandom secureRandom = new SecureRandom();
        signature.initSign(privateKey, secureRandom);
        signature.update(data);
        return signature.sign();
    }
    public static boolean SignIsValid(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256WithRSA");
        sign.initVerify(publicKey);
        sign.update(data);
        return sign.verify(signature);
    }
    public static PublicKey getEncodedPublicKey(String address) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] b1 = address.getBytes();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b1);
        return keyFactory.generatePublic(publicKeySpec);
    }
    public static byte[] hashData(byte[] data){
        return Hashing.sha256().hashBytes(data).asBytes();
    }
    public static long proofOfWork(byte[] blockHash){
        long target = 1;
        long intHash = 1;
        long nonce = Math.round(Math.random()*Long.MAX_VALUE);
        byte[] hash = new byte[0];
        target = target << (256- BlockChainConstants.DIFFICULTY);
        while (nonce < Long.MAX_VALUE){
            hash = HashingService.hashData(Bytes.concat(
                    blockHash,
                    new byte[] {
                            (byte)nonce
                    }
            ));
            intHash = ByteBuffer.wrap(hash).getLong();
            if(intHash<target){
                return nonce;
            }
            nonce++;
        }
        return nonce;
    }
}
