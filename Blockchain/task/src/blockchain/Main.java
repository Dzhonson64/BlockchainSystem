package blockchain;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        int N = 0;
        Scanner sc = new Scanner(System.in);
        try {
            N = sc.nextInt();
        }catch (Exception e){
            System.out.println("Error: " + e.getClass());
        }
        BlockchainSystem b = new BlockchainSystem("./chainSerealizeable.txt", N);
        b.createChain();
        b.createChain();
        b.createChain();
        b.createChain();
        b.createChain();
        /*b.simpleShow();
        b.showFromFile();*/
    }
}



