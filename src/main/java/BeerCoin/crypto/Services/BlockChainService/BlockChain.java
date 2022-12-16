package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import BeerCoin.crypto.Entities.BlockEntity;
import BeerCoin.crypto.Repositories.BlockRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

public class BlockChain {
    private BlockChain instance;
    @Autowired
    private BlockRepository blockRepository;
    private BlockChain(){
        this.instance = new BlockChain();
    }
    public static void newChain(String fileName,String receiver){


        Block genesis = new Block(BlockChainConstants.GENESIS_BLOCK.getBytes(),receiver);
        genesis.getMapping().put(BlockChainConstants.STORAGE_CHAIN, BlockChainConstants.STORAGE_VALUE);
        genesis.getMapping().put(receiver, BlockChainConstants.GENESIS_REWARD);
        genesis.setCurrHash(Hashing.sha256().hashInt(genesis.hashCode()).asBytes());
    }
    public void addBlock(Block block){
        blockRepository.save(new BlockEntity(block));
    }
    public Block newBlock(String miner, byte[] prevHash){
        return new Block(prevHash, miner, BlockChainConstants.DIFFICULTY);
    }
    public int balance(String address){
        int balance;
        List<BlockEntity> blocks = blockRepository.findAll();
        for(int i = blocks.size()-1; i >=0; i--){
            if(blocks.get(i).getBlock().getMapping().containsKey(address)){
                return blocks.get(i).getBlock().getMapping().get(address);
            }
        }
        return 0;
    }
    public byte[] lastHash(){
        List<BlockEntity> blocks = blockRepository.findAll();
        return blocks.get(blocks.size()).getBlock().getCurrHash();
    }
}
