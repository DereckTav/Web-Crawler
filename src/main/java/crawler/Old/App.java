package crawler.Old;
// /**
//  * The App class provides functionality for crawling hyperlinks from a given root URL.
//  * It leverages multithreading to efficiently crawl and extract links using a thread pool.
//  * The class ensures that only valid URLs are processed and supports concurrent processing
//  * for faster crawling.
//  *
//  * Features:
//  * - Validates the root URL using Apache Commons UrlValidator before processing.
//  * - Uses a fixed thread pool of 5 threads to process the URLs concurrently.
//  * - Crawls links starting from the root URL and adds them to a crawled collection.
//  * - Returns a set of all the crawled links.
//  *
//  * Dependencies:
//  * - Apache Commons Validator (UrlValidator)
//  * - ExecutorService for handling thread management
//  * - CountDownLatch for synchronizing multiple threads
//  *
//  * Usage:
//  * 1. Instantiate the App class.
//  * 2. Call the getHyperLinkOf() method with a root URL to start the crawling process.
//  * 3. Use returnLinks() to retrieve the set of crawled URLs after the process completes.
//  * 
//  * Example:
//  * App app = new App();
//  * app.getHyperLinkOf("https://example.com", new CountDownLatch(5));
//  * Set<String> links = app.returnLinks();
//  */
// package hypercrawl;

// import java.util.concurrent.LinkedBlockingQueue;
// import java.time.Duration;
// import java.time.Instant;
// import java.util.Queue;
// import java.util.Scanner;
// import java.util.Set;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

// import org.apache.commons.validator.routines.UrlValidator;

// //TODO linkfinder takes in keywords, since linkfinder is a singleton
// //should applicatoin be a singleton?
// //

// /**
//  * This class enables multithreading to crawl and extract hyperlinks from a given root URL.
//  * It uses a thread pool to process the URLs concurrently and ensures that only valid URLs are processed.
//  */
// public class App {
    
//     private static Queue queue;
//     private static VisitedUrls visitedUrls;

//     /*  
//      * have to use playwright
//      * it has an async feature so thats good
//      * mostly what I would really on is making multiple pages (that is limited tho because of memory)
//      * have to figure out a way for the browser and page to be concurrently used by threads
//      * maybe async is very useful here
//      */


    
//     /**
//      * Constructor to initialize Queue and Crawled objects.
//      */
//     public App() {
//         queue = new LinkedBlockingQueue<String>();
//         visitedUrls = new VisitedUrls(200);
//     }

//     /**
//      * This method validates the root URL and processes it in a separate thread using ExecutorService.
//      * It creates a Spider object for the root URL and submits it for execution.
//      * 
//      * @param rootURL The starting URL to crawl
//      * @param latch The latch to wait for the threads to complete
//      */
//     public void getHyperLinkOf(String rootURL, int numOfSpiders) {
//         // Apache Commons UrlValidator to ensure links are valid before processing them.
//         UrlValidator urlValidator = new UrlValidator();

//         // If the URL is not valid, print a message and return.
//         if(!urlValidator.isValid(rootURL)) {
//             System.out.println("invalid link");
//             return;
//         }

//         // Create an ExecutorService with a fixed thread pool of 5 threads for concurrent processing.
//         ExecutorService executorService = Executors.newFixedThreadPool(numOfSpiders);

//         // Create a new Spider object that will crawl the root URL and its links.
//         // Spider motherSpider = new Spider(rootURL, visitedUrls, queue, executorService, numOfSpiders);

//         // Submit the Spider task to the ExecutorService for processing
//         // executorService.submit(motherSpider);
//     }

//     /**
//      * This method returns the set of links that have been crawled.
//      * 
//      * @return A set containing all the crawled links.
//      */
//     public Set<String> returnLinks() {
//         return visitedUrls.getvisited();
//     } 
    
//     /**
//      * The main method initializes the application, prompts the user for a root URL,
//      * validates the URL, and starts the crawling process using multithreading.
//      */
//     public static void main(String args[]) {
//         //TODO make versio with completeable future




//         // // Initialize the App class which handles the crawling process
//         // Instant start = Instant.now();

//         // // Define the root URL for crawling (could be taken from user input)
//         // String url = "https://web-scraping.dev/";

//         // // System.out.print("get Hyperlinks for: ");
//         // // Scanner scanner = new Scanner(System.in);
//         // // String url = scanner.nextLine().strip();

//         // // CountDownLatch used to wait for all crawling tasks to finish
//         // CountDownLatch latch = new CountDownLatch(5);

//         // // Start the crawling process by passing the root URL and latch to the App
//         // //numOfSpiders is number of threads
//         // //make sure to not use more threads then your pc has.
//         //     // i.e my pc has 6 cores so I have 12 threads
//         //     // so max of threads I would use is 9;
//         // getHyperLinkOf(url, 9);

//         // // Retrieve the set of discovered links after crawling
//         // Set<String> links = returnLinks();

//         // try {
//         //     // Wait for the CountDownLatch to reach zero, ensuring crawling tasks are completed
//         //     latch.await();
//         // } catch (Exception e) {
//         //     System.out.println("exception:" + e);
//         // }

//         // Instant end = Instant.now();
//         // Duration runtime = Duration.between(start, end);
        
//         // // After crawling is finished, print the discovered links
//         // System.out.println("-----------------LINKS---------------------");
//         // for (String link : links) {
//         //     System.out.println(link);
//         // }

//         // System.out.println("Execution time: " + runtime.toMillis() + " milliseconds");

//         // // ways to make this faster is by limiting crawl depths.
//         // // also not caring about which links are processed
//         // // just take the ones that are discovered
//     }
// }
