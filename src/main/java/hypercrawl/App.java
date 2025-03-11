/**
 * The App class provides functionality for crawling hyperlinks from a given root URL.
 * It leverages multithreading to efficiently crawl and extract links using a thread pool.
 * The class ensures that only valid URLs are processed and supports concurrent processing
 * for faster crawling.
 *
 * Features:
 * - Validates the root URL using Apache Commons UrlValidator before processing.
 * - Uses a fixed thread pool of 5 threads to process the URLs concurrently.
 * - Crawls links starting from the root URL and adds them to a crawled collection.
 * - Returns a set of all the crawled links.
 *
 * Dependencies:
 * - Apache Commons Validator (UrlValidator)
 * - ExecutorService for handling thread management
 * - CountDownLatch for synchronizing multiple threads
 *
 * Usage:
 * 1. Instantiate the App class.
 * 2. Call the getHyperLinkOf() method with a root URL to start the crawling process.
 * 3. Use returnLinks() to retrieve the set of crawled URLs after the process completes.
 * 
 * Example:
 * App app = new App();
 * app.getHyperLinkOf("https://example.com", new CountDownLatch(5));
 * Set<String> links = app.returnLinks();
 */
package hypercrawl;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.validator.routines.UrlValidator;


/**
 * This class enables multithreading to crawl and extract hyperlinks from a given root URL.
 * It uses a thread pool to process the URLs concurrently and ensures that only valid URLs are processed.
 */
public class App {
    
    // Declaring static variables for Queue and Crawled objects to store URLs to be processed and crawled.
    private static Queue queue;
    private static Crawled crawled;
       
    /**
     * Constructor to initialize Queue and Crawled objects.
     */
    public App() {
        queue = new Queue();
        crawled = new Crawled();
    }

    /**
     * This method validates the root URL and processes it in a separate thread using ExecutorService.
     * It creates a Spider object for the root URL and submits it for execution.
     * 
     * @param rootURL The starting URL to crawl
     * @param latch The latch to wait for the threads to complete
     */
    public void getHyperLinkOf(String rootURL, CountDownLatch latch) {
        // Apache Commons UrlValidator to ensure links are valid before processing them.
        UrlValidator urlValidator = new UrlValidator();

        // If the URL is not valid, print a message and return.
        if(!urlValidator.isValid(rootURL)) {
            System.out.println("invalid link");
            return;
        }

        // Create an ExecutorService with a fixed thread pool of 5 threads for concurrent processing.
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Create a new Spider object that will crawl the root URL and its links.
        Spider motherSpider = new Spider(rootURL, crawled, queue, executorService, latch);

        // Submit the Spider task to the ExecutorService for processing
        executorService.submit(motherSpider);
    }

    /**
     * This method returns the set of links that have been crawled.
     * 
     * @return A set containing all the crawled links.
     */
    public Set<String> returnLinks() {
        return crawled.getLinks();
    }
    
    /**
     * The main method initializes the application, prompts the user for a root URL,
     * validates the URL, and starts the crawling process using multithreading.
     */
    public static void main(String args[]) {
        new App();
        Scanner scan = new Scanner(System.in);

        System.out.print("get Hyperlinks for: ");
        String rootURL = scan.nextLine().strip();

        //https://web-scraping.dev/

        // Apache Commons UrlValidator to ensure links are valid before processing them.
        UrlValidator urlValidator = new UrlValidator();

        if(!urlValidator.isValid(rootURL)) {
            System.out.println("invalid link");
            return;
        }

        // thread process
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        CountDownLatch latch = new CountDownLatch(5);
        Spider motherSpider = new Spider(rootURL, crawled, queue, executorService, latch);
        executorService.submit(motherSpider);

        // return proccessed in crawled
    }
}
