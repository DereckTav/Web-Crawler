/**
 * The Crawled class is responsible for tracking discovered and processed hyperlinks
 * during the crawling process. It ensures thread-safe access to the sets of discovered 
 * and processed URLs using a ReentrantReadWriteLock.
 * 
 * Features:
 * - Tracks URLs that have been discovered but not yet processed.
 * - Tracks URLs that have been processed.
 * - Provides thread-safe access to these sets using read/write locks.
 * - Provides the ability to clear the discovered and processed sets.
 * - Limits the number of processed links to a predefined size.
 * 
 * Usage:
 * - Add discovered links to the discovered set.
 * - Once a link is processed, move it from the discovered set to the processed set.
 * - Use `getLinks()` to retrieve the set of processed links.
 * - Can check if the maximum number of processed links has been reached using `isFull()`.
 */
package hypercrawl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Crawled {
    
    // private final Set<String> processed; // Set to store processed hyperlinks to be returned
    private final Set<String> discovered; // Set to store discovered hyperlinks
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); // Lock for thread safety
    private final int size; // Maximum size for the processed set

    /**
     * Initializes the Crawled class, setting up the discovered and processed sets.
     * Also defines a maximum size for the processed set (default: 50).
     */
    public Crawled() {
        // processed = new HashSet<>(); 
        discovered = new HashSet<>();
        size = 200; // Set the maximum number of processed URLs
    }

        /**
     * Checks if the link has already been discovered or processed.
     * Uses a read lock to allow concurrent reading of the discovered and processed sets.
     * 
     * @param link The hyperlink to check.
     * @return true if the link is either discovered or processed, false otherwise.
     */
    public boolean contains(String link) {
        lock.readLock().lock(); // Acquire read lock
        try {
            return discovered.contains(link);
        } finally {
            lock.readLock().unlock(); // Release read lock
        }
    }

    /**
     * Checks if the processed set has reached its predefined size limit.
     * 
     * @return true if the processed set is full, false otherwise.
     */
    public boolean isFull() {
        return discovered.size() >= size;
    }

    /**
     * Marks a link as processed by moving it from the discovered set to the processed set.
     * Ensures that the processed set does not exceed its size limit.
     * 
     * @param link The hyperlink to mark as processed.
     */
    // public void processed(String link) {
    //     lock.writeLock().lock(); // Acquire write lock for modifying the sets
    //     try {
    //         if (isFull()) {
    //             return; // Prevent adding more processed links if the limit is reached
    //         }

    //         discovered.remove(link); // Remove link from discovered set
    //         processed.add(link); // Add link to processed set
    //     } finally {
    //         lock.writeLock().unlock(); // Release write lock
    //     }
    // }

    /**
     * Marks a link as discovered, adding it to the discovered set.
     * 
     * @param link The hyperlink to mark as discovered.
     */
    public void discovered(String link) {
        lock.writeLock().lock(); // Acquire write lock for modifying the sets
        try {
            discovered.add(link);
        } finally {
            lock.writeLock().unlock(); // Release write lock
        }
    }
    
    /**
     * Clears both the discovered and processed sets.
     * This method removes all entries from both sets.
     */
    public void clear() {
        lock.writeLock().lock(); // Acquire write lock for clearing the sets

        try{
            discovered.clear();
            // processed.clear();
        } finally {
            lock.writeLock().unlock(); // Release write lock
        }
    }

    /**
     * Retrieves the set of processed links.
     * 
     * @return The set of processed hyperlinks.
     */
    public Set<String> getLinks() {
        return discovered;
    }
}
