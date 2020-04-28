package blockchain;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int N = 1;

        BlockchainSystem blockchainSystem =  new BlockchainSystem("./chainSerealizeable.txt", N);
        blockchainSystem.mining();

    }
}



