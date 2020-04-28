package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SerializeFile {
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private FileOutputStream fos;
    private FileInputStream fis;
    private final BlockchainSystem blockchainSystem;
    public boolean isOpenWrite;
    public boolean isOpenRead;

    public SerializeFile(BlockchainSystem blockchainSystem) {
        this.blockchainSystem = blockchainSystem;
        isOpenWrite = false;
        isOpenRead = false;
    }

    public void save(Chain chain) throws IOException {
        synchronized (blockchainSystem.getChainList()) {
            if (blockchainSystem.getFilePathSerialize() == null) {
                return;
            }
            if (!isOpenWrite){
                fos = new FileOutputStream(blockchainSystem.getFilePathSerialize());
                //var buffer = new BufferedOutputStream(file);
                os = new ObjectOutputStream(fos);
                isOpenWrite = true;
            }
            os.writeObject(chain);

        }
    }

    public Object readSerialize() {
        synchronized (blockchainSystem.getChainList()) {
            if (isOpenWrite){
                try {
                    os.close();
                    fos.close();
                    isOpenWrite = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!isOpenRead){

                try {
                    fis = new FileInputStream(blockchainSystem.getFilePathSerialize());
                    is = new ObjectInputStream(fis);
                    isOpenRead = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                return is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void closeDeserialize(){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
