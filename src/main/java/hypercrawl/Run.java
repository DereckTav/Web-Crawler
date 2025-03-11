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

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Run {
    public static void main(String[] args) {
        // Initialize the App class which handles the crawling process
        App HyperCrawler = new App();

        // Define the root URL for crawling (could be taken from user input)
        // String url = "https://web-scraping.dev/";

        System.out.print("get Hyperlinks for: ");
        Scanner scanner = new Scanner(System.in);
        String url = scanner.nextLine().strip();

        // CountDownLatch used to wait for all crawling tasks to finish
        CountDownLatch latch = new CountDownLatch(5);

        // Start the crawling process by passing the root URL and latch to the App
        HyperCrawler.getHyperLinkOf(url, latch);

        // Retrieve the set of discovered links after crawling
        Set<String> links = HyperCrawler.returnLinks();

        try {
            // Wait for the CountDownLatch to reach zero, ensuring crawling tasks are completed
            latch.await();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        }
        
        // After crawling is finished, print the discovered links
        System.out.println("-----------------LINKS---------------------");
        for (String link : links) {
            System.out.println(link);
        }

        // ways to make this faster is by limiting crawl depths.
        // also not caring about which links are processed
        // just take the ones that are discovered
    }
}
