package blockchain;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;

public class Chain implements Serializable, Callable<Map<String, String>> {
    private static final long serialVersionUID = 1L;
    private int id;
    private String prevHash;
    private String currentHash;
    private long timeStamp;
    private int magicNumber;
    private int zerosCount;
    private int numThread;

    public Chain(int zerosCount){
        prevHash = "0";
        this.zerosCount = zerosCount;
    }

    public Chain(int zerosCount, String prevHash){
        this.prevHash = prevHash;
        this.zerosCount = zerosCount;
    }

    public Chain(int zerosCount, int id) {
        this.timeStamp = new Date().getTime();
        this.id = id;
        prevHash = "0";
        this.zerosCount = zerosCount;
        this.currentHash = getNewHash();


    }
    public Chain(int zerosCount, int id, String prevHash) {
        this.timeStamp = timeStamp;
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
    public void setData(Map<String,  String> data){
        magicNumber = Integer.parseInt(data.get("magicNumber"));
        currentHash = data.get("currentHash");
        id = Integer.parseInt(data.get("id"));
        timeStamp = Long.parseLong(data.get("timeStamp"));
        numThread = Integer.parseInt(data.get("numThread").substring(data.get("numThread").length()-1));
    }

    @Override
    public synchronized String toString() {
        return  "Block:" +
                "\nCreated by miner # " + numThread +
                "\nId: " + id +
                "\nTimestamp: " + timeStamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block:\n" +
                 prevHash +
                "\nHash of the block:\n" +
                currentHash + "\n";
    }

    private String getNewHash(){
        String currentHash = createMagicNumberAndHash().get("currentHash");
        if (currentHash.length() > 64 - zerosCount){
            currentHash = currentHash.substring(0, 64 - zerosCount);
        }
        return currentHash;
    }

    private Map<String, String> createMagicNumberAndHash() {
        //String threadName = Thread.currentThread().getName();
        //System.out.println("Created by miner # " + threadName.substring(threadName.length()-1, threadName.length()));
        Random rnd = new Random();
        magicNumber =  rnd.nextInt((int)10e6) + (int)10e7;
        String currentHash = StringUtil.applySha256(Integer.toString(id) + timeStamp + magicNumber);
        while(!currentHash.startsWith("0".repeat(zerosCount))){
            magicNumber = rnd.nextInt((int)10e6) + (int)10e7;
            currentHash = StringUtil.applySha256(Integer.toString(id) + timeStamp  + magicNumber);
        }

        Map<String, String> h = new LinkedHashMap<>();
        h.put("currentHash", currentHash);
        h.put("magicNumber", Integer.toString(magicNumber));
        h.put("id", Integer.toString(id));
        h.put("timeStamp", Long.toString(timeStamp));
        h.put("numThread", Thread.currentThread().getName());

        return h;
    }



    @Override
    public Map<String, String> call() throws Exception {
        return  createMagicNumberAndHash();
    }
}
