package hypercrawl.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Crawled {
    
    private final Set<String> processed; // processed list of hyperlinks to be returned
    private final Set<String> discovered;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final int size;

    /**
     * Create Crawled list that all spiders can access
     */
    public Crawled() {
        processed = new HashSet<>(); 
        discovered = new HashSet<>();
        size = 50;
    }

    public boolean contains(String link) {
        lock.readLock().lock();
        try {
            return processed.contains(link) || discovered.contains(link);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isFull() {
        return processed.size() >= size;
    }

    public void processed(String link) {
        lock.writeLock().lock();
        try {
            if (isFull()) {
                return;
            }

            discovered.remove(link);
            processed.add(link);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void discovered(String link) {
        lock.writeLock().lock();
        try {
            discovered.add(link);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try{
            discovered.clear();
            processed.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<String> getLinks() {
        return processed;
    }
}
