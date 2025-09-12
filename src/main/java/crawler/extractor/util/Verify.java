package crawler.extractor.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;

public class Verify {

    private Verify() {}
    
    /**
     * Checks if the provided URL is an absolute URL (starts with "http://" or "https://").
     * 
     * @param url the URL to check
     * @return true if the URL is an absolute URL, false otherwise
     */
    public static boolean isAbsolute(String url) {
        url = url.toLowerCase();
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
        url = url.toLowerCase();
        return !url.startsWith("#")
                && !url.startsWith("mailto:")
                && !url.contains("javascript:")
                && !url.matches("(?i).*\\.(jpg|jpeg|png|gif|docx|zip|mp4|avi|svg|css|js|ico)(\\?.*)?$");
    }

    public static boolean isPdf(String url) {
        Tika tika = new Tika();
        String mimeType = tika.detect(url);
        return "application/pdf".equals(mimeType);
    }

    public static boolean isPdf(InputStream stream) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(stream);
        return "application/pdf".equals(mimeType);
    }
}
