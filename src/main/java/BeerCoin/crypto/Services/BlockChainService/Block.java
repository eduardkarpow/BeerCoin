package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import org.apache.coyote.Constants;
import org.yaml.snakeyaml.nodes.CollectionNode;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Block {
    private int nonce;
    private int difficulty;
    private byte[] currHash;
    private byte[] prevHash;
    private Transaction[] transactions;
    private Map<String, Integer> mapping;
    private String miner;
    private byte[] signature;
    private String timeStamp;
    public Block(byte[] prevHash, String miner){
        this.prevHash = prevHash;
        this.miner = miner;
        this.mapping = new HashMap<String, Integer>();
        this.timeStamp = LocalTime.now().toString();
    }
    public Block(byte[] prevHash, String miner, int difficulty){
        this.prevHash = prevHash;
        this.miner = miner;
        this.mapping = new HashMap<String, Integer>();
        this.difficulty = difficulty;
    }
    public Transaction newTransaction(User user, byte[] lastHash,String to, int value){
        Transaction ts = new Transaction(generateRandomBytes(),lastHash,user.getAdress(),to, value);

        return ts;
    }
    private byte[] generateRandomBytes(){
        Random rnd = new Random();
        byte[] arr = new byte[BlockChainConstants.RAND_BYTES];
        rnd.nextBytes(arr);
        return arr;
    }
    public Map<String, Integer> getMapping(){
        return this.mapping;
    }
    public void setCurrHash(byte[] currHash){
        this.currHash = currHash;
    }
    public String toString(){
        return String.valueOf(this.nonce)+String.valueOf(transactions)+String.valueOf(mapping)+miner+String.valueOf(signature)+timeStamp;
    }
}
