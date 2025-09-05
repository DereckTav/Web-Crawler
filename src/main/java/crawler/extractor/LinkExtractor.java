package crawler.extractor;

import java.util.Set;

interface LinkExtractor {
    Set<String> getLinksFrom(String url);
}

