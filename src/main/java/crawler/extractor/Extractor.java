package crawler.extractor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import crawler.extractor.link.DefaultLinkExtractor;
import crawler.extractor.link.LinkExtractor;
import crawler.extractor.util.Verify;

public class Extractor {

    private static LinkExtractor extractor = new DefaultLinkExtractor();

    private Extractor() {}

    /*
     * className needs to be the name of a class that implements LinkExtractor
     * or extends AbstractLinkExtractor    
     */
    //for testing purposes (to switch implementation of extractor)
    @SuppressWarnings("unchecked")
    protected static void of(String className) {
        try {
            Class<? extends LinkExtractor> linkExtractor =
                (Class<? extends LinkExtractor>) Class.forName(className);
            extractor = linkExtractor.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to load extractor: " + className, e);
        }
    }

    public static Set<String> extractUrlsFrom(String url) {
        url = url.trim();
        
        //TODO Check if we can remove this isValid

        if (Verify.isBlank(url) || !Verify.isValid(url)) {
            return Collections.emptySet();
        }

        return extractor.getLinksFrom(url);
    }

    // for future implementation of ranking
    public static HashMap<String,String> extractWithContext(String url) {
        throw new UnsupportedOperationException("not implemented");
    }
}
