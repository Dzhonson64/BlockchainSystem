package blockchain;

import java.io.Serializable;
import java.util.*;

public class Chain implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String prevHash;
    private final String currentHash;
    private long timeStamp = 0;
    private int magicNumber;
    private double generationTime = 0.0;
    private int minerId;


    public Chain() {
        long start = System.nanoTime();
        this.timeStamp = new Date().getTime();
        this.id = BlockchainSystem.COUNT_CHAIN;
        this.prevHash = BlockchainSystem.LAST_CHAIN == null ? "0" :  BlockchainSystem.LAST_CHAIN.getCurrentHash();
        this.currentHash = getNewHash();
        generationTime = (System.nanoTime() - start)/1000000000.0;
    }

    public Chain(String defaultHash){
        this.currentHash = defaultHash;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    @Override
    public synchronized String toString() {
        return  "Block:" +
                "\nCreated by miner # " + minerId/*"*/ +
                "\nId: " + id +
                "\nTimestamp: " + timeStamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block:\n" +
                 prevHash +
                "\nHash of the block:\n" +
                currentHash  +
                "\nBlock was generating for " + generationTime + " seconds";
    }

    private String getNewHash(){
        magicNumber = createMagicNumber();
        return createHash();
    }

    private int createMagicNumber() {
        Random rnd = new Random();
        int magicNumber =  rnd.nextInt((int)10e6) + (int)10e7;
        String currentHash = StringUtil.applySha256(Integer.toString(id) + timeStamp + magicNumber);
        while(!currentHash.startsWith("0".repeat(BlockchainSystem.ZEROS_COUNT))){
            magicNumber = rnd.nextInt((int)10e6) + (int)10e7;
            currentHash = StringUtil.applySha256(Integer.toString(id) + timeStamp  + magicNumber);
        }
        return magicNumber;
    }

    public double getGenerationTime() {
        return generationTime;
    }

    private String createHash(){
        return StringUtil.applySha256(Integer.toString(id) + timeStamp  + magicNumber);
    }

    private String createHash(int magicNumber){
        return StringUtil.applySha256(Integer.toString(id) + timeStamp  + magicNumber);
    }

    public boolean validate(String previousHash) {
        if (previousHash == null || !previousHash.equals(getPrevHash())) {
                return false;
        }
        return createHash().equals(getCurrentHash());
    }

    public void setMinerId(int minerId) {
        this.minerId = minerId;
    }

}
