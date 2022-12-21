package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;

import javax.swing.tree.TreeNode;
import java.security.*;
import java.util.Arrays;

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
        return HashingService.hashData(Bytes.concat(
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
        );
    }
    public byte[] getSign(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException,SignatureException {
        return HashingService.Sign(privateKey, currHash);

    }
    public boolean hashIsValid(){
        return Arrays.equals(currHash,this.hash());
    }
    public boolean signIsValid(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return HashingService.SignIsValid(publicKey, currHash, signature);
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

    public String getReceiver() {
        return receiver;
    }

    public byte[] getRandBytes() {
        return randBytes;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getCurrHash() {
        return currHash;
    }

    public byte[] getPrevBlock() {
        return prevBlock;
    }

    public byte[] getSignature() {
        return signature;
    }
}
