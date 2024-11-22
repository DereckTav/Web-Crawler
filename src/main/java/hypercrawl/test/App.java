package hypercrawl.test;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * enable use of thread in vs code
 * 
 */
public class App {
    
    private static Queue queue;
    private static LinkFinder linkFinder;
    private static Crawled crawled;
                
    public App() {
        queue = new Queue();
        linkFinder = new LinkFinder();
        crawled = new Crawled();
    }
    
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        System.out.println("give URL you which to get Hyperlinks for:");
        String url = scan.nextLine();

        // Apache Commons UrlValidator to ensure links are valid before processing them.
        String[] allowedSchemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(allowedSchemes, UrlValidator.ALLOW_LOCAL_URLS);

        if(!urlValidator.isValid(url)) {
            System.out.println("invalid link");
            return;
        }

        // thread process
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Spider spider = new Spider(url, linkFinder, crawled, queue);

        while (!queue.isEmpty() || !crawled.isFull()) {
            executorService.submit(() -> spider.crawlPage());
        }
        

        executorService.shutdown();

        // return proccessed in crawled
    }

    // add block list

    //return links

    //BFS part
    // remember clear features
}
