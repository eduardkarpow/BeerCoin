package BeerCoin.crypto.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String key;
    private int value;

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
