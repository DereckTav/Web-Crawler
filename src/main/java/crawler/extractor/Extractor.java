package crawler.extractor;

import java.util.HashSet;
import java.util.Set;

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

    public static Set<String> extractURLsFrom(String URL) {
        
        if (Verify.isBlank(URL) || !Verify.isValid(URL)) {
            return new HashSet<>();
        }



        // pass it on to extractors
        // return 

        throw new UnsupportedOperationException("Unimplemented method 'getHyperlinksFrom'");
    }

}
