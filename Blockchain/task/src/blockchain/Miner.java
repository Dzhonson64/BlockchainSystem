package blockchain;

import java.util.List;
import java.util.concurrent.Callable;

public class Miner implements Callable<List<Object>> {

    @Override
    public List<Object> call() {
        List<Object> data;
        try{
            synchronized (this){
                data = Chain.createMagicNumbers();
                data.add(Integer.parseInt(Thread.currentThread().getName().substring(Thread.currentThread().getName().length()-1)));
            }
            return data;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
