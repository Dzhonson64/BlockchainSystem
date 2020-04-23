package blockchain;

import java.io.*;

public class SerializeFile {
    private static ObjectOutputStream os;
    private static ObjectInputStream is;

    public SerializeFile() {

    }
    public static void openSerialize(String fileName){
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(fileName, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //BufferedOutputStream bos = new BufferedOutputStream(f);
        try {
            os =  new ObjectOutputStream(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSerialize(Object obj){
        try {
            os.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDeserialize(String fileName) throws IOException, ClassNotFoundException{
        FileInputStream fi = new FileInputStream(fileName);
        //BufferedInputStream bis = new BufferedInputStream(fi);
        is = new ObjectInputStream(fi);
    }

    public static Object readSerialize() {
        try {
            return  is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeSerialize(){
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeDeserialize(){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(String filename){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }
}
