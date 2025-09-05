package crawler.extractor.util;

public class Verify {

    private Verify() {}
    
    /**
     * Checks if the provided URL is an absolute URL (starts with "http://" or "https://").
     * 
     * @param URL the URL to check
     * @return true if the URL is an absolute URL, false otherwise
     */
    public static boolean isAbsolute(String URL) {
        return URL.startsWith("http://") || URL.startsWith("https://");
    }

    public static boolean isBlank(String URL) {
        return URL.isBlank();
    }

    /**
     * Checks if the provided URL is valid. 
     * (notice that http doesn't count as a valid URL, because it isn't secure)
     * 
     * @param URL the URL to check
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValid(String URL) {
        if (check(URL)) {
            return true;
        }

        return false;
    }

    /**
     * Performs various checks to validate a URL.
     * 
     * @param URL the URL to check
     * @return true if the URL passes the checks, false otherwise
     */
    private static boolean check(String URL) {
        // Exclude URLs with certain characteristics (e.g., anchor URLs, mailto URLs, etc.)
        return !URL.startsWith("#")
                && !URL.startsWith("mailto:")
                && !URL.startsWith("http://")
                && !URL.contains("javascript:")
                && !URL.matches(".*\\.(jpg|jpeg|png|gif|docx|zip|mp4|avi|svg|css|js|ico)$");
    }
}
