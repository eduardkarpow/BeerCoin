package BeerCoin.crypto.Entities;

import BeerCoin.crypto.Services.BlockChainService.Block;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Block block;

    public BlockEntity() {
    }
    public BlockEntity(Block block) {
        this.setBlock(block);
    }

    public BlockEntity(int id, Block block) {
        this.setId(id);
        this.setBlock(block);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='"+block+
        "}";
    }
}
