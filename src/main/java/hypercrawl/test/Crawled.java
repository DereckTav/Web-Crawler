package hypercrawl.test;

import java.util.HashSet;
import java.util.Set;

// not thread safe fix
public class Crawled {

    private Set<String> processed;
    private Set<String> discovered;

    /**
     * Create Crawled list that all spiders can access
     */
    public Crawled() {
        processed = new HashSet<>(); 
        discovered = new HashSet<>();
    }

    public boolean contains(String link) {
        return processed.contains(link) || discovered.contains(link);
    }

    public void processed(String link) {
        discovered.remove(link);
        processed.add(link);
    }

    public void discovered(String link) {
        discovered.add(link);
    }
    
    public void clear() {
        discovered.clear();
        processed.clear();
    }
}
