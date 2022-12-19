package BeerCoin.crypto.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String randBytes;
    private String prevBlock;
    private UserEntity sender;
    private UserEntity receiver;
    private int value;
    private int toStorage;
    private String currHash;
    private String signature;

    public TransactionEntity(int id, String randBytes, String prevBlock, UserEntity sender, UserEntity receiver, int value, int toStorage, String currHash, String signature) {
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

    public TransactionEntity(String randBytes, String prevBlock, UserEntity sender, UserEntity receiver, int value, int toStorage, String currHash, String signature) {
        this.randBytes = randBytes;
        this.prevBlock = prevBlock;
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.toStorage = toStorage;
        this.currHash = currHash;
        this.signature = signature;
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

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
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
