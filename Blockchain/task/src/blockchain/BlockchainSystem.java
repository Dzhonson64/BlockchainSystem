package blockchain;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BlockchainSystem {
    private final String filePathSerialize;
    private volatile List<Chain> chainList;
    private volatile static int COUNT_CHAIN;
    private volatile static Chain tempChain;
    public volatile int ZEROS_COUNT;
    public volatile String nameThread;




    public BlockchainSystem(String filePathSerialize, int zerosCount) {
        SerializeFile.clearFile(filePathSerialize);
        SerializeFile.openSerialize(filePathSerialize);
        this.filePathSerialize = filePathSerialize;
        this.chainList = new ArrayList<>();
        COUNT_CHAIN = 0;
        tempChain = null;
        ZEROS_COUNT = zerosCount;
        nameThread = null;
    }

    public double createChain(Map<String, String> data){
        long timeStart = System.nanoTime();
        Chain chain;
        if (COUNT_CHAIN == 0) {
            chain = new Chain(
                    ZEROS_COUNT
            );
        }
        else {
            chain = new Chain(
                    ZEROS_COUNT,
                    tempChain.getCurrentHash()
            );
        }
        double timeFinish = System.nanoTime();
        synchronized (this){
            if (nameThread == null){
                nameThread = Thread.currentThread().getName();
            }
        }
        //chainList.add(chain);
        chain.setData(data);

        System.out.print(chain.toString());
        double time =  (timeFinish - timeStart)/1000000000.0;
        System.out.println("Block was generating for " + time + " seconds");
        //System.out.println("Block was generating for " + time + " seconds");
        tempChain = chain;
        SerializeFile.writeSerialize(chain);
        synchronized (this) {
            COUNT_CHAIN++;
        }

        return time;
    }

    public double createChain() {
        long timeStart = System.nanoTime();
        Chain chain;
        if (COUNT_CHAIN == 0) {
            chain = new Chain(
                    ZEROS_COUNT,
                    COUNT_CHAIN
            );
        }
        else {
            chain = new Chain(
                    ZEROS_COUNT,
                    COUNT_CHAIN,
                    tempChain.getCurrentHash()
            );
        }
        long timeFinish = System.nanoTime();
        synchronized (this){
            if (nameThread == null){
                nameThread = Thread.currentThread().getName();
            }
        }
        //chainList.add(chain);
        System.out.print(chain.toString());
        double time =  (timeFinish - timeStart)/1000000000.0;
        System.out.println("Block was generating for " + time + " seconds");
        System.out.println("Block was generating for " + time + " seconds");
        tempChain = chain;
        SerializeFile.writeSerialize(chain);
        synchronized (this) {
            COUNT_CHAIN++;
        }
        return time;
    }

    public void simpleShow() {
        for (Chain chain: chainList) {
            System.out.println(chain.toString());

        }
    }

    public void showFromFile() {
        SerializeFile.closeSerialize();
        try {
            SerializeFile.openDeserialize(filePathSerialize);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        deserialize();
        System.out.println(validate());
        System.out.println("\n\n\nDATA IN FILE");
        simpleShow();
    }

    private void deserialize(){
        for (int i = 0; i < COUNT_CHAIN; i++) {
            Chain p = (Chain) SerializeFile.readSerialize();
            chainList.add(p);
        }
        SerializeFile.closeDeserialize();
    }

    public boolean validate() {
        if (chainList.get(0).getPrevHash() == null){
            return false;
        }
        for (int i = 1; i < chainList.size(); i++) {
            if (!chainList.get(i).getPrevHash().equals(chainList.get(i-1).getCurrentHash())) {
                return false;
            }
        }
        return true;
    }

    public void mining(int countThreads) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(countThreads);
        int N = 0;
        for (int i = 0; i < 5; i++) {
            List<Chain> tasks = new ArrayList<>();
            for (int j = 0; j < countThreads; j++) {
                tasks.add(new Chain(N, i+1));
            }

            Map<String, String> data = executorService.invokeAny(tasks);
            double time = createChain(data);
            if (time < 3.0E-6){
                N++;
                System.out.println("N was increased to " +  N + "\n");
            }else{
                System.out.println("N stays the same" + "\n");
            }
            /*for (String it: data.values()) {
                System.out.println(it);
            }*/
        }


        try {
            executorService.awaitTermination(60, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
