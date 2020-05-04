package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class BlockchainSystem {
    private final String filePathSerialize;
    private volatile List<Chain> chainList;
    protected volatile static int COUNT_CHAIN;
    private SerializeFile serializeFile;
    protected volatile static Chain LAST_CHAIN;
    protected volatile static int ZEROS_COUNT;
    public volatile String nameThread;
    public List<String> messages = null;
    public String[] mess = {
            "Tom: Hey, I'm first!",
            "Sarah: It's not fair!",
            "Sarah: You always will be first because it is your blockchain!",
            "Sarah: Anyway, thank you for this amazing chat.",
            "Tom: You're welcome :)",
            "Nick: Hey Tom, nice chat"
    };




    public BlockchainSystem(String filePathSerialize, int zerosCount) throws IOException {
        serializeFile = new SerializeFile(this);
        this.filePathSerialize = filePathSerialize;
        this.chainList = new ArrayList<>();
        COUNT_CHAIN = 0;
        LAST_CHAIN = new Chain("0");
        ZEROS_COUNT = zerosCount;
        nameThread = null;
        var path = Paths.get(getFilePathSerialize() );
        if (Files.exists(path)) {
            Files.delete(path);
        }
        messages = new ArrayList<>();
    }

    public void simpleShow() {
        for (Chain chain: chainList) {
            System.out.println(chain.toString());

        }
    }

    public String getFilePathSerialize() {
        return filePathSerialize;
    }

    public List<Chain> getChainList() {
        return chainList;
    }

    public void showFromFile() {
        deserialize();
        System.out.println("Validate is " + validate());
        System.out.println("\n\n\nDATA IN FILE");
        for (Chain i : chainList){
            System.out.println(i);
        }

    }

    private void deserialize(){
        for (int i = 0; i < COUNT_CHAIN; i++) {
            Chain p = (Chain) serializeFile.readSerialize();
            chainList.add(p);
        }
        serializeFile.closeDeserialize();
    }

    public boolean validate() {
        if (chainList.isEmpty()){
            deserialize();
        }
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

    /*public void mining() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newWorkStealingPool();

        int N = 0;
        for (int i = 0; i < 5; i++) {
            Set<Callable<Chain>> tasks = new HashSet<>();
            for (int j = 0; j < 16; j++) {
                tasks.add(new Miner());
            }

            Chain data = null;
            try
            {
                data = executorService.invokeAny(tasks);
                serializeFile.save(data);
                System.out.println(data);

            }
            catch (InterruptedException | ExecutionException | IOException e)
            {
                e.printStackTrace();
            }


            synchronized (this){
                BlockchainSystem.COUNT_CHAIN++;
                BlockchainSystem.LAST_CHAIN = data;
            }

            if (data.getGenerationTime() > 1 && BlockchainSystem.ZEROS_COUNT > 0) {
                synchronized (this) {
                    BlockchainSystem.ZEROS_COUNT--;
                }
                System.out.println("N was decreased by 1\n");
            } else if (data.getGenerationTime() < 1) {
                synchronized (this) {
                    BlockchainSystem.ZEROS_COUNT++;
                }
                System.out.println("N was increased to " + BlockchainSystem.ZEROS_COUNT + '\n');
            } else {
                System.out.println("N stays the same\n " + BlockchainSystem.ZEROS_COUNT + '\n');
            }
        }

        //shutdownAndAwaitTermination(executorService);
        executorService.shutdownNow();
        final boolean done = executorService.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Все ли письма были отправлены? - " + done);
    }*/


    public void mining2() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newWorkStealingPool();
        Chain chain = null;
        int N = 0;
        for (int i = 0; i < 5; i++) {
            Set<Callable<List<Object>>> tasks = new HashSet<>();
            for (int j = 0; j < 16; j++) {
                tasks.add(new Miner());
            }


            if (BlockchainSystem.COUNT_CHAIN != 0){
                Random r = new Random();
                int g = r.nextInt(mess.length);
                setMessage(mess[g]);
            }
            double start = System.nanoTime();
            List<Object> data = executorService.invokeAny(tasks);
            double end = System.nanoTime();
            chain = new Chain((int)data.get(0), (long)data.get(1), (int)data.get(2), (int)data.get(3), end - start);


            synchronized (this){
                BlockchainSystem.COUNT_CHAIN++;
                BlockchainSystem.LAST_CHAIN = chain;
                pushMessages();
                serializeFile.save(BlockchainSystem.LAST_CHAIN);
                System.out.println(chain);
            }

            if (chain.getGenerationTime() > 1 && BlockchainSystem.ZEROS_COUNT > 0) {
                synchronized (this) {
                    BlockchainSystem.ZEROS_COUNT--;
                }
                System.out.println("N was decreased by 1\n");
            } else if (chain.getGenerationTime() < 1 && BlockchainSystem.ZEROS_COUNT < 4) {
                synchronized (this) {
                    BlockchainSystem.ZEROS_COUNT++;
                }
                System.out.println("N was increased to " + BlockchainSystem.ZEROS_COUNT + '\n');
            } else {
                System.out.println("N stays the same" + BlockchainSystem.ZEROS_COUNT + '\n');
            }
        }

        //shutdownAndAwaitTermination(executorService);
        executorService.shutdownNow();
        final boolean done = executorService.awaitTermination(5, TimeUnit.SECONDS);
        //System.out.println("Все ли письма были отправлены? - " + done);
    }

    public void showActiveThreads() {
        List<String> threads = new ArrayList<>();
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = threadGroup.getParent()) != null) {
            if (threadGroup != null) {
                threadGroup = parent;
                System.out.println(parent.getName());
                Thread[] threadList = new Thread[threadGroup.activeCount()];
                threadGroup.enumerate(threadList);
                for (Thread thread : threadList)
                    threads.add(new StringBuilder().append(thread.getThreadGroup().getName())
                            .append("::").append(thread.getName()).append("::PRIORITY:-")
                            .append(thread.getPriority()).toString());
            }
        }

        for (String i: threads){
            System.out.println(i);
        }
    }
    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdownNow(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
    public void setMessage(String message){
        messages.add(message);
    }
    private void pushMessages(){
        BlockchainSystem.LAST_CHAIN.setMessages(new ArrayList<>(messages));
        messages.clear();
    }

}
