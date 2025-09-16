package crawler.extractor.link.parser.web;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import crawler.extractor.link.parser.Parser;
import crawler.extractor.util.Verify;

public class WebParser implements Parser {

    //TODO headless browsers make multiple request which breaks robots.txt
        // solution make request to get html and block all other request for loading data
        // preced to put those apis for loading data back on queue to reconstruct page
            // Prob is a better solution to this would check after finishing implementing robots

    //TODO respect rules (rate-limit themselves and respect crawl-delay heuristics)

    //should I switch everyting from String to URI or urls.URL
    // I do getBase() so is that teh domain?
    // I need the host

    // TODO take care of HTTPs status codes (Have to get them and verify it  then rule will be made stop crawling)
        // to respect Robots.txt and Crawl Delay Directives
        // create rule to stop crawling site for ... time


    // TODO  //note: solution doesn't take care of links with flags
            //so two links could lead to same page but becuase of flags
            //they will be in same set

    // TODO rotating IPs(proxy) to avoid detection should each time the method is ran should it ahve a diff ip
        // might have to be done in sub classes 

    // respect rules


    // NOTE: this finds clickable links and doesn't consider links in text
    public Set<String> parseWebsite(String url) {
        String baseUrl = getBase(url);
        
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Browser.NewContextOptions contextOptions = new Browser.NewContextOptions() // Set the User-Agent when creating a context
                    .setUserAgent("researchBot");

            BrowserContext context = browser.newContext(contextOptions);
            Page page = context.newPage();

            // Navigate to the page and wait for up to 10 seconds for it to load
            page.navigate(url, new Page.NavigateOptions()
                .setTimeout(10000));
                                                                                            
            @SuppressWarnings("unchecked")
            List<String> links = (List<String>) page.locator("a")
                .evaluateAll("list => list.map(element => element.href)"); 

            Set<String> urls = resolveRelative(links, baseUrl);

            browser.close();
            return urls;

        } catch (Exception e) {
            System.err.println(e);
            //TODO log it
        }

        return Collections.emptySet();
    }
}
