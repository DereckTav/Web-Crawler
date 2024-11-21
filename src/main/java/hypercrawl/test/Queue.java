package hypercrawl.test;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue {

    private LinkedBlockingQueue<String> queue;
    
    /**
     * Create Queue that all spiders can access 
     */
    public Queue() {
        queue = new LinkedBlockingQueue<>();
    }

    public void enqueueList(Set<String> links) {
        for (String link : links) {
            enqueue(link);
        }
    }

    public void enqueue(String link) {
        queue.add(link);
    }

    public String dequeue() {
        return queue.remove();   
    }

    public void clear() {
        queue.clear();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
