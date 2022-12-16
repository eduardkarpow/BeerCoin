package BeerCoin.crypto.Exceptions;

public class BlockIsNotValidException extends RuntimeException{
    public BlockIsNotValidException(){
        super("Block Isn't valid");
    }
}
