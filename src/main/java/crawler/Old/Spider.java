/**
 * A web crawler that processes pages concurrently, discovers links, and manages crawling using a queue and a set of visited URLs.
 */
package crawler.Old;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Spider implements Runnable {

    private final LinkedBlockingQueue<String> queue;  // Queue for managing URLs to crawl
    private final VisitedUrls visitedUrls;  // Keeps track of markVisited and processed URLs
    private final LinkFinder linkFinder;  // Finds hyperlinks on a given page
    private final ExecutorService executorService;  // Executor service for thread management
    private String rootURL;  // The root URL to start crawling from
    private int numOfSpiders;

    /**
     * Creates a Spider that starts crawling from the given root URL.
     * @param rootURL the starting URL
     * @param visitedUrls tracks visited URLs
     * @param queue manages URLs to crawl
     * @param executorService thread pool for concurrent crawling
     * @param numOfSpiders number of spiders to create
     */
    public Spider(String rootURL, VisitedUrls visitedUrls, LinkedBlockingQueue<String> queue, 
                ExecutorService executorService, int numOfSpiders, LinkFinder linkFinder) {
        this.queue = queue;
        this.visitedUrls = visitedUrls;
        this.linkFinder = linkFinder;
        this.executorService = executorService;
        this.rootURL = rootURL;
        this.numOfSpiders = numOfSpiders;
    }

    /**
     * Creates a Spider for concurrent crawling (used for baby spiders).
     * @param visitedUrls tracks visited URLs
     * @param queue manages URLs to crawl
     * @param executorService thread pool for concurrent crawling
     */
    public Spider(VisitedUrls visitedUrls, LinkedBlockingQueue<String> queue, 
                ExecutorService executorService, int numOfSpiders, LinkFinder linkFinder) {
        this.queue = queue;
        this.visitedUrls = visitedUrls;
        this.linkFinder = linkFinder;
        this.executorService = executorService;
    }

    /**
     * Crawls the root URL and adds discovered links to the queue.
     */
    public void intialCrawl() {
        // remove the root URL and find hyperlinks on the page
        String rootURL = queue.remove();
        Set<String> hyperLinks = linkFinder.getLinks(rootURL);

        // Iterate over the found links and add them to the queue if they haven't been visitedUrls
        for (String link : hyperLinks) {
            if (!visitedUrls.contains(link)) {
                visitedUrls.add(link);  
                queue.add(link);  
            }
        }
        
        // Mark the root URL as processed (can be removed if unnecessary)
        // visitedUrls.processed(rootURL);
    }

    /**
     * Crawls pages by processing URLs from the queue until empty or the limit is reached.
     */
    public void crawlPage() { 
        //TODO if visitedURLs.isFull end crawling we reached our target
        // Continue crawling while there are URLs in the queue and the visitedUrls set isn't full
        while (!queue.isEmpty() && !visitedUrls.isFull()) {
            String pageURL = queue.remove();
            Set<String> hyperLinks = linkFinder.getLinks(pageURL);  // Find hyperlinks on the page
            for (String link : hyperLinks) {
                if (!visitedUrls.contains(link)) {
                    visitedUrls.add(link);
                    queue.add(link);  
                }
            }

            // visitedUrls.processed(pageURL);
        }

        // Print a message when crawling is finished
        System.out.println("finished crawling");

        // Shutdown the executor service to stop further thread submissions
        executorService.shutdown();
    }

    /**
     * Starts the crawling process and manages concurrent crawling with additional spiders.
     */
    @Override
    public void run() {
        try {
            if (queue.isEmpty()) {
                visitedUrls.add(rootURL);
                queue.add(rootURL);
        
                System.out.println("mother crawling"); //TODO start logging instead of printing
                intialCrawl();
                
                // Create and submit 4 baby spiders to crawl more pages concurrently
                // TODO we are def going to switch this to software threads -- this is just a placeholder
                for (int thread = 0; thread < numOfSpiders - 1; thread++) {
                    // Spider babySpider = new Spider(visitedUrls, queue, executorService);
                    // executorService.submit(babySpider);
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
