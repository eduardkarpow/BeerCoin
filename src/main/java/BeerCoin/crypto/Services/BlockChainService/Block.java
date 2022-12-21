package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import BeerCoin.crypto.Entities.BlockEntity;
import BeerCoin.crypto.Entities.MappingEntity;
import BeerCoin.crypto.Entities.TransactionEntity;
import BeerCoin.crypto.Exceptions.BlockIsNotValidException;
import BeerCoin.crypto.Repositories.MappingRepository;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import org.apache.coyote.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.nodes.CollectionNode;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalTime;
import java.util.*;

@Service
public class Block {
    private long nonce;
    private int difficulty = BlockChainConstants.DIFFICULTY;
    private byte[] currHash;
    private byte[] prevHash;
    private Set<TransactionEntity> transactions;
    private Set<MappingEntity> mapping;
    private String miner;
    private byte[] signature;
    private String timeStamp;
    public Block(){

    }
    public Block(BlockEntity blockEntity){
        this.nonce = blockEntity.getNonce();
        this.currHash = HashingService.decode(blockEntity.getCurrHash());
        this.prevHash = HashingService.decode(blockEntity.getPrevHash());
        this.transactions = blockEntity.getTransactions();
        this.mapping = blockEntity.getMapping();
        this.miner = blockEntity.getMiner();
        this.signature = HashingService.decode(blockEntity.getSignature());
        this.timeStamp = blockEntity.getTimeStamp();
    }
    public Block(byte[] prevHash, String miner){
        this.prevHash = prevHash;
        this.miner = miner;
        this.mapping = new HashSet<>();
        this.timeStamp = LocalTime.now().toString();
        this.transactions = new HashSet<>();
    }
    public Block(byte[] prevHash, String miner, int difficulty){
        this.prevHash = prevHash;
        this.miner = miner;
        this.mapping = new HashSet<>();
        this.difficulty = difficulty;
    }
    public Transaction newTransaction(User user, byte[] lastHash,String to, int value) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Transaction ts = new Transaction(HashingService.generateRandomBytes(),lastHash,user.getAdress(),to, value);
        if(value > BlockChainConstants.START_PERCENT){
            ts.setToStorage(BlockChainConstants.STORAGE_REWARD);
        }
        ts.setCurrHash(ts.hash());
        ts.setSignature(ts.getSign(user.getPrivateKey()));
        return ts;
    }
    public int containsKey(Set<MappingEntity> set, String key){
        for(MappingEntity mEntity : set){
            if(mEntity.getKey().equals(key)){
                return mEntity.getValue();
            }
        }
        return -1;
    }

    public void addTransaction(BlockChain chain, MappingRepository mappingRepository,Transaction ts, BlockEntity blockEntity){
        int balanceInChain;
        int balanceInTs = ts.getValue()+ts.getToStorage();
        int value = containsKey(blockEntity.getMapping(),ts.getSender());
        if(value != -1){
            balanceInChain = value;
        } else{
            balanceInChain = chain.balance(ts.getSender());

        }
        MappingEntity mappingEntity = new MappingEntity(ts.getSender(), balanceInChain - balanceInTs);
        blockEntity.addMapping(mappingEntity);
        mapping.add(mappingEntity);
        addBalance(mappingRepository,chain,ts.getReceiver(), ts.getValue());
        addBalance(mappingRepository,chain, BlockChainConstants.STORAGE_CHAIN, ts.getToStorage());
        transactions.add(ts);

    }
    public void addBalance(BlockEntity blockEntity,MappingRepository mappingRepository,BlockChain chain, String receiver, int value){
        int balanceInChain;
        MappingEntity mappingEntity;
        int value1 = containsKey(mapping,receiver);
        if(value1 != -1){
            balanceInChain = value1;
            mappingEntity = new MappingEntity(receiver, balanceInChain+value1);
            blockEntity.addMapping(mappingEntity);
            mappingRepository.save(mappingEntity);
        } else{
            balanceInChain = chain.balance(receiver);
            mappingEntity = new MappingEntity(receiver, balanceInChain+value);
            blockEntity.addMapping(mappingEntity);
            mappingRepository.save(mappingEntity);
            mapping.add(mappingEntity);
        }

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
    public long proof(){
        return HashingService.proofOfWork(currHash);
    }
    public void accept(BlockChain chain, User user) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        if(!transactionIsValid(chain)){
            throw new BlockIsNotValidException();
        }
        Transaction toMinerTransaction = new Transaction(HashingService.generateRandomBytes(), chain.lastHash() ,BlockChainConstants.STORAGE_CHAIN, user.getAdress(), BlockChainConstants.STORAGE_REWARD);
        addTransaction(chain, toMinerTransaction);
        timeStamp = LocalTime.now().toString();
        currHash = hash();
        signature = HashingService.Sign(user.getPrivateKey(), currHash);
        nonce = proof();
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
    public byte[] getCurrHash() {
        return currHash;
    }

    public long getNonce() {
        return nonce;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getMiner() {
        return miner;
    }

    public byte[] getSignature() {
        return signature;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
