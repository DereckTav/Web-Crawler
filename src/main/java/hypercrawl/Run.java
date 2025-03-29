/**
 * The Run class serves as the entry point for the hypercrawl application.
 * It starts the crawling process by invoking methods from the App class to 
 * get hyperlinks from a given root URL. The crawling is performed concurrently 
 * using multiple threads.
 * 
 * Features:
 * - Initializes the App class and starts crawling from a given root URL.
 * - Waits for the crawling process to complete using a CountDownLatch.
 * - Prints the discovered links after crawling is finished.
 * 
 * Usage:
 * - Simply run the main method to start the crawling process on the specified root URL.
 */
package hypercrawl;

// import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.time.Instant;
import java.time.Duration;

public class Run {
    public static void main(String[] args) {
        // Initialize the App class which handles the crawling process
        Instant start = Instant.now();
        App HyperCrawler = new App();

        // Define the root URL for crawling (could be taken from user input)
        String url = "https://web-scraping.dev/";

        // System.out.print("get Hyperlinks for: ");
        // Scanner scanner = new Scanner(System.in);
        // String url = scanner.nextLine().strip();

        // CountDownLatch used to wait for all crawling tasks to finish
        CountDownLatch latch = new CountDownLatch(5);

        // Start the crawling process by passing the root URL and latch to the App
        //numOfSpiders is number of threads
        //make sure to not use more threads then your pc has.
            // i.e my pc has 6 cores so I have 12 threads
            // so max of threads I would use is 9;
        HyperCrawler.getHyperLinkOf(url, latch, 9);

        // Retrieve the set of discovered links after crawling
        Set<String> links = HyperCrawler.returnLinks();

        try {
            // Wait for the CountDownLatch to reach zero, ensuring crawling tasks are completed
            latch.await();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        }

        Instant end = Instant.now();
        Duration runtime = Duration.between(start, end);
        
        // After crawling is finished, print the discovered links
        System.out.println("-----------------LINKS---------------------");
        for (String link : links) {
            System.out.println(link);
        }

        System.out.println("Execution time: " + runtime.toMillis() + " milliseconds");

        // ways to make this faster is by limiting crawl depths.
        // also not caring about which links are processed
        // just take the ones that are discovered
    }
}
