package hypercrawl.test;

import java.util.Set;

public class Spider {

    private Queue queue;
    private Crawled crawled;
    private LinkFinder linkFinder;

    public Spider(String pageURL, LinkFinder linkFinder, Crawled crawled, Queue queue) {
        this.queue = queue;
        this.crawled = crawled;
        this.linkFinder = linkFinder;
        this.crawlPage(pageURL);
    }

    public void crawlPage(String pageURL) {
        // if first spider add page to queue
        if (queue.isEmpty()) {
            queue.enqueue(pageURL); 
        }

        // finish when you have down the bfs part in app
        // may not need discovered and processed in crawled
        
    }
}
