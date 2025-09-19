package crawler.extractor.web.robots;

import crawler.extractor.exceptions.RobotsTxtUnavailableException;
import crawler.http.Client;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

// TODO take care of HTTPs status codes (stop crawling) to respect Robots.txt and Crawl Delay Directives
        // create rule to stop crawling site for ... time
        // or edit rules in cache
        // if rule not in cache make new rule

// class that will cache the rules
// rules will be created in this class
// class will setup connection with domen/robots.txt

public class RobotsFetcher {

    private static final HttpClient client = Client.INSTANCE.getHttpClient();

    //TODO datastructure to keep track of retries for each domain
    //make sure to delete after

    private RobotsFetcher() {};
 
    public static RobotsRules fetch(String domain) throws RobotsTxtUnavailableException{
        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(domain + "/robots.txt"))
                                .timeout(Duration.ofSeconds(5))
                                .GET()
                                .build();

        try {
            HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());

            int statusCode = response.statusCode();
            
            switch (statusCode) {
                case 200:
                    try (InputStream robotsTxt = response.body()) {
                        return RobotsRules.of(robotsTxt);
                    }
                case 404:
                    return RobotsRules.of();
                default:
                    //TODO log it
                    return RobotsRules.DISSALOW_ALL;
            }

            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RobotsTxtUnavailableException("Failed to retrieve robots.txt for " + domain + e);
            // due to timeout cancelations or timeout
            // consider retrying later (give a max of 3 retries)
                // make datastructure to hold retry counts
                    //log if after 3 retries it doesn't work
                        // increase wait time for retires exponentially
                            //in this case update cacheruels for that domain
        } catch (Exception e) {
            throw new RobotsTxtUnavailableException("Failed to retrieve robots.txt for " + domain + e);
            //log because this could be due to 300 redirects (or sum niche status code).
                    /* if so : Check the status code of the response.
                     * If the code indicates a redirect, grab the new URI from the response headers.
                     * If the new URI is relative then resolve it against the request URI.
                     * Send a new request.
                     */


            // If the request times out or the site is down: treat it as empty file (no disallows). (preferably in switch case!) 
            // if site is down get rid of return that (let it be known that site is down ( given that site is down try again later (try to get the robots again later)))
        }

        //questions what if connection timesout?
        //what would robotstxtunavailableException intail for domains
        //if httpclient that send gives interrupeted exception or io exception does that mean i should try later
    };
}


//TODO : important test case

// go to robotsfetcher (Can't have lazy fetcher becuase if we have two links one link says to crawl and it is blocked (because of delay) but the another link (from same domain) is sent we broke the rule)
            // checks robotsfetcher.fetch
                // if url is domain do regurler fetch (will never be used just will be dead code)
                // if not not domain do special fetch where (retrive domain get robots)
                    // give to robots rules
                        // robot rules can have function (wrapper function) that checks weather current link can parse
                            //with out creating rules
                                // if it can create ruels and return it
                                    // if it can't be parse return exception
