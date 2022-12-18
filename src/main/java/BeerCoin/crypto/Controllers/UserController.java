package BeerCoin.crypto.Controllers;

import BeerCoin.crypto.Entities.UserEntity;
import BeerCoin.crypto.Repositories.UserRepository;
import BeerCoin.crypto.Services.BlockChainService.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private User currentUser;
    public UserController(){

    }
    @PostMapping("users/register")
    UserEntity register(@RequestBody UserEntity user) throws NoSuchAlgorithmException {
        currentUser = new User();
        return userRepository.save(new UserEntity(user.getLogin(), currentUser.Public(), currentUser.Private()));
    }
    @PostMapping("users/login")
    UserEntity login(@RequestBody UserEntity user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(userRepository.findByLogin(user.getLogin()).get(0).getPublicKey().equals(user.getPublicKey())){
            currentUser = new User(user.getPublicKey(), user.getPrivateKey());
        }
        return user;
    }
    @GetMapping("users/getAll")
    List<UserEntity> getAll(){
        return userRepository.findAll();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e){
        System.out.println(e);
    }
}
