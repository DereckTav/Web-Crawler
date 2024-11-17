package hypercrawl.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class Linkfinder {

    private String sourceURL;
    private Set<String> links;
    
    /**
     * create instance of Linkfinder
     * @param sourceURL
     */
    public Linkfinder(String sourceURL) {
        this.sourceURL = sourceURL;
        this.links = new HashSet<>();
    }

    /**
     * gets the Links from the page,
     * and adds it to list links,
     * ready to be returned
     * @param pageURL
     */
    public void pageLinks(String pageURL) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate(pageURL);
            
            // extraxts link from page
            List<String> links = page.locator("a").allTextContents();

            // resolve the relative links to absolute links
            // add absolute links to set
            try {

                for (String relativeLink : links){
                    String relativeURL = resolveRelativeUrl(sourceURL, relativeLink);
                    this.links.add(relativeURL);
                }

            } catch (URISyntaxException e) {
                System.out.println("Invalid URI syntax: " + e.getMessage());
            }

            browser.close();
        }
    }

    /**
     * return list of links
     * clear links after
     * @return Set<String> links
     */
    public Set<String> getLinks() {
        Set<String> setOfLinks = new HashSet<>(links);
        links.clear();
        return setOfLinks;
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
