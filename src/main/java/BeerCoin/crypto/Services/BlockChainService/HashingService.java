package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import info.debatty.java.lsh.LSH;
import info.debatty.java.lsh.LSHMinHash;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.MathContext;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

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

    public static PrivateKey getEncodedPrivateKey(String address) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] b1 = address.getBytes();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(b1);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    public static byte[] hashData(byte[] data) {
        return Hashing.sha256().hashBytes(data).asBytes();
    }

    public static long proofOfWork(byte[] blockHash) {
        long target = 1;
        long intHash = 1;
        long nonce = Math.round(Math.random() * Long.MAX_VALUE);
        byte[] hash = new byte[0];
        target = target << (256 - BlockChainConstants.DIFFICULTY);
        while (nonce < Long.MAX_VALUE) {
            hash = HashingService.hashData(Bytes.concat(
                    blockHash,
                    new byte[]{
                            (byte) nonce
                    }
            ));
            intHash = ByteBuffer.wrap(hash).getLong();
            if (intHash < target) {
                return nonce;
            }
            nonce++;
        }
        return nonce;
    }

    public static byte[] generateRandomBytes() {
        Random rnd = new Random();
        byte[] arr = new byte[BlockChainConstants.RAND_BYTES];
        rnd.nextBytes(arr);
        return arr;
    }

    public static String encode(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }
    public static byte[] decode(String data){
        return Base64.decodeBase64(data);
    }
}
