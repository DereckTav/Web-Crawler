package hypercrawl.test;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * enable use of thread in vs code
 * 
 */
public class App {
    
    private static Queue queue;
    private static Crawled crawled;
                
    public App() {
        queue = new Queue();
        crawled = new Crawled();
    }

    public void getHyperLinkOf(String rootURL, CountDownLatch latch) {
        // Apache Commons UrlValidator to ensure links are valid before processing them.
        UrlValidator urlValidator = new UrlValidator();

        if(!urlValidator.isValid(rootURL)) {
            System.out.println("invalid link");
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Spider motherSpider = new Spider(rootURL, crawled, queue, executorService, latch);
        executorService.submit(motherSpider);
    }

    public Set<String> returnLinks() {
        return crawled.getLinks();
    }
    
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
