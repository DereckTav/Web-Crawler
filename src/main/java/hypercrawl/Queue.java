/**
 * The Queue class is responsible for managing a thread-safe queue of URLs to be crawled.
 * It utilizes a LinkedBlockingQueue to ensure that multiple threads can safely add and remove URLs 
 * from the queue. The queue is shared among multiple spiders during the crawling process.
 * 
 * Features:
 * - Allows adding single or multiple links to the queue.
 * - Provides thread-safe methods to enqueue and dequeue links.
 * - Includes a method to check if the queue is empty.
 * - Can clear the queue when needed.
 * 
 * Usage:
 * - Spiders can add discovered links to the queue and dequeue links for further crawling.
 */
package hypercrawl;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue {

    private LinkedBlockingQueue<String> queue;  // Thread-safe queue to store URLs
    
    /**
     * Initializes the Queue by creating a new LinkedBlockingQueue.
     * The queue will hold the URLs to be crawled.
     */
    public Queue() {
        queue = new LinkedBlockingQueue<>();
    }

    /**
     * Adds a set of links to the queue.
     * This method iterates over the set of links and enqueues them one by one.
     * 
     * @param links A set of hyperlinks to be added to the queue.
     */
    public void enqueueList(Set<String> links) {
        for (String link : links) {
            enqueue(link);
        }
    }

    /**
     * Adds a single link to the queue.
     * 
     * @param link The hyperlink to be added to the queue.
     */
    public void enqueue(String link) {
        queue.add(link);
    }

    /**
     * Removes and returns the first link in the queue.
     * This method removes the head of the queue and returns it.
     * 
     * @return The link at the head of the queue.
     */
    public String dequeue() {
        return queue.remove();   
    }

    /**
     * Clears all links in the queue.
     * This method removes all elements from the queue.
     */
    public void clear() {
        queue.clear();
    }

    /**
     * Checks if the queue is empty.
     * 
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
