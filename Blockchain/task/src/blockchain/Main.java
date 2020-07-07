package blockchain;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {
        int N = 0;
        BlockchainSystem blockchainSystem =  new BlockchainSystem("../chainSerealizeable.txt", N);
        blockchainSystem.mining2();

        //blockchainSystem.showActiveThreads();
        //blockchainSystem.showFromFile();


    }
}



