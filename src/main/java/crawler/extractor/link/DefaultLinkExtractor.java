package crawler.extractor.link;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

import crawler.urls.detection.UrlDetector;
import crawler.urls.detection.UrlDetectorOptions;
import crawler.urls.Url;

public class DefaultLinkExtractor implements LinkExtractor {

    public DefaultLinkExtractor() {}

    // TODO make crawler work with pdf refrences - search it up and get link
    // TODO If website is in list of reputable research sites and links list 
    // has .pdf url get rid of all other urls and just return only pdf links. If any. 
    // aslo depending on site add cite as with links


    // this class will be repuprosed as controller
    // respect rules

    @Override
    public Set<String> getLinksFrom(String url) {
        throw new UnsupportedOperationException("not implemented");

        // since urls are strings and we need domain 
        // we will parse the first few chars until .com (util function TODO)
            // for this use Pattern.compile for performance

        // use RobotsCache.getOrLoad(domain)
        // will return robotsRules

        //verify that url can be parsed
            // if not return empty set

        // if robotsRules has delay
            // Throw exception retry url later (this will let it be know that url can be parsed but would have to try later)


        // if no exception is thrown
        // go ahead an parse

        // if (Verify.isPdf(url)) {
        //     return parsePdf(url);
        // }

        // return parseWebsite(url);

    }

    //TODO test for linkextractors should make sure that it respects these caching things.

}