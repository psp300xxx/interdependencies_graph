package graphs.publish_subscribe;

import graphs.Unit;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class UnitManagerThread extends Thread implements UnitPublisher{

    private AtomicArrayList<UnitMessage> messageReceived = new AtomicArrayList<>();


    private AtomicArrayList<UnitMessage> messageQueue = new AtomicArrayList<>();

    private boolean isSimulationOver = false;

    private AtomicBoolean hasToWait = new AtomicBoolean(true);

    public UnitManagerThread(){

    }


    public void endSimulation(){
        this.isSimulationOver = true;
        Thread.currentThread().notify();
    }

    @Override
    public synchronized void run() {
        while(!isSimulationOver){
            try {
                while(hasToWait.getAcquire()){
                    this.wait(500);
                }
                while( !messageReceived.isEmpty() ){
                    UnitMessage nextMessage = messageReceived.remove(0);
                    nextMessage.getReceiverUnit().receiveMessage(nextMessage.getSenderUnit(), nextMessage);
                }
                hasToWait.set(true);
                while( !messageQueue.isEmpty() ){
                    UnitMessage nextMessage = messageQueue.remove(0);
                    System.out.println("Sending message to "+ nextMessage.getReceiverUnit());
                    publishPvt(nextMessage.getSenderUnit(), nextMessage);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Thread " + getName() + " over");
    }

    public synchronized void receiveMsg(Unit unit, UnitMessage unitMessage){
        this.messageReceived.add(unitMessage);
        hasToWait.set(false);
    }

    private void publishPvt(Unit unit, UnitMessage m){
        m.getReceiverUnit().getThread().receiveMsg(unit, m);
    }

    @Override
    public synchronized void publish(Unit unit, UnitMessage m) {
        messageQueue.add(m);
        this.hasToWait.set(false);
    }

}
