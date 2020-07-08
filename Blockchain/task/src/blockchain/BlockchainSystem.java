package blockchain;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class BlockchainSystem implements Serializable {
    private volatile List<Chain> chainList;
    protected volatile static int COUNT_CHAIN;
    protected static long maxIdentifyMessage = 1L;
    transient private SerializeFile serializeFile;
    protected volatile static Chain LAST_CHAIN;
    protected volatile static int ZEROS_COUNT;
    public volatile String nameThread;
    public List<Message> messages = null;
    private String pathMetaData = "../metaData.txt";
    private String pathChainsSerializable = "../chains.txt";
    public String[] messText = {
            "Tom: Hey, I'm first!",
            "Sarah: It's not fair!",
            "Sarah: You always will be first because it is your blockchain!",
            "Sarah: Anyway, thank you for this amazing chat.",
            "Tom: You're welcome :)",
            "Nick: Hey Tom, nice chat"
    };

    public BlockchainSystem() {
    }

    public BlockchainSystem(int zerosCount) throws IOException {
        serializeFile = new SerializeFile(this);
        this.chainList = new ArrayList<>();
        COUNT_CHAIN = 0;
        LAST_CHAIN = new Chain("0");
        ZEROS_COUNT = zerosCount;
        nameThread = null;
        messages = new ArrayList<>();

    }

    public void load(String fileMeta, String fileChains) {
        if (Files.exists(Paths.get(fileMeta))) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileMeta))) {
                BlockchainSystem.COUNT_CHAIN = (int) SerializeFile.readWithoutClose(inputStream);
                BlockchainSystem.LAST_CHAIN = (Chain) SerializeFile.readWithoutClose(inputStream);
                BlockchainSystem.ZEROS_COUNT = (int) SerializeFile.readWithoutClose(inputStream);
            } catch (IOException e) {
                return;
            }




        } else {
            return;
        }
        if (Files.exists(Paths.get(fileChains))) {
            Chain chain;
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileChains))) {
                for (int i = 0; i < BlockchainSystem.COUNT_CHAIN; i++) {
                    chain = (Chain) SerializeFile.readWithoutClose(inputStream);

                    getChainList().add(chain);
                }
            } catch (IOException e) {
                return;
            }




        } else {
            return;
        }
        if (validate()){
            System.out.println("This Blockchain is valid");
        }else{
            System.out.println("This Blockchain isn't valid");
        }


    }

    public String getPathMetaData() {
        return pathMetaData;
    }

    public void setPathMetaData(String pathMetaData) {
        this.pathMetaData = pathMetaData;
    }

    public String getPathChainsSerializable() {
        return pathChainsSerializable;
    }

    public void setPathChainsSerializable(String pathChainsSerializable) {
        this.pathChainsSerializable = pathChainsSerializable;
    }

    public void setChainList(List<Chain> chainList) {
        this.chainList = chainList;
    }

    public static int getCountChain() {
        return COUNT_CHAIN;
    }

    public static void setCountChain(int countChain) {
        COUNT_CHAIN = countChain;
    }

    public SerializeFile getSerializeFile() {
        return serializeFile;
    }

    public void setSerializeFile(SerializeFile serializeFile) {
        this.serializeFile = serializeFile;
    }

    public static Chain getLastChain() {
        return LAST_CHAIN;
    }


    public static void setLastChain(Chain lastChain) {
        LAST_CHAIN = lastChain;
    }

    public static int getZerosCount() {
        return ZEROS_COUNT;
    }

    public static void setZerosCount(int zerosCount) {
        ZEROS_COUNT = zerosCount;
    }

    public String getNameThread() {
        return nameThread;
    }

    public void setNameThread(String nameThread) {
        this.nameThread = nameThread;
    }


    public void simpleShow() {
        for (Chain chain : chainList) {
            System.out.println(chain.toString());

        }
    }


    public List<Chain> getChainList() {
        return chainList;
    }

    public void showFromFile() {
        System.out.println("Validate is " + validate());
        System.out.println("\n\n\nDATA IN FILE");
        for (Chain i : chainList) {
            System.out.println(i);
        }

    }


    public boolean validate() {

        String prevHash = "0";
        for (var chain : chainList) {
            BlockChainValidator.identifierIsBiggerPrevBlock(chain.getMessages(), chain.getMessages().size());
            BlockChainValidator.isValidMessage(chain.getMessages());
            if (!chain.validate(prevHash)) {
                System.out.println("Invalid block: " + chain);
                return false;
            }
            if (chain.getPrevChain().getId() != 0 && BlockChainValidator.identifierIsBigger(chain.getId(), chain.getPrevChain().getId())){
                System.out.println("Invalid block: " + chain);
                return false;
            }

            if (!chain.getCurrentHash().equals(chain.createHash())){
                System.out.println(chain.getCurrentHash());
                System.out.println(chain.createHash());
                System.out.println("Invalid block: " + chain);
                return false;
            }
            prevHash = chain.getCurrentHash();
        }
        return true;
    }



    public void mining() throws Exception {
        ExecutorService executorService = Executors.newWorkStealingPool();
        Chain chain = null;
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(getPathChainsSerializable(), true))) {
            for (int i = 0; i < 5; i++) {
                Set<Callable<List<Object>>> tasks = new HashSet<>();
                for (int j = 0; j < 16; j++) {
                    tasks.add(new Miner());
                }

                double start = System.nanoTime();
                synchronized (this) {
                    GenerateKeys.generate("KeyPair/" + COUNT_CHAIN + "/publicKey", "KeyPair/" + COUNT_CHAIN + "/privateKey");
                    messages.add(Message.create(messText[i], "KeyPair/" + COUNT_CHAIN + "/privateKey", "MyData/" + COUNT_CHAIN + "/SignedData.txt" , maxIdentifyMessage));
                    maxIdentifyMessage++;
                }

                List<Object> data = executorService.invokeAny(tasks);
                double end = System.nanoTime();
                chain = new Chain((int) data.get(0), (long) data.get(1), (int) data.get(2), (int) data.get(3), end - start, LAST_CHAIN);


                synchronized (this) {
                    BlockchainSystem.COUNT_CHAIN++;
                    BlockchainSystem.LAST_CHAIN = chain;
                    pushMessages(chain);

                    SerializeFile.writeWithoutClose(chain, objectOutputStream);

                }
                System.out.println(chain);
                if (chain.getGenerationTime() > 1 && BlockchainSystem.ZEROS_COUNT > 1) {
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
                    System.out.println("N stays the same " + BlockchainSystem.ZEROS_COUNT + '\n');
                }

                //VerifyMessage.verify();
                maxIdentifyMessage = 1L;
            }
        }catch (IOException e) {
            return;
        }

        //shutdownAndAwaitTermination(executorService);
        executorService.shutdownNow();
        final boolean done = executorService.awaitTermination(5, TimeUnit.SECONDS);

        saveBlockchainMetaData(pathMetaData);

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

        for (String i : threads) {
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

    private void pushMessages(Chain chain) {
        chain.setMessages(new ArrayList<>(messages));
        messages.clear();
    }

    private void saveBlockchainMetaData(String path) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            SerializeFile.writeWithoutClose(COUNT_CHAIN, objectOutputStream);
            SerializeFile.writeWithoutClose(LAST_CHAIN, objectOutputStream);
            SerializeFile.writeWithoutClose(ZEROS_COUNT, objectOutputStream);
        }catch (IOException e) {
            return;
        }

    }



}
