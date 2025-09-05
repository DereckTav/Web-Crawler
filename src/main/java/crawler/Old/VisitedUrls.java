package crawler.Old;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class VisitedUrls {
    
    private final Set<String> visited; // Stores visited URLs
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); // For thread safety
    private final int size; // Maximum number of URLs to track

    /**
     * Creates a new VisitedUrls set with a maximum size.
     * @param size the maximum number of URLs to track
     */
    public VisitedUrls(int size) {
        visited = new HashSet<>();
        this.size = size;
    }

    /**
     * Checks if a URL has already been visited.
     * @param link the URL to check
     * @return true if the URL has been visited, false otherwise
     */
    public boolean contains(String link) {
        lock.readLock().lock();
        try {
            return visited.contains(link);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns true if the set has reached its maximum size.
     * @return true if the maximum size is reached, false otherwise
     */
    public boolean isFull() {
        lock.readLock().lock();
        try {
            return visited.size() >= size;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Marks a URL as visited by adding it to the set.
     * @param link the URL to add
     */
    public void add(String link) {
        lock.writeLock().lock();
        try {
            visited.add(link);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Removes all URLs from the set of visited URLs.
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            visited.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Returns a copy of the set of visited URLs.
     * @return a set containing all visited URLs
     */
    public Set<String> getvisited() {
        lock.readLock().lock();
        try {
            return new HashSet<>(visited); // Return a copy to avoid external modification
        } finally {
            lock.readLock().unlock();
        }
    }
}
