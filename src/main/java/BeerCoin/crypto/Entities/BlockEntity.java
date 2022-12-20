package BeerCoin.crypto.Entities;

import BeerCoin.crypto.Services.BlockChainService.Block;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "block",schema = "", catalog = "block")
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //private Block block;
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




    private Set<MappingEntity> mapping = new HashSet<MappingEntity>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "block")
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
    private Set<TransactionEntity> transactions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "block")
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

    public BlockEntity() {
    }
    public BlockEntity(Block block) {
        this.nonce = block.getNonce();
        this.diffivulty = block.getDifficulty();
        this.currHash = block.getCurrHash().toString();
        this.prevHash = block.getPrevHash().toString();
        this.signature = block.getSignature().toString();
        this.timeStamp = block.getTimeStamp();
    }

    public BlockEntity(int id, Block block) {
        this.setId(id);
        //this.setBlock(block);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Block getBlock() {
        return new Block();
    }

    public void setBlock(Block block) {
        //this.block = block;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id;
    }
}
