package blockchain;

import java.util.concurrent.Callable;

public class Miner implements Callable<Chain> {

    @Override
    public Chain call() throws Exception {
        Chain c = new Chain();
        synchronized (this){
            c.setMinerId(Integer.parseInt(Thread.currentThread().getName().substring(Thread.currentThread().getName().length()-1)));
        }

        return c;
    }
}
