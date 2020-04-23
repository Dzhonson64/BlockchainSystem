package blockchain;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class Chain implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String prevHash;
    private String currentHash;
    private long timeStamp;
    private int magicNumber;


    public Chain(int id) {
        timeStamp = new Date().getTime();
        this.currentHash = getNewHash();
        this.id = id;
        prevHash = "0";

    }
    public Chain(int id, String prevHash) {
        timeStamp = new Date().getTime();
        this.currentHash = getNewHash();
        this.id = id;
        this.prevHash = prevHash;

    }

    public String getCurrentHash() {
        return currentHash;
    }
    public String getPrevHash() {
        return prevHash;
    }

    @Override
    public String toString() {
        return "Block:" +
                "\nId: " + id +
                "\nTimestamp: " + timeStamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block:\n" +
                 prevHash +
                "\nHash of the block:\n" +
                currentHash + "\n";
    }

    private String getNewHash(){
        magicNumber = createMagicNumber();
        String currentHash = StringUtil.applySha256(id + timeStamp + getPrevHash() + magicNumber);
        while(!currentHash.startsWith("0".repeat(BlockchainSystem.ZEROS_COUNT))){
            magicNumber = createMagicNumber();
            currentHash = StringUtil.applySha256(id + timeStamp + getPrevHash() + magicNumber);
        }
        if (currentHash.length() > 64 - BlockchainSystem.ZEROS_COUNT){
            currentHash = currentHash.substring(0, 64 - BlockchainSystem.ZEROS_COUNT);
        }
        return currentHash;
    }

    public static int createMagicNumber() {
        Random rnd = new Random();
        return  rnd.nextInt((int)10e6) + (int)10e7;

    }

}
