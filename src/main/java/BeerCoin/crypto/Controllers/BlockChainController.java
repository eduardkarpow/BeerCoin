package BeerCoin.crypto.Controllers;

import BeerCoin.crypto.Entities.BlockEntity;
import BeerCoin.crypto.Entities.TransactionEntity;
import BeerCoin.crypto.Repositories.BlockRepository;
import BeerCoin.crypto.Repositories.TransactionRepository;
import BeerCoin.crypto.Services.BlockChainService.Block;
import BeerCoin.crypto.Services.BlockChainService.BlockChain;
import BeerCoin.crypto.Services.BlockChainService.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class BlockChainController {
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    private BlockChain chain;
    BlockChainController() {
    }
    @GetMapping("block/init")
    BlockEntity init(){
        BlockChain.newChain(blockRepository,UserController.getCurrentUser().Public());
        return new BlockEntity();
    }
    @GetMapping("transaction/test/add")
    BlockEntity add(){
        byte[] bt = {(byte) 12, (byte)13};
        TransactionEntity transactionEntity = new TransactionEntity(new Transaction(bt,bt,"user1","user2", 20));
        transactionRepository.save(transactionEntity);
        BlockEntity blockEntity = new BlockEntity(new Block(bt, "user1"));
        blockEntity.addTransaction(transactionEntity);
        return blockEntity;
    }
    @ExceptionHandler
    void handle(Exception e){
        System.out.println(e);
    }
}
