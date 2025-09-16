package crawler.extractor.link.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import crawler.extractor.util.Verify;
import crawler.urls.Url;
import crawler.urls.detection.UrlDetector;
import crawler.urls.detection.UrlDetectorOptions;

public interface Parser {
     /**
     * Extracts the base URL from a given URL (scheme and host and port if given).
     * Will return an empty string if URI parsing fails
     * 
     * @param url 
     * @return the base URL (scheme and host)
     */
    default String getBase(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();

            // If the port is not specified, return only the scheme and host
            if (port == -1) {
                return scheme + "://" + host; // No port
            } else {
                return scheme + "://" + host + ":" + port; // Include port
            }
        } catch (URISyntaxException e) {
            // Return an empty string if the URI parsing fails
            //TODO add logger
            return "";
        }
    }    

    /**
     * Resolves a relative URL to an absolute URL using the provided base URL.
     * 
     * @param baseUrl the base URL e.g (example.com)
     * @param relativeUrl the relative URL e.g (/help/no-problem)
     * @return the absolute URL e.g(example.com/help/no-problem)
     * @throws Exception if the URL resolution fails
     */
    default String resolveRelative(String relativeUrl, String baseUrl) throws URISyntaxException {
        URI baseUri = new URI(baseUrl);
        URI resolvedUri = baseUri.resolve(relativeUrl);
        return resolvedUri.toString();
    }

    /**
     * Processes the list of relative URLs, resolves them to absolute URLs, and adds them to the set of URLs.
     * 
     * @param baseUrl the base URL to resolve relative URLs against
     * @param urls the set of URLs to add the resolved absolute URLs to
     * @param relativeUrls the list of relative URLs to process
     */
    default Set<String> resolveRelative(List<String> urls, String baseUrl) {
        Set<String> processedUrls = new HashSet<>();

        for (String url : urls) {
            // Skip invalid or already added urls
            if (url == null || Verify.isBlank(url) || processedUrls.contains(url)) {
                continue;
            }

            url = url.trim();

            try {
                if (Verify.isAbsolute(url)) { // already an absolute URL
                    if (Verify.isValid(url)) {
                        processedUrls.add(url);
                    }

                } else if (url.startsWith("//")) { // URL starts wtih "//" e.g (//example.com)
                    String absoluteUrl = "https:" + url;

                    if (Verify.isValid(absoluteUrl)) {
                        processedUrls.add(absoluteUrl);
                    }

                } else if (Verify.isValid(url)) { // checks if it is a valid relative path
                    processedUrls.add(resolveRelative(url, baseUrl));
                } else {
                    //log this
                    System.err.println("URL " + url + " didn't pass any checks");
                }

            } catch (URISyntaxException e) {
                System.err.println("Invalid URL syntax for URL: " + url + ". Error: " + e.getMessage());
            }
        }

        return processedUrls;
    }

    default Set<String> getAbsoluteUrls(String string) {
        UrlDetector parser = new UrlDetector(string, UrlDetectorOptions.HTML);
        List<Url> found = parser.detect();

        return found.stream()
            .map(url -> url.getOriginalUrl())
            .map(url -> {
                if (!url.matches("^(https?://|http%3a//|https%3a//).*")) {
                    if (!url.matches("^www\\..*")) {
                        return "https://www." + url;
                    } else {
                        return "https://" + url;
                    }
                }
                
                return url;
            }).collect(Collectors.toSet());
    }
}
