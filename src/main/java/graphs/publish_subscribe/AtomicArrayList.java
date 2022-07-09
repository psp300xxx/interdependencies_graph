package graphs.publish_subscribe;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicArrayList<E> extends ArrayList<E> {

    private Lock lock = new ReentrantLock();

    @Override
    public boolean add(E e) {
        lock.lock();
        boolean res = super.add(e);
        lock.unlock();
        return res;
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        boolean res =  super.isEmpty();
        lock.unlock();
        return res;
    }

    @Override
    public E remove(int index) {
        lock.lock();
        E val = super.remove(index);
        lock.unlock();
        return val;
    }

    @Override
    public boolean remove(Object o) {
        lock.lock();
        boolean res =  super.remove(o);
        lock.unlock();
        return res;
    }
}
