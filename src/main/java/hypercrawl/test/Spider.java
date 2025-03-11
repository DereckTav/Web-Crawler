/**
 * The Spider class is responsible for crawling and processing web pages concurrently.
 * It is a part of a multi-threaded system that uses a queue to manage URLs and tracks crawled URLs.
 * 
 * Each Spider instance crawls a page, discovers hyperlinks, and adds them to the queue for further crawling.
 * The Spider can also spawn new threads (baby spiders) to crawl additional pages concurrently.
 * 
 * Features:
 * - Handles initial crawling of a root URL and subsequent pages.
 * - Uses a queue to manage discovered URLs and ensure concurrent crawling.
 * - Tracks discovered and processed URLs using the Crawled object.
 * - Implements the Runnable interface to run concurrently with other spiders.
 */
package hypercrawl.test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class Spider implements Runnable {

    private final Queue queue;  // Queue for managing URLs to crawl
    private final Crawled crawled;  // Keeps track of discovered and processed URLs
    private final LinkFinder linkFinder;  // Finds hyperlinks on a given page
    private final ExecutorService executorService;  // Executor service for thread management
    private final String rootURL;  // The root URL to start crawling from
    private final CountDownLatch latch;  // Latch to synchronize threads

    /**
     * Constructor for the Spider class.
     * 
     * @param rootURL The URL from which crawling starts.
     * @param crawled The Crawled object to track discovered and processed URLs.
     * @param queue The Queue to manage URLs to crawl.
     * @param executorService The ExecutorService to handle thread execution.
     * @param latch The CountDownLatch to synchronize threads.
     */
    public Spider(String rootURL, Crawled crawled, Queue queue, ExecutorService executorService, CountDownLatch latch) {
        this.queue = queue;
        this.crawled = crawled;
        this.linkFinder = new LinkFinder();
        this.executorService = executorService;
        this.rootURL = rootURL;
        this.latch = latch;
    }

     /**
     * Performs the initial crawl of the root URL, discovering its hyperlinks and adding them to the queue.
     */
    public void intialCrawl() {
        // Dequeue the root URL and find hyperlinks on the page
        String rootURL = queue.dequeue();
        Set<String> hyperLinks = linkFinder.getLinks(rootURL);

        // Iterate over the found links and add them to the queue if they haven't been crawled
        for (String link : hyperLinks) {
            if (!crawled.contains(link)) {
                crawled.discovered(link);  
                queue.enqueue(link);  
            }
        }
        
        // Mark the root URL as processed (can be removed if unnecessary)
        crawled.processed(rootURL);
    }

    /**
     * Crawls pages by dequeuing URLs from the queue, discovering hyperlinks, and processing them.
     * It continues crawling until the queue is empty or the crawling limit is reached.
     */
    public void crawlPage() { 
        // Continue crawling while there are URLs in the queue and the crawled set isn't full
        while (!queue.isEmpty() && !crawled.isFull()) {
            String pageURL = queue.dequeue();
            Set<String> hyperLinks = linkFinder.getLinks(pageURL);  // Find hyperlinks on the page
            for (String link : hyperLinks) {
                if (!crawled.contains(link)) {
                    crawled.discovered(link);
                    queue.enqueue(link);  
                }
            }

            crawled.processed(pageURL);
        }

        // Print a message when crawling is finished
        System.out.println("finished crawling");

        // Count down the latch to signal that the crawling is done for this spider
        latch.countDown();

        // Shutdown the executor service to stop further thread submissions
        executorService.shutdown();
    }

    /**
     * The run method is executed when the Spider thread starts.
     * It initializes the crawling process, starts additional threads (baby spiders), 
     * and manages the crawling of pages concurrently.
     */
    @Override
    public void run() {
        try {
            // If the queue is empty, start the initial crawl for the root URL
            if (queue.isEmpty()) {
                crawled.discovered(rootURL); // Mark the root URL as discovered
                queue.enqueue(rootURL); // Add the root URL to the queue
        
                System.out.println("mother crawling");
                intialCrawl();
                
                // Create and submit 4 baby spiders to crawl more pages concurrently
                for (int thread = 0; thread < 4; thread++) {
                    Spider babySpider = new Spider(rootURL, crawled, queue, executorService, latch);
                    executorService.submit(babySpider);
                }
            }
            
            // Start crawling the pages
            System.out.println("crawling");
            crawlPage();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}
