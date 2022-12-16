package graphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class UnitManagerThread extends Thread{

    public static final long WAITING_TIME = 1000;

    private static Logger LOGGER =  LoggerFactory.getLogger("UnitManagerThread");

    private boolean isRunning = true;

    private Queue<UnitMessage> queue = new LinkedBlockingQueue<>();

    public void stopThread(){
        this.isRunning = false;
    }

    public void addMessageInQueue(UnitMessage unitMessage){
        LOGGER.info("Appending message: "+unitMessage+" to queue of: "+this.getName());
        queue.add(unitMessage);
    }

    @Override
    public void run() {
        while( isRunning ){
            if(!queue.isEmpty()){
                UnitMessage message = queue.poll();
                Unit destination = message.getDestination();
                destination.receiveMessage(message);
            }
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
                LOGGER.warn(getName() + " was inturrupted due to: "+e.getMessage());
                this.isRunning = false;
                throw new RuntimeException(e);
            }
        }
        LOGGER.info(this.getName() + " is going to terminate");
    }
}
