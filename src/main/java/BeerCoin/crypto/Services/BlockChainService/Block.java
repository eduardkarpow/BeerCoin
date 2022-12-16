package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import BeerCoin.crypto.Exceptions.BlockIsNotValidException;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import org.apache.coyote.Constants;
import org.yaml.snakeyaml.nodes.CollectionNode;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalTime;
import java.util.*;

public class Block {
    private int nonce;
    private int difficulty;
    private byte[] currHash;
    private byte[] prevHash;
    private List<Transaction> transactions;
    private Map<String, Integer> mapping;
    private String miner;
    private byte[] signature;
    private String timeStamp;
    public Block(byte[] prevHash, String miner){
        this.prevHash = prevHash;
        this.miner = miner;
        this.mapping = new HashMap<String, Integer>();
        this.timeStamp = LocalTime.now().toString();
        this.transactions = new ArrayList<Transaction>();
    }
    public Block(byte[] prevHash, String miner, int difficulty){
        this.prevHash = prevHash;
        this.miner = miner;
        this.mapping = new HashMap<String, Integer>();
        this.difficulty = difficulty;
    }
    public Transaction newTransaction(User user, byte[] lastHash,String to, int value) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Transaction ts = new Transaction(generateRandomBytes(),lastHash,user.getAdress(),to, value);
        if(value > BlockChainConstants.START_PERCENT){
            ts.setToStorage(BlockChainConstants.STORAGE_REWARD);
        }
        ts.setCurrHash(ts.hash());
        ts.setSignature(ts.getSign(user.getPrivateKey()));
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
    public void addTransaction(BlockChain chain,Transaction ts){
        int balanceInChain;
        int balanceInTs = ts.getValue()+ts.getToStorage();
        if(mapping.containsKey(ts.getSender())){
            balanceInChain = mapping.get(ts.getSender());
        } else{
            balanceInChain = chain.balance(ts.getSender());

        }
        mapping.put(ts.getSender(), balanceInChain - balanceInTs);
        addBalance(chain,ts.getReceiver(), ts.getValue());
        addBalance(chain, BlockChainConstants.STORAGE_CHAIN, ts.getToStorage());
        transactions.add(ts);

    }
    public void addBalance(BlockChain chain, String receiver, int value){
        int balanceInChain;
        if(mapping.containsKey(receiver)){
            balanceInChain = mapping.get(receiver);
        } else{
            balanceInChain = chain.balance(receiver);
        }
        mapping.put(receiver, balanceInChain+value);
    }
    public boolean balanceIsValid(BlockChain chain,String address){
        if(!mapping.containsKey(address)){
            return false;
        }
        int lenTs = transactions.size();
        int balanceInChain = chain.balance(address);
        int balanceSubBlock = 0;
        int balanceAddBlock = 0;
        for(int j = 0; j < lenTs; j++){
            Transaction ts = transactions.get(j);
            if(ts.getSender().equals(address)){
                balanceSubBlock += ts.getValue()+ts.getToStorage();
            }
            if(ts.getReceiver().equals(address)){
                balanceAddBlock += ts.getValue();
            }
            if(BlockChainConstants.STORAGE_CHAIN.equals(address)){
                balanceAddBlock += ts.getToStorage();
            }
        }
        if(balanceInChain+balanceAddBlock-balanceSubBlock != mapping.get(address)){
            return false;
        }
        return true;
    }
    public byte[] hash(){
        byte[] tempHash = new byte[0];
        for(Transaction ts: transactions){
            tempHash = Bytes.concat(tempHash,ts.getCurrHash());
        }
        List<String> lst = new ArrayList<String>();
        for(String key: mapping.keySet()){
            tempHash = Bytes.concat(tempHash,key.getBytes(),new byte[] {mapping.get(key).byteValue()});
        }
        return HashingService.hashData(Bytes.concat(
                tempHash,
                new byte[]{(byte)BlockChainConstants.DIFFICULTY},
                prevHash,
                miner.getBytes(),
                timeStamp.getBytes()
        ));
    }
    public boolean transactionIsValid(BlockChain chain) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        int lenTs = transactions.size();
        int plusStorage = 0;
        for(int i = 0; i < lenTs; i++){
            if(transactions.get(i).getSender().equals(BlockChainConstants.STORAGE_CHAIN)){
                plusStorage = 1;
                break;
            }
        }
        if(lenTs == 0 || lenTs > BlockChainConstants.TRANSACTIONS_LIMIT+plusStorage){
            return false;
        }
        for(int i = 0; i < lenTs-1; i++){
            for(int j = i; j <lenTs; j++){
                if(Arrays.equals(transactions.get(i).getRandBytes(), transactions.get(j).getRandBytes())){
                    return false;
                }
                if(transactions.get(i).getSender().equals(BlockChainConstants.STORAGE_CHAIN) && transactions.get(j).equals(BlockChainConstants.STORAGE_CHAIN)){
                    return false;
                }
            }
        }
        for(int i = 0; i < lenTs; i++){
            Transaction ts = transactions.get(i);
            if(ts.getSender().equals(BlockChainConstants.STORAGE_CHAIN)){
                if(!ts.getReceiver().equals(miner) || ts.getValue() != BlockChainConstants.STORAGE_REWARD){
                    return false;
                }
            } else{
                if(!ts.hashIsValid()){
                    return false;
                }
                if(!ts.signIsValid(HashingService.getEncodedPublicKey(ts.getSender()))){
                    return false;
                }
            }
            if (!balanceIsValid(chain, ts.getReceiver())){
                return false;
            }
            if(!balanceIsValid(chain, ts.getSender())){
                return false;
            }
        }
        return true;
    }
    public void accept(BlockChain chain, User user) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        if(!transactionIsValid(chain)){
            throw new BlockIsNotValidException();
        }
        Transaction toMinerTransaction = new Transaction(generateRandomBytes(), chain.lastHash() ,BlockChainConstants.STORAGE_CHAIN, user.getAdress(), BlockChainConstants.STORAGE_REWARD);
        addTransaction(chain, toMinerTransaction);
        timeStamp = LocalTime.now().toString();
        currHash = hash();
        signature = HashingService.Sign(user.getPrivateKey(), currHash);

    }

    public byte[] getCurrHash() {
        return currHash;
    }
}
