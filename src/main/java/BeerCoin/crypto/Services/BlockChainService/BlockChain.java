package BeerCoin.crypto.Services.BlockChainService;

import BeerCoin.crypto.Constants.Constants;
import com.google.common.hash.Hashing;

public class BlockChain {
    private BlockChain instance;
    private BlockChain(){
        this.instance = new BlockChain();
    }
    public static void newChain(String fileName,String receiver){


        Block genesis = new Block(Constants.GENESIS_BLOCK.getBytes(),receiver);
        genesis.getMapping().put(Constants.STORAGE_CHAIN, Constants.STORAGE_VALUE);
        genesis.getMapping().put(receiver, Constants.GENESIS_REWARD);
        genesis.setCurrHash(Hashing.sha256().hashInt(genesis.hashCode()).asBytes());
    }
}
