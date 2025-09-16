package crawler.extractor.link;

import java.util.Set;

public interface LinkExtractor {
    Set<String> getLinksFrom(String url);
}

