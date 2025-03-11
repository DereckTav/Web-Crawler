package hypercrawl.test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class Spider implements Runnable {

    private final Queue queue;
    private final Crawled crawled;
    private final LinkFinder linkFinder;
    private final ExecutorService executorService;
    private final String rootURL;
    private final CountDownLatch latch;

    public Spider(String rootURL, Crawled crawled, Queue queue, ExecutorService executorService, CountDownLatch latch) {
        this.queue = queue;
        this.crawled = crawled;
        this.linkFinder = new LinkFinder();
        this.executorService = executorService;
        this.rootURL = rootURL;
        this.latch = latch;
    }

    public void intialCrawl() {
        String rootURL = queue.dequeue();
        Set<String> hyperLinks = linkFinder.getLinks(rootURL);

        for (String link : hyperLinks) {
            if (!crawled.contains(link)) {
                crawled.discovered(link);
                queue.enqueue(link);
            }
        }
        
        //TODO might want to remove
        crawled.processed(rootURL);
    }

    public void crawlPage() { 
        // crawled allows us to see if link was discovered or not

        while (!queue.isEmpty() && !crawled.isFull()) {
            String pageURL = queue.dequeue();
            Set<String> hyperLinks = linkFinder.getLinks(pageURL);
            for (String link : hyperLinks) {
                if (!crawled.contains(link)) {
                    crawled.discovered(link);
                    queue.enqueue(link);
                }
            }

            crawled.processed(pageURL);
        }

        System.out.println("finished crawling");
        latch.countDown();
        executorService.shutdown();
    }

    @Override
    public void run() {
        try {
            if (queue.isEmpty()) {
                crawled.discovered(rootURL);
                queue.enqueue(rootURL);
        
                System.out.println("mother crawling");
                intialCrawl();
        
                for (int thread = 0; thread < 4; thread++) {
                    Spider babySpider = new Spider(rootURL, crawled, queue, executorService, latch);
                    executorService.submit(babySpider);
                }
            }
        
            System.out.println("crawling");
            crawlPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
