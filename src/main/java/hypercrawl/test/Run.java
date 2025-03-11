package hypercrawl.test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Run {
    public static void main(String[] args) {
        App HyperCrawler = new App();

        String url = "https://web-scraping.dev/";
        CountDownLatch latch = new CountDownLatch(5);

        // System.out.print("get Hyperlinks for: ");
        // String rootURL = scan.nextLine().strip();

        HyperCrawler.getHyperLinkOf(url, latch);
        Set<String> links = HyperCrawler.returnLinks();

        try {
            latch.await();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        }
        
        System.out.println("-----------------LINKS---------------------");
        for (String link : links) {
            System.out.println(link);
        }

        // ways to make this faster is by limiting crawl depths.
    }
}
