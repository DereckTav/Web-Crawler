package hypercrawl.test;

import java.util.LinkedList;
import java.util.Set;

// not thread safe fix
public class Queue {

    private LinkedList<String> queue;
    
    /**
     * Create Queue that all spiders can access 
     */
    public Queue() {
        queue = new LinkedList<>();
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
