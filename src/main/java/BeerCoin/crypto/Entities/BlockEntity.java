package BeerCoin.crypto.Entities;

import BeerCoin.crypto.Services.BlockChainService.Block;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="block_entity")
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long nonce;
    private int diffivulty;
    @Column(columnDefinition = "TEXT")
    private String currHash;
    @Column(columnDefinition = "TEXT")
    private String prevHash;

    @Column(columnDefinition = "TEXT")
    private String miner;
    @Column(columnDefinition = "TEXT")
    private String signature;
    private String timeStamp;
    @OneToMany(mappedBy = "blockEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MappingEntity> mapping = new HashSet<MappingEntity>();

    public Set<MappingEntity> getMapping() {
        return mapping;
    }

    public void setMapping(Set<MappingEntity> mapping) {
        this.mapping = mapping;
    }
    public void addMapping(MappingEntity mapping){
        mapping.setBlock(this);
        this.mapping.add(mapping);
    }
    @OneToMany(mappedBy = "blockEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TransactionEntity> transactions = new HashSet<>();

    public BlockEntity() {
    }
    public BlockEntity(Block block) {
        this.nonce = block.getNonce();
        this.diffivulty = block.getDifficulty();
        this.currHash = block.getCurrHash() == null ? null :  java.util.Base64.getEncoder().encodeToString(block.getCurrHash());
        this.prevHash = block.getPrevHash() == null ? null : java.util.Base64.getEncoder().encodeToString(block.getPrevHash());
        this.signature = block.getSignature() == null ? null : java.util.Base64.getEncoder().encodeToString(block.getSignature());
        this.timeStamp = block.getTimeStamp();
    }

    public BlockEntity(int id) {
        this.setId(id);
    }
    public Set<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
    public void addTransaction(TransactionEntity transaction){
        transaction.setBlock(this);
        this.transactions.add(transaction);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Block getBlock(){
        return new Block(this);
    }

    public long getNonce() {
        return nonce;
    }


    public String getCurrHash() {
        return currHash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getMiner() {
        return miner;
    }

    public String getSignature() {
        return signature;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id;
    }
}
