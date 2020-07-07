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


    private List<Message> messages = null;


    public Chain() {
        long start = System.nanoTime();
        this.timeStamp = new Date().getTime();
        this.id = BlockchainSystem.COUNT_CHAIN;
        this.prevHash = BlockchainSystem.LAST_CHAIN == null ? "0" :  BlockchainSystem.LAST_CHAIN.getCurrentHash();
        this.currentHash = getNewHash();
        generationTime = (System.nanoTime() - start)/1000000000.0;
    }

    public Chain(int id, long timeStamp, int magicNumber, int idMiner, double generationTime) {
        long start = System.nanoTime();
        this.timeStamp = timeStamp;
        this.magicNumber = magicNumber;
        this.id = id;
        this.minerId = idMiner;
        this.prevHash = BlockchainSystem.LAST_CHAIN == null ? "0" :  BlockchainSystem.LAST_CHAIN.getCurrentHash();
        this.currentHash = createHash(magicNumber);
        this.generationTime = generationTime/1000000000.0;
        messages = new ArrayList<>();
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
        return  "\nBlock:" +
                "\nCreated by miner # " + minerId/*"*/ +
                "\nId: " + id +
                "\nTimestamp: " + timeStamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block:\n" +
                prevHash +
                "\nHash of the block:\n" +
                currentHash  +
                "\nBlock data:" +
                getMessagesString() +
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

    public static List<Object> createMagicNumbers() {
        Random rnd = new Random();

        List<Object> str = new ArrayList<>();
        str.add(BlockchainSystem.COUNT_CHAIN);
        long timeStamp = new Date().getTime();
        str.add(timeStamp);

        int magicNumber = rnd.nextInt((int)10e6) + (int)10e7;
        String currentHash = StringUtil.applySha256(Integer.toString(BlockchainSystem.COUNT_CHAIN) + timeStamp + magicNumber);
        while(!currentHash.startsWith("0".repeat(BlockchainSystem.ZEROS_COUNT))){
            magicNumber = rnd.nextInt((int)10e6) + (int)10e7;
            currentHash = StringUtil.applySha256(Integer.toString(BlockchainSystem.COUNT_CHAIN) + timeStamp  + magicNumber);
        }
        str.add(magicNumber);
        return str;
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


    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getMessagesString() {
        String result = "";
        if (messages != null){
            for (int i = 0; i < messages.size(); i++) {
                result += messages.get(i).getMessageString();
                if (i != messages.size() - 1) {
                    result += "\n";
                }

            }

        }
        if (result == ""){
            return " no messages";
        }
        return result;
    }

}
