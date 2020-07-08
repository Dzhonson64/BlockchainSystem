package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SerializeFile implements Serializable {
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

//    public List<OutputStream> saveChain(Object object, String filepath, FileOutputStream fileOutputStream, ObjectOutputStream objectOutputStream) throws IOException {
//        synchronized (blockchainSystem.getChainList()) {
//            if (filepath == null) {
//                return null;
//            }
//            return writeWithoutClose(filepath, object, fileOutputStream, objectOutputStream);
//
//        }
//    }


    public static boolean write(String file, Object obj) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file, true))) {
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void writeWithoutClose(Object obj, ObjectOutputStream objectOutputStream) {
        try {

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.reset();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static Object read(String file) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static Object readWithoutClose(ObjectInputStream objectInputStream) {
        try {
            Object o =  objectInputStream.readObject();
            return o;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObjectOutputStream getOs() {
        return os;
    }

    public void setOs(ObjectOutputStream os) {
        this.os = os;
    }

    public ObjectInputStream getIs() {
        return is;
    }

    public void setIs(ObjectInputStream is) {
        this.is = is;
    }

    public FileOutputStream getFos() {
        return fos;
    }

    public void setFos(FileOutputStream fos) {
        this.fos = fos;
    }

    public FileInputStream getFis() {
        return fis;
    }

    public void setFis(FileInputStream fis) {
        this.fis = fis;
    }

    public BlockchainSystem getBlockchainSystem() {
        return blockchainSystem;
    }

}
