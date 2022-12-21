package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.BlockChainConstants;
import BeerCoin.crypto.Entities.BlockEntity;
import BeerCoin.crypto.Entities.MappingEntity;
import BeerCoin.crypto.Repositories.BlockRepository;
import BeerCoin.crypto.Repositories.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

public class BlockChain {
    private BlockChain instance;
    @Autowired
    private BlockRepository blockRepository;
    private BlockChain(){
        this.instance = new BlockChain();
    }
    public static void newChain(BlockRepository blockRepository, String receiver){

        if(blockRepository.findAll() != null){
            return;
        }
        Block genesis = new Block(BlockChainConstants.GENESIS_BLOCK.getBytes(),receiver);
        MappingEntity senderMap = new MappingEntity(BlockChainConstants.STORAGE_CHAIN,BlockChainConstants.STORAGE_VALUE);
        MappingEntity receiverMap = new MappingEntity(receiver, BlockChainConstants.GENESIS_REWARD);
        genesis.setCurrHash(Hashing.sha256().hashInt(genesis.hashCode()).asBytes());
        BlockEntity blockEntity = new BlockEntity(genesis);
        blockEntity.addMapping(senderMap);
        blockEntity.addMapping(receiverMap);
        blockRepository.save(blockEntity);
    }
    public void addBlock(BlockRepository blockRepository,Block block){
        blockRepository.save(new BlockEntity(block));
    }
    public Block newBlock(String miner, byte[] prevHash){
        return new Block(prevHash, miner, BlockChainConstants.DIFFICULTY);
    }
    public int balance(String address){
        int balance;
        List<BlockEntity> blocks = blockRepository.findAll();
        for(int i = blocks.size()-1; i >=0; i--){
            for(MappingEntity mappingEntity : blocks.get(i).getMapping()) {
                if (mappingEntity.getKey().equals(address)) {
                    return mappingEntity.getValue();
                }
            }
        }
        return 0;
    }
    public byte[] lastHash(){
        List<BlockEntity> blocks = blockRepository.findAll();
        return blocks.get(blocks.size()).getBlock().getCurrHash();
    }
}
