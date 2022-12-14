package BeerCoin.crypto.Entities;


import BeerCoin.crypto.Services.BlockChainService.Transaction;
import jakarta.persistence.*;

@Entity
@Table(name = "transaction_entity")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String randBytes;
    @Column(columnDefinition = "TEXT")
    private String prevBlock;
    @Column(columnDefinition = "TEXT")
    private String sender;
    @Column(columnDefinition = "TEXT")
    private String receiver;
    private int value;
    private int toStorage;
    @Column(columnDefinition = "TEXT")
    private String currHash;
    @Column(columnDefinition = "TEXT")
    private String signature;


    @ManyToOne
    @JoinColumn(name = "block_entity_id")
    private BlockEntity blockEntity;

    public BlockEntity getBlock(){
        return this.blockEntity;
    }
    public void setBlock(BlockEntity block){
        this.blockEntity = block;
    }

    public TransactionEntity(int id, String randBytes, String prevBlock, String sender, String receiver, int value, int toStorage, String currHash, String signature) {
        this.id = id;
        this.randBytes = randBytes;
        this.prevBlock = prevBlock;
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.toStorage = toStorage;
        this.currHash = currHash;
        this.signature = signature;
    }

    public TransactionEntity(Transaction transaction) {
        this.randBytes = transaction.getRandBytes() == null ? null : transaction.getRandBytes().toString();
        this.prevBlock = transaction.getPrevBlock() == null ? null : transaction.getPrevBlock().toString();
        this.sender = transaction.getSender();
        this.receiver = transaction.getReceiver();
        this.value = transaction.getValue();
        this.toStorage = transaction.getToStorage();
        this.currHash = transaction.getCurrHash() == null ? null : transaction.getCurrHash().toString();
        this.signature = transaction.getSignature() == null ? null : transaction.getSignature().toString();
    }

    public TransactionEntity() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRandBytes() {
        return randBytes;
    }

    public void setRandBytes(String randBytes) {
        this.randBytes = randBytes;
    }

    public String getPrevBlock() {
        return prevBlock;
    }

    public void setPrevBlock(String prevBlock) {
        this.prevBlock = prevBlock;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getToStorage() {
        return toStorage;
    }

    public void setToStorage(int toStorage) {
        this.toStorage = toStorage;
    }

    public String getCurrHash() {
        return currHash;
    }

    public void setCurrHash(String currHash) {
        this.currHash = currHash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", randBytes='" + randBytes + '\'' +
                ", prevBlock='" + prevBlock + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", value=" + value +
                ", toStorage=" + toStorage +
                ", currHash='" + currHash + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
