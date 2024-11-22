package hypercrawl.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class LinkFinder {
    /**
     * create instance of Linkfinder
     */
    public LinkFinder() {
    }

    /**
     * return list of links
     * 
     * @return Set<String> links
     */
    public Set<String> getLinks(String pageURL) {
        Set<String> links = pageLinks(pageURL);
        return links;
    }

    /**
     * gets the Links from the page,
     * and returns Set<String> of absolute strings
     * 
     * @param pageURL
     * @return Set<String> links
     */
    private Set<String> pageLinks(String pageURL) {
        String baseURL = getBaseUrl(pageURL);
        Set<String> links = new HashSet<>();

        if (baseURL.isBlank()) { // if it's not possible to get baseURL, skip
            return links;
        }

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Browser.NewContextOptions contextOptions = new Browser.NewContextOptions() // Set the User-Agent when
                                                                                       // creating a context
                    .setUserAgent("practice webcrawler project");

            BrowserContext context = browser.newContext(contextOptions);
            Page page = context.newPage();

            page.navigate(pageURL, new Page.NavigateOptions().setTimeout(10000)); // if page doesn't load in 10 seconds
                                                                                            // skip

            @SuppressWarnings("unchecked")
            List<String> relativeLinks = (List<String>) page.locator("a")
                    .evaluateAll("list => list.map(element => element.href)"); // extracts link from page

            // todo take care of HTTPs status codes (stop crawling)

            for (String relativeLink : relativeLinks) { // resolve the relative links to absolute links and add absolute
                                                        // links to set
                                                        
                if (relativeLink == null || relativeLink.trim().isEmpty() && links.contains(relativeLink)) {
                    continue;
                }

                try {
                    if (isAbsoluteURL(relativeLink)) { // already an absolute url
                        if (isValid(relativeLink)) {
                            String absoluteURL = relativeLink;
                            links.add(absoluteURL);
                        }

                    } else if (relativeLink.startsWith("//")) { // link starts wtih // i,e //example.com
                        String absoluteURL = "https:" + relativeLink;

                        if (isValid(absoluteURL)) {
                            links.add(absoluteURL);
                        }

                    } else if (isValid(relativeLink)) { // checks if it is a valid relative path
                        String absoluteURL = resolveRelativeUrl(baseURL, relativeLink);
                        links.add(absoluteURL);
                    }

                } catch (Exception e) {
                    System.err.println("Invalid URI syntax for link: " + relativeLink + ". Error: " + e.getMessage());
                }
            }

            browser.close();
            return links;

        } catch (Exception e) {
            return links;
        }
    }

    private String getBaseUrl(String link) {
        try {
            URI uri = new URI(link);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();

            if (port == -1) {
                return scheme + "://" + host; // No port
            } else {
                return scheme + "://" + host + ":" + port; // Include port
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * turn relative path to absolute path
     * 
     * @param baseUrl
     * @param relativeUrl
     * @return
     * @throws URISyntaxException
     */
    private String resolveRelativeUrl(String baseUrl, String relativeUrl) throws Exception {
        URI baseUri = new URI(baseUrl);
        URI resolvedUri = baseUri.resolve(relativeUrl);
        return resolvedUri.toString();
    }

    /**
     * Checks if link is an absolute link
     * 
     * @param link
     * @return
     */
    private boolean isAbsoluteURL(String link) {
        return link.startsWith("http://") || link.startsWith("https://");
    }

    /**
     * checks if link is valid.
     * 
     * @param link
     * @return true if link is valid, and false if not
     */
    private boolean isValid(String link) {
        if (checks(link)) {
            // create class Blocklist
            // add check for block list of links
            // remember robots.txt
            return true;
        }

        return false;
    }

    private boolean checks(String link) {
        return !link.startsWith("#")
                && !link.startsWith("mailto:")
                && !link.contains("javascript:")
                && !link.matches(".*\\.(jpg|jpeg|png|gif|pdf|docx|zip|mp4|avi|svg|css|js|ico)$");
    }
}
