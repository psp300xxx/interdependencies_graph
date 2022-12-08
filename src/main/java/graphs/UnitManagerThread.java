package graphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnitManagerThread extends Thread{

    public static final long WAITING_TIME = 1000;

    private static Logger LOGGER =  LoggerFactory.getLogger("UnitManagerThread");

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
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
                LOGGER.warn(getName() + " was inturrupted due to: "+e.getMessage());
                throw new RuntimeException(e);
            }
        }
        LOGGER.info(this.getName() + " is going to terminate");
    }
}
