package hypercrawl.test;

import java.util.Set;

public class Spider implements Runnable {

    private final Queue queue;
    private final Crawled crawled;
    private final LinkFinder linkFinder;
    private String pageURL;

    public Spider(String pageURL, LinkFinder linkFinder, Crawled crawled, Queue queue) {
        this.queue = queue;
        this.crawled = crawled;
        this.linkFinder = linkFinder;
        this.pageURL = pageURL;
    }

    // add delay
    public void crawlPage() { //took away method should just get from queue right since the first thing that happens is run
        // add if statment
        // finish when you have down the bfs part in app
        // may not need discovered and processed in crawled

    }

    @Override
    public void run() {
        if(queue.isEmpty()) {
            queue.enqueue(pageURL);
        }
    }
}
