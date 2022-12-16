package BeerCoin.crypto.Entities;

import BeerCoin.crypto.Services.BlockChainService.User;
import jakarta.persistence.*;

import java.security.NoSuchAlgorithmException;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String login;
    @Column(columnDefinition = "TEXT")
    private String publicKey;

    public UserEntity() {
    }
    public UserEntity(String login) throws NoSuchAlgorithmException {
        this.login = login;
    }
    public UserEntity(String login, String publicKey) {
        this.login = login;
        this.publicKey = publicKey;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    @Override
    public String toString(){
        return login + " " + publicKey;
    }
}
