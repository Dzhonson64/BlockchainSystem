package blockchain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BlockchainSystem {
    private final String filePathSerialize;
    private List<Chain> chainList;
    private static int COUNT_CHAIN;
    private Chain tempChain;
    public static int ZEROS_COUNT;



    public BlockchainSystem(String filePathSerialize, int zerosCount) {
        SerializeFile.clearFile(filePathSerialize);
        SerializeFile.openSerialize(filePathSerialize);
        this.filePathSerialize = filePathSerialize;
        this.chainList = new ArrayList<>();
        COUNT_CHAIN = 0;
        tempChain = null;
        ZEROS_COUNT = zerosCount;
    }

    public void createChain() {
        long timeStart = System.nanoTime();
        Chain chain;
        if (COUNT_CHAIN == 0) {
            chain = new Chain(
                    COUNT_CHAIN
            );
        }
        else {
            chain = new Chain(
                    COUNT_CHAIN,
                    tempChain.getCurrentHash()
            );
        }
        long timeFinish = System.nanoTime();
        System.out.print(chain.toString());
        System.out.println("Block was generating for " + (timeFinish - timeStart)/1000000000.0 + " seconds\n");
        tempChain = chain;
        SerializeFile.writeSerialize(chain);
        COUNT_CHAIN++;
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

}
