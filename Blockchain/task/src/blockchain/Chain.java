package blockchain;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Chain implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String prevHash;
    private String currentHash;
    private long timeStamp;
    private int magicNumber;


    public Chain(int countZeros, String currentHash, int id, int numMagic) {
        timeStamp = new Date().getTime();
        this.magicNumber =  numMagic;
        this.currentHash = getNewHash(currentHash, countZeros);
        this.id = id;
        prevHash = "0";

    }
    public Chain(int countZeros, String currentHash, int id, String prevHash, int numMagic) {
        timeStamp = new Date().getTime();
        this.magicNumber = numMagic;
        this.currentHash = getNewHash(currentHash, countZeros);
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

    private String getNewHash(String currentHash, int countZeros){
        StringBuilder zeros = new StringBuilder();
        for (int i = 0; i < countZeros; i++){
            zeros.append("0");
        }
        if (currentHash.length() > 64-countZeros){
            currentHash = currentHash.substring(0, 64-countZeros);
        }
        return zeros + currentHash;
    }

}
