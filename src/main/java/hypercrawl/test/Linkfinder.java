package hypercrawl.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class LinkFinder {

    private String sourceURL;
    
    /**
     * create instance of Linkfinder
     * @param sourceURL
     */
    public LinkFinder(String sourceURL) {
        this.sourceURL = sourceURL;
    }

     /**
      * gets the Links from the page,
      * and returns Set<String> of absolute strings
      * @param pageURL
      * @return Set<String> links
      */
    private Set<String> pageLinks(String pageURL) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate(pageURL);
            
            // extracts link from page
            List<String> relativelinks = page.locator("a").allTextContents();

            Set<String> links = new HashSet<>();

            // resolve the relative links to absolute links
            // add absolute links to set
            try {

                for (String relativeLink : relativelinks){
                    String relativeURL = resolveRelativeUrl(sourceURL, relativeLink);
                    links.add(relativeURL);
                }

            } catch (URISyntaxException e) {
                System.out.println("Invalid URI syntax: " + e.getMessage());
            }

            browser.close();

            return links;
        }
    }

    /**
     * return list of links
     * @return Set<String> links
     */
    public Set<String> getLinks(String pageURL) {
        Set<String> links = pageLinks(pageURL);
        return links;
    }

    /**
     * turn relative path to absolute path
     * @param baseUrl
     * @param relativeUrl
     * @return
     * @throws URISyntaxException
     */
    private static String resolveRelativeUrl(String baseUrl, String relativeUrl) throws URISyntaxException {
        URI baseUri = new URI(baseUrl);
        URI resolvedUri = baseUri.resolve(relativeUrl);
        return resolvedUri.toString();
    }
}
