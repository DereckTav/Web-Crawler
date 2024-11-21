package hypercrawl.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Crawled {
    
    private final Set<String> processed;
    private final Set<String> discovered;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Create Crawled list that all spiders can access
     */
    public Crawled() {
        processed = new HashSet<>(); 
        discovered = new HashSet<>();
    }

    public boolean contains(String link) {
        lock.readLock().lock();
        try {
            return processed.contains(link) || discovered.contains(link);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void processed(String link) {
        lock.writeLock().lock();
        try {
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
}
