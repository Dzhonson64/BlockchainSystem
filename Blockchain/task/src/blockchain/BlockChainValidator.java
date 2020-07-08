package blockchain;

import java.util.List;

public class BlockChainValidator {
    public static boolean identifierIsBiggerPrevBlock(List<Message> list, long maxIdentifier) {
        for (Message m : list) {
            if (m.getIdentifier() > maxIdentifier)
                return false;
        }

        return true;
    }

    public static boolean identifierIsBigger(long msgIdentifier, long bcIdentifier) {
        return msgIdentifier > bcIdentifier ? true : false;
    }


    public static boolean tryHash(String hash, int nZero) {
        for (int i = 0; i <= nZero; i++) {
            if (i == nZero) {
                if (hash.charAt(i) == '0')
                    return false;
            } else if (hash.charAt(i) != '0')
                return false;
        }
        return true;
    }

    public static boolean validateTheList(List<Chain> BlockChain) {
        for (int i = 0; i < BlockChain.size() - 1; i++) {
            if (!(BlockChain.get(i + 1).getPrevHash().equals(BlockChain.get(i).getCurrentHash())) ||
                    !(identifierIsBiggerPrevBlock(BlockChain.get(i + 1).getMessages(), BlockChain.get(i + 1).getMessages().size())))
            return false;
        }
        return true;
    }

    public static void isValidMessage(List<Message> list){
        for (int i = 0; i < list.size(); i++){
            try {
                VerifyMessage.verify("MyData/" + i + "/SignedData.txt", "KeyPair/" + i + "/publicKey");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
