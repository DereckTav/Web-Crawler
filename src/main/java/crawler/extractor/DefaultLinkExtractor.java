package crawler.extractor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import crawler.extractor.util.Verify;

class DefaultLinkExtractor extends AbstractLinkExtractor {

    // TODO create yaml
    // TODO take care of HTTPs status codes (stop crawling) to respect Robots.txt and Crawl Delay Directives
    // TODO Handling pagination (Many sites split content across multiple pages)
    // TODO this finds clickable links but what about links that aren't clickable?
    // TODO rotating IPs
    // Respecting robots.txt
    // Managing request throttling to avoid detection
    // TODO keyword feature (filter links based on keywords)
    // we can take this a step further and scrap the page start up a new thread to ai to validate if its relevant to the keyword
    // fix code and documentation
    // TODO add class that is an immutable empty set

    @Override
    public Set<String> getHyperlinksFrom(String url) {
        String baseUrl = getBase(url);

        if (Verify.isBlank(url)) {
            return new HashSet<>();
        }

        //TODO figure out wether pdf or web page
        //call methods regarding them
        //then parse
        
        throw new UnsupportedOperationException("not implemented");
    }


    public Set<String> parseWebsite(String url, String baseUrl) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Navigate to the page and wait for up to 10 seconds for it to load
            //TODO should add yaml for this: timeout
            page.navigate(url, new Page.NavigateOptions().setTimeout(10000));
                                                                                            
            @SuppressWarnings("unchecked")
            List<String> Links = (List<String>) page.locator("a")
                    .evaluateAll("list => list.map(element => element.href)"); 

            Set<String> urls = resolveRelative(Links, baseUrl);

            browser.close();
            return urls;

        } catch (Exception e) {
            System.err.println(e);
        }

        return new HashSet<>();

    }

    public Set<String> parsePdf() {
        throw new UnsupportedOperationException("unimplemented");
    }
}