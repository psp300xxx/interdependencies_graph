package graphs;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnitManagerThread extends Thread{

    private static Logger LOGGER =  Logger.getLogger("UnitManagerThread");

    private Lock lock = new ReentrantLock();

    private Set<Unit> units;

    private boolean isRunning = true;

    private Queue<UnitMessage> queue = new LinkedList<>();

    public void stopThread(){
        this.isRunning = false;
    }

    public void addMessageInQueue(UnitMessage unitMessage){
        lock.lock();
        LOGGER.info("Appending message: "+unitMessage+" to queue of: "+this.getName());
        queue.add(unitMessage);
        lock.unlock();
    }

    @Override
    public void run() {
        while( isRunning ){
            if(!queue.isEmpty()){
                lock.lock();
                UnitMessage message = queue.poll();
                Unit destination = message.getDestination();
                destination.receiveMessage(message);
                lock.unlock();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
