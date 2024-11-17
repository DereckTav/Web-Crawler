package hypercrawl.test;

import java.util.HashSet;
import java.util.Set;

public class Crawl {

    private Set<String> crawled;

    public Crawl() {
        crawled = new HashSet<>(); 
    }

    public boolean contains(String link) {
        return crawled.contains(link);
    }

    public void add(String link) {
        crawled.add(link);
    }
    
    public void clear() {
        crawled.clear();
    }
}
