package hypercrawl.test;

import java.util.LinkedList;
import java.util.Set;

public class Queue {

    private LinkedList<String> queue;
    
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

    public void dequeue() {
        queue.remove();
    }

    public void clear() {
        queue.clear();
    }
}
