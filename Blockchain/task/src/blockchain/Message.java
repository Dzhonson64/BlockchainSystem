package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Message implements Serializable {
    private List<byte[]> list;
    private long identifier;

    //The constructor of Message class builds the list that will be written to the file.
    //The list consists of the message and the signature.
    public Message(String data, String keyFile, long identifier) throws InvalidKeyException, Exception {
        list = new ArrayList<byte[]>();
        list.add(data.getBytes());
        list.add(sign(data, keyFile));
        this.identifier = identifier;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    //The method that signs the data using the private key that is stored in keyFile path
    public byte[] sign(String data, String keyFile) throws InvalidKeyException, Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivate(keyFile));
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    //Method to retrieve the Private Key from a file
    public PrivateKey getPrivate(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public String getMessageString() {
        return new String(list.get(0));
    }

    //Method to write the List of byte[] to a file
    private void writeToFile(String filename) throws FileNotFoundException, IOException {
        File f = new File(filename);
        f.getParentFile().mkdirs();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(list);
        out.close();
        //System.out.println("Your file is ready.");
    }


    public static Message create(String data, String pathFileKey, String pathFileSign, long identifier) throws InvalidKeyException, IOException, Exception {
        Message message = new Message(data, pathFileKey, identifier);
        message.writeToFile(pathFileSign);
        return message;
    }
}
