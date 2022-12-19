package BeerCoin.crypto.Controllers;

import BeerCoin.crypto.Entities.BlockEntity;
import BeerCoin.crypto.Repositories.BlockRepository;
import BeerCoin.crypto.Services.BlockChainService.BlockChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class BlockChainController {
    @Autowired
    private BlockRepository blockRepository;
    private BlockChain chain;
    BlockChainController() {
    }
    @GetMapping("block/init")
    BlockEntity init(){
        return new BlockEntity();
    }
}
