/**
 * LinkFinder is a class that retrieves and processes all the valid links (absolute URLs) from a given webpage.
 * It extracts both absolute and relative links, resolves them into absolute URLs, and returns a set of valid links.
 * The class uses Playwright to interact with a web browser and retrieve links from the page.
 * 
 * The main functionalities include:
 * - Extracting links from the page
 * - Resolving relative links to absolute URLs
 * - Validating the extracted links based on certain criteria
 * 
 * This class is designed to be used for web scraping or crawling purposes.
 */
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
     * Constructor for creating an instance of LinkFinder.
     */
    public LinkFinder() {
    }

    /**
     * Retrieves a set of links (absolute URLs) from the given webpage.
     * 
     * @param pageURL the URL of the page to extract links from
     * @return a Set<String> containing all the valid absolute links found on the page
     */
    public Set<String> getLinks(String pageURL) {
        // Fetch all the links from the provided page URL
        Set<String> links = pageLinks(pageURL);
        return links;
    }

    /**
     * Fetches all the links from the given page URL and returns them as absolute URLs.
     * 
     * @param pageURL the URL of the page to extract links from
     * @return a Set<String> of absolute URLs found on the page
     */
    private Set<String> pageLinks(String pageURL) {
        // Extract the base URL from the provided page URL
        String baseURL = getBaseUrl(pageURL);
        Set<String> links = new HashSet<>();

        // If base URL is empty, return an empty set
        if (baseURL.isBlank()) {
            return links;
        }

        try (Playwright playwright = Playwright.create()) {
            // Launch the browser using Playwright
            Browser browser = playwright.chromium().launch();
            Browser.NewContextOptions contextOptions = new Browser.NewContextOptions() // Set the User-Agent when creating a context
                    .setUserAgent("practice webcrawler project");

            // Create a new context and page in the browser
            BrowserContext context = browser.newContext(contextOptions);
            Page page = context.newPage();

             // Navigate to the page and wait for up to 10 seconds for it to load
            page.navigate(pageURL, new Page.NavigateOptions().setTimeout(10000));
                                                                                            
            // Extract all the links from the page using Playwright
            @SuppressWarnings("unchecked")
            List<String> relativeLinks = (List<String>) page.locator("a")
                    .evaluateAll("list => list.map(element => element.href)"); // Extracts href attributes of all <a> tags

            // todo take care of HTTPs status codes (stop crawling) to respect Robots.txt and Crawl Delay Directives

            // Process the relative links to convert them to absolute URLs and add them to the set
            processRelativeLinks(baseURL, links, relativeLinks);

            browser.close();
            return links;

        } catch (Exception e) {
            // In case of any error, return the current set of links (which might be empty)
            return links;
        }
    }

    /**
     * Processes the list of relative links, resolves them to absolute URLs, and adds them to the set of links.
     * 
     * @param baseURL the base URL to resolve relative URLs against
     * @param links the set of links to add the resolved absolute URLs to
     * @param relativeLinks the list of relative links to process
     */
    private void processRelativeLinks(String baseURL, Set<String> links, List<String> relativeLinks) {
        for (String relativeLink : relativeLinks) { // resolve the relative links to absolute links and add absolute links to set
            
            // Skip invalid or already added links                                       
            if (relativeLink == null || relativeLink.trim().isEmpty() && links.contains(relativeLink)) {
                continue;
            }

            try {
                if (isAbsoluteURL(relativeLink)) { // already an absolute url
                    if (isValid(relativeLink)) {
                        String absoluteURL = relativeLink;
                        links.add(absoluteURL);
                    }

                } else if (relativeLink.startsWith("//")) { // link starts wtih "//" i,e //example.com
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
    }

    /**
     * Extracts the base URL from a given URL (scheme and host).
     * 
     * @param link the full URL
     * @return the base URL (scheme and host)
     */
    private String getBaseUrl(String link) {
        try {
            URI uri = new URI(link);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();

            // If the port is not specified, return only the scheme and host
            if (port == -1) {
                return scheme + "://" + host; // No port
            } else {
                return scheme + "://" + host + ":" + port; // Include port
            }
        } catch (Exception e) {
            // Return an empty string if the URI parsing fails
            return "";
        }
    }

    /**
     * Resolves a relative URL to an absolute URL using the provided base URL.
     * 
     * @param baseUrl the base URL to resolve against
     * @param relativeUrl the relative URL to resolve
     * @return the absolute URL
     * @throws Exception if the URL resolution fails
     */
    private String resolveRelativeUrl(String baseUrl, String relativeUrl) throws Exception {
        URI baseUri = new URI(baseUrl);
        URI resolvedUri = baseUri.resolve(relativeUrl);
        return resolvedUri.toString();
    }

    /**
     * Checks if the provided link is an absolute URL (starts with "http://" or "https://").
     * 
     * @param link the link to check
     * @return true if the link is an absolute URL, false otherwise
     */
    private boolean isAbsoluteURL(String link) {
        return link.startsWith("http://") || link.startsWith("https://");
    }

    /**
     * Checks if the provided link is valid. A valid link is not blocked or of an invalid type.
     * 
     * @param link the link to check
     * @return true if the link is valid, false otherwise
     */
    private boolean isValid(String link) {
        if (checks(link)) {
            return true;
        }

        return false;
    }

    /**
     * Performs various checks to validate a link.
     * 
     * @param link the link to check
     * @return true if the link passes the checks, false otherwise
     */
    private boolean checks(String link) {
        // Exclude links with certain characteristics (e.g., anchor links, mailto links, etc.)
        return !link.startsWith("#")
                && !link.startsWith("mailto:")
                && !link.contains("javascript:")
                && !link.matches(".*\\.(jpg|jpeg|png|gif|pdf|docx|zip|mp4|avi|svg|css|js|ico)$");
    }
}
