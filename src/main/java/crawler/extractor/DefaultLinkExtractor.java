package crawler.extractor;

import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.metadata.Metadata;

import org.xml.sax.ContentHandler;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import crawler.extractor.util.Verify;

class DefaultLinkExtractor extends AbstractLinkExtractor {

    public DefaultLinkExtractor() {}

    // TODO take care of HTTPs status codes (stop crawling) to respect Robots.txt and Crawl Delay Directives
    // TODO this finds clickable links but what about links that aren't clickable?
    // TODO rotating IPs to avoid detection should each time the method is ran should it ahve a diff ip
    // TODO Respecting robots.txt
    // TODO keyword feature (filter links based on keywords)

    @Override
    public Set<String> getLinksFrom(String url) {

        if (Verify.isPdf(url)) {
            return parsePdf(url);
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
        }

        return Set.of();
    }

    public Set<String> parsePdf(String url) {
        try {
            URL entity = new URI(url).toURL();
            try (InputStream input = entity.openStream()) {

                if (!Verify.isPdf(input)) {
                    return Set.of();
                }

                ContentHandler handler = new BodyContentHandler();
                Metadata metadata = new Metadata();
                AutoDetectParser parser = new AutoDetectParser();

                parser.parse(input, handler, metadata);
                String content = handler.toString()
                    .replaceAll("\n|\r|\t", " ");

                return getAbsoluteUrls(content);

            } catch (IOException e) {
                System.err.println(e.getStackTrace());
                // could log it
            }

        } catch(Exception e) {
            System.err.println(e.getStackTrace());
            // could log it
        }

        return Set.of();
    }

    /*
     *Assumptions:
     * 
     * 1. This method only searches for absolute URLs because its purpose is to extract 
     * URLs from content that originates from PDF documents.
     * 
     * 2. Because this application is designed to parse research papers, which are 
     * typically transmitted as PDFs, urls inside these papers must be absolute URLs 
     * according to standards or best practices.
     * 
     * 3. Since PDFs don't have a useful base URL to resolve them, relative URLs are 
     * not handled. Therefore, in this context, any relative urls are deemed 
     * broken or meaningless.
     */
    private Set<String> getAbsoluteUrls(String string) {

        // regex to get absolute urls from string

        return Set.of();
    } 
}