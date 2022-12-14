package BeerCoin.crypto.Services.BlockChainService;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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
