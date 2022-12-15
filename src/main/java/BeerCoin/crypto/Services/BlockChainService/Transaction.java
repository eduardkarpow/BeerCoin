package BeerCoin.crypto.Services.BlockChainService;

import javax.swing.tree.TreeNode;

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

}
