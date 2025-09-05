package crawler.extractor.util;

public class Verify {

    private Verify() {}
    
    /**
     * Checks if the provided URL is an absolute URL (starts with "http://" or "https://").
     * 
     * @param url the URL to check
     * @return true if the URL is an absolute URL, false otherwise
     */
    public static boolean isAbsolute(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static boolean isBlank(String url) {
        return url.isBlank();
    }

    /**
     * Checks if the provided URL is valid. 
     * (notice that http doesn't count as a valid URL, because it isn't secure)
     * 
     * @param url the URL to check
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValid(String url) {
        if (check(url)) {
            return true;
        }

        return false;
    }

    /**
     * Performs various checks to validate a URL.
     * 
     * @param url the URL to check
     * @return true if the URL passes the checks, false otherwise
     */
    private static boolean check(String url) {
        // Exclude URLs with certain characteristics (e.g., anchor URLs, mailto URLs, etc.)
        return !url.startsWith("#")
                && !url.startsWith("mailto:")
                && !url.contains("javascript:")
                && !url.matches("(?i).*\\.(jpg|jpeg|png|gif|docx|zip|mp4|avi|svg|css|js|ico)(\\?.*)?$");
    }

    public static boolean isPdf(String url) {
        return url.matches("(?i).*\\.pdf(\\?.*)?$");
    }
}
