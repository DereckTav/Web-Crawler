package crawler.extractor;

import java.util.List;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import crawler.extractor.util.Verify;

class DefaultLinkExtractor extends AbstractLinkExtractor {

    public DefaultLinkExtractor() {}

    // TODO implement pdf parser
    // TODO take care of HTTPs status codes (stop crawling) to respect Robots.txt and Crawl Delay Directives
    // TODO this finds clickable links but what about links that aren't clickable?
    // TODO rotating IPs to avoid detection should each time the method is ran should it ahve a diff ip
    // TODO Respecting robots.txt
    // TODO keyword feature (filter links based on keywords)

    @Override
    public Set<String> getLinksFrom(String url) {

        if (Verify.isPdf(url)) {
            return parsePdf();
        }

        return parseWebsite(url);
    }


    public Set<String> parseWebsite(String url) {
        String baseUrl = getBase(url);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Navigate to the page and wait for up to 10 seconds for it to load
            page.navigate(url, new Page.NavigateOptions().setTimeout(10000));
                                                                                            
            @SuppressWarnings("unchecked")
            List<String> links = (List<String>) page.locator("a")
                    .evaluateAll("list => list.map(element => element.href)"); 

            Set<String> urls = resolveRelative(links, baseUrl);

            browser.close();
            return urls;

        } catch (Exception e) {
            System.err.println(e);
        }

        return Set.of();
    }

    public Set<String> parsePdf() {
        // 1.download to disk then parse
            // negatives use disk space
            // would have to delet after
            // adds overhead (because of download)
            // would also need another library

        // important idea how does pdf work in websites?

        // 2. If possible to jsut instead pdf from website?
            // can just extract text from pdf right there
            // less overhead
        
        throw new UnsupportedOperationException("unimplemented");
    }
}