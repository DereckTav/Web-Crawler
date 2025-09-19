package crawler.extractor.web.robots;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

//This class should be immutable by default
// for creating test cases use : https://scrape.do/blog/robots-txt/
//robots.txt speciifes crawldelay
// if none then ... (1 sec delay) (maybe get rid of parralel parsing rule)


public class RobotsRules {
    // metadate of rules
        // allowed : bool
        // disallowed : bool
        // delay crawl (float) : 0 by default for TO_ROBOTS_RULES 
        // if rules doesn't ahve delay crawl it will be initizalized to 0
        // queue will make sure that we add 1 sec delay for links iwth 0
            // would it be ok to still use headless browser for links with no crawl?
            // is there a chance of ip ban?
                // could prop just switch proxies and keep headless browser saving time
                

    // interpretations
        //generate evrything that is disallwed
        //don't need to generate eveyrthing allowed because it should interpret what to do
        // if disallowed blank or none go for everything


    //TODO implement this with InputStreams
    
    //what is sitemap?
    // any robots not utf-8 ignore return rule that indicates to ignore domain

    private Optional<String[]> dissallowed; // basically if everything is allowed no reason to have dissallowed
    
    private int crawlDelay;

    private RobotsRules() {}

    private static final RobotsRules DEFAULT_RULES = 
        RobotsRules.of(
            "" //TODO replace with a supplier // also replace empty string with default params for RobotsRules
        );

    public static final RobotsRules DISSALOW_ALL = 
        RobotsRules.of(
            "" //make so can't crawl // basically disallow: /
        );

    public static final RobotsRules RETRY = 
        RobotsRules.of(
            "" //make so has default crawl delay, and delay can increase exp
        );
        // This should have a method
        // increase crawl time exp if instance of RETRY

    public static RobotsRules of() {
        return DEFAULT_RULES;
    }

    public static RobotsRules of(InputStream robotsTxtContent) {
        return parse(robotsTxtContent);
    }

    public static RobotsRules of(String robotsTxtContent) {
        // verify if correct content. if not throw error.
        // creates robots rules and returns it.
        return parse(robotsTxtContent);
    }   

    public static RobotsRules parse(InputStream robotsTxtContent) {
        HashMap<String, List<String>> connection = new HashMap<>();

        List<String> disallow = new ArrayList<>();
        List<String> allow = new ArrayList();

        // find approriate user agent
        // if cralwer delay is empty?

        return new RobotsRules();
    }

    // Builder would make this easier



    public static RobotsRules parse(String robotsTxtContent) {

        // find approriate user agent
        // if cralwer delay is empty?

        // this is in the case you have a string
        // then you would parse the whole thing set values for robots and return it

        return new RobotsRules();
    }

    /* 
     * ignore #
     * user-agent substring-based, case-insensitive, and longest-match win
     * 
     * syntax is User-agent: <name>
     * target all crawlers with *
     * also should look for crawler for User-agent: researchBot (look for anysupstring of this)
     * if it contains  research ....
     * it will obey those rules. If not, it will fall back to a generic User-agent: * (asterisk) section if present.
     * 
     * there can be multiple useragent
     * 
     *  A crawler will use the first matching block (usually the most specific match)
     * 
     *  
     * A Disallow directive tells the crawler not to visit any URL that starts with the specified path. The syntax i
     *  always relative to the site’s roo
     * 
     * /secret
     * block any url beginning with secret
     * 
     * /secret/page
     * only block urls beginning with that
     * any ohter /secret is available
     * 
     * Disallow: 
     *  no pages are disallowed
     * 
     * Disallow: /
     * 
     *  can't access anything
     *
     * If a user-agent section has no Disallow lines at all, it implicitly means “allow everything.” 
     * Some sites use an explicit Allow: / for clarity (covered below), but it isn’t necessary. Conversely, 
     * if a section has no Allow but has some Disallow rules, anything not disallowed is allowed.
     * 
     * User-agent: *
        Disallow: /public/
        Allow: /public/index.html
        This would block everything under /public/ except the /public/index.html page 
        (which is explicitly allowed). When crawlers interpret this, they usually give precedence to the more specific rule. 
        In this case, the allow rule for the specific file overrides the blanket disallow for that directory.
     * 
     * 
     * Multiple Allow/Disallow Rules: When there are potentially conflicting rules, the usual resolution (per Google and the standard) is:

        The most specific rule (the one with longest path that matches the URL) wins.
        For example, given Disallow: /public/ and Allow: /public/index.html, 
        the URL /public/index.html is allowed (because the allow rule is a more specific match to that URL). 
        Another URL /public/contact.html would be disallowed (no specific allow for it, so the /public/ disallow takes effect).
        If two rules have equal specificity, a crawler might choose the deny over allow as a precaution. But in practice, avoid ambiguous cases.

     *
     *  Robots.txt rules can include wildcard characters to match patterns of URLs. The two special tokens you’ll see are * (asterisk) and $
     * 
     * matches any sequence of characters. This can be used in the middle or end of a path to indicate “anything goes here.” For example, Disallow: 
     */

     // /blog/*/comments 
     // would disallow URLs like /blog/2023/post1/comments and /blog/draft/comments – essentially any URL that has “/blog/ something /comments”. Similarly, Disallow: /*.php would disallow all URLs containing “.php”
     
     /* 
      * $ (dollar sign) – matches the end of the URL. It’s used to indicate “ends with…”. 
      For example, Disallow: /*.pdf$ means disallow any URL that ends in “.pdf” (likely to block PDF files from being crawled)​.
       The $ is typically used in conjunction with a preceding pattern to specify file extensions or exact endings. 
       Another example: Disallow: /temp$ would disallow the URL that exactly ends with “/temp” (but not /temp/anything because that has additional characters after “temp”). 
       In practice, $ is mostly used for file-type blocking as shown.

       Disallow: /*? – disallow any URL containing a ? (query string). This is a way to block all query parameters (perhaps to avoid crawling dynamic URLs or duplicates). For instance, Disallow: /*?session= would block any URL that has “?session=” in it.
        Disallow: /*.jpg$ – block all JPEG image URLs. A site might do this if they want to prevent image search engines from indexing their photos, for example.
        Allow: /*.css$ – allow all .css files to be crawled (maybe amidst a disallow of everything else). This could be used if the site wants search engines to fetch CSS (for rendering), even if much of the site is disallowed.
        Disallow: /backup/* – disallow any URL under /backup/ (and the * means anything after /backup/ is covered, effectively the same as Disallow: /backup/ with a trailing slash in this case).
     */

     //Apache HttpClient to fetch and parse the robots.txt file. This approach is useful if you only need basic functionality like checking Allow and Disallow rules.

    //Crawl-delay is a directive used to throttle the crawl rate of bots. It is not universally supported by all crawlers, but many honor it. The syntax is:

    // Crawl-delay: <number> seconds
    // 0.5” for half-second) – not all parsers handle decimals consistently, so usually integers are used
    //User-agent lines in one block), a crawl-delay in that block applies to all of them.
    /*
     * If you’re implementing this in a scraper, it means after each request, you should sleep for that many seconds before the next request to that same site. 
     * This is especially relevant if you plan to bombard a site with many requests – a crawl-delay is the site’s way of saying “please go slow.
     */
     
}
