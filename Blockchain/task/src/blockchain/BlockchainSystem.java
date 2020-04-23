package blockchain;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockchainSystem {
    private String filePathSerialize;
    private List<Chain> chainList;
    private static int countChain;
    private Chain tempChain;



    public BlockchainSystem(String filePathSerialize) {
        SerializeFile.clearFile(filePathSerialize);
        SerializeFile.openSerialize(filePathSerialize);
        this.filePathSerialize = filePathSerialize;
        this.chainList = new ArrayList<>();
        countChain = 0;
        tempChain = null;

    }

    public void createChain(int countZeros, String str) {
        long timeStart = System.currentTimeMillis();
        int numMagic = StringUtil.createMagicNumber();
        Chain chain;
        if (countChain == 0) {
            chain = new Chain(
                    countZeros,
                    StringUtil.applySha256(str, numMagic),
                    countChain,
                    numMagic
            );
            //chainList.add(chain);
        }
        else {
            chain = new Chain(
                    countZeros,
                    StringUtil.applySha256(str, numMagic),
                    countChain,
                    tempChain.getCurrentHash(),
                    numMagic
            );
            //chainList.add(chain);
        }
        long timeFinish = System.currentTimeMillis();
        System.out.print(chain.toString());
        System.out.println("Block was generating for " + (timeFinish - timeStart)*10e-3 + " seconds\n");
        tempChain = chain;
        SerializeFile.writeSerialize(chain);
        countChain++;
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
        for (int i = 0; i < countChain; i++) {
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
