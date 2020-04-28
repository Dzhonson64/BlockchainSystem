package blockchain;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BlockchainSystem {
    private final String filePathSerialize;
    private volatile List<Chain> chainList;
    protected volatile static int COUNT_CHAIN;
    protected volatile static Chain LAST_CHAIN;
    protected volatile static int ZEROS_COUNT;
    public volatile String nameThread;




    public BlockchainSystem(String filePathSerialize, int zerosCount) {
        SerializeFile.clearFile(filePathSerialize);
        SerializeFile.openSerialize(filePathSerialize);
        this.filePathSerialize = filePathSerialize;
        this.chainList = new ArrayList<>();
        COUNT_CHAIN = 0;
        LAST_CHAIN = new Chain("0");
        ZEROS_COUNT = zerosCount;
        nameThread = null;
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
        deserialize();
        String prevHash = "0";
        for (var chain : chainList) {
            if (!chain.validate(prevHash)) {
                System.out.println("Invalid block: " + chain);
                return false;
            }
            prevHash = chain.getCurrentHash();
        }
        return true;
    }

    public void mining() throws ExecutionException, InterruptedException {
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        int N = 0;
        for (int i = 0; i < 5; i++) {
            Set<Callable<Chain>> tasks = new HashSet<>();
            for (int j = 0; j < threads; j++) {
                tasks.add(new Miner());
            }

            Chain data = executorService.invokeAny(tasks);
            System.out.println(data);
            synchronized (this){
                BlockchainSystem.COUNT_CHAIN++;
                BlockchainSystem.LAST_CHAIN = data;
            }

            if (data.getGenerationTime() > 1 && BlockchainSystem.ZEROS_COUNT > 0) {
                BlockchainSystem.ZEROS_COUNT--;
                System.out.println("N was decreased by 1\n");
            } else if (data.getGenerationTime() < 1) {
                BlockchainSystem.ZEROS_COUNT++;
                System.out.println("N was increased to " + BlockchainSystem.ZEROS_COUNT + '\n');
            } else {
                System.out.println("N stays the same\n " + BlockchainSystem.ZEROS_COUNT + '\n');
            }
        }


        executorService.shutdownNow();
    }

}
