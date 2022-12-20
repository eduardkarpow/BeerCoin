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
        this.setBlockId(id);
        //this.setBlock(block);
    }

    public int getBlockId() {
        return id;
    }

    public void setBlockId(int id) {
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
