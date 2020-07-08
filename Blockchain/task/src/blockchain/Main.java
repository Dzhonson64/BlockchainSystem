package blockchain;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {
        int N = 1;
        BlockchainSystem blockchainSystem =  new BlockchainSystem(N);
        blockchainSystem.load("../metaData.txt", "../chains.txt");
        blockchainSystem.mining();


    }
}



