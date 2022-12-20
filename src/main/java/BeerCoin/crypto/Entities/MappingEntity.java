package BeerCoin.crypto.Entities;

import jakarta.persistence.*;

@Entity
public class MappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String key;
    private int value;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private BlockEntity block;
    @ManyToOne
    @JoinColumn(name="block_id",referencedColumnName = "id")
    public BlockEntity getBlock() {
        return block;
    }

    public void setBlock(BlockEntity block) {
        this.block = block;
    }


    public MappingEntity(int id,String key, int value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public MappingEntity(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MappingEntity{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
