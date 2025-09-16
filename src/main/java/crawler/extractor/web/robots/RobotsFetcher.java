package crawler.extractor.web.robots;

import crawler.http.Client;

import java.net.http.HttpClient;

// TODO take care of HTTPs status codes (stop crawling) to respect Robots.txt and Crawl Delay Directives
        // create rule to stop crawling site for ... time
        // or edit rules in cache
        // if rule not in cache make new rule

// fetching logic


public class RobotsFetcher {

    private static final HttpClient client = Client.INSTANCE.getHttpClient();

    private RobotsFetcher() {};

    public static RobotsRules fetch(String domain) {
        
        return RobotsRules.of();
    };

    // class that will cache the rules
    // rules will be created in this class
    // class will setup connection with domen/robots.txt
    // interpret http response
        // response == 200 robots.txt exists
        // response == 404 not found doesn't exists “no rules to worry about.” (However, it’s still good to crawl responsibly even if there’s no robots.txt.)
        //response == 403 do not crawl
        // response == 301 a site might redirect the robots.txt URL (301/302 redirect). If so, follow the redirect (most HTTP clients do this automatically) to get the content.
        // If the request times out or the site is down: treat it as empty file. if site is down get rid of return that (let it be known that site is down)
        // give rules for given code 
    // robots usually utf-8 txt 


    //method getRobotFrome(String Domain)
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
