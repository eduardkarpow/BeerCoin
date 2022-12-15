package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;

import javax.swing.tree.TreeNode;
import java.security.*;

public class Transaction {
    private byte[] randBytes;
    private byte[] prevBlock;
    private String sender;
    private String receiver;
    private int value;
    private int toStorage;
    private byte[] currHash;
    private byte[] signature;

    public Transaction(byte[] randBytes, byte[] prevBlock, String sender, String receiver, int value){
        this.randBytes = randBytes;
        this.prevBlock = prevBlock;
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
    }
    public void setToStorage(int toStorage){
        this.toStorage = toStorage;
    }
    public void setCurrHash(byte[] currHash){
        this.currHash = currHash;
    }
    public byte[] hash(){
        return Hashing.sha256().hashBytes(Bytes.concat(
                this.randBytes,
                this.prevBlock,
                sender.getBytes(),
                receiver.getBytes(),
                new byte[]{
                        (byte)value
                },
                new byte[]{
                        (byte)toStorage
                }
            )
        ).asBytes();
    }
    public byte[] getSign(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException,SignatureException {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        SecureRandom secureRandom = new SecureRandom();
        signature.initSign(privateKey, secureRandom);
        signature.update(data);
        return signature.sign();

    }

    public int getValue() {
        return value;
    }

    public int getToStorage() {
        return toStorage;
    }

    public String getSender() {
        return sender;
    }
}
