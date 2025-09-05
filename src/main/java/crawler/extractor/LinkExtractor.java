package crawler.extractor;

import java.util.Set;

interface LinkExtractor {
    Set<String> getHyperlinksFrom(String url);
}

