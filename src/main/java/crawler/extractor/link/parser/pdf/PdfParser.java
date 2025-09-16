package crawler.extractor.link.parser.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import crawler.extractor.link.parser.Parser;
import crawler.extractor.util.Verify;

public class PdfParser implements Parser {
    // TODO make crawler work with pdf refrences - search it up and get link
    // TODO If website is in list of reputable research sites and links list 
    // has .pdf url get rid of all other urls and just return only pdf links. If any. 
    // aslo depending on site add cite as with links

    //TODO should take links from refrence page
    public Set<String> parsePdf(String url) {
        try {
            URL entity = new URI(url).toURL();
            try (InputStream input = entity.openStream()) {

                if (!Verify.isPdf(input)) {
                    return Collections.emptySet();
                }

                ContentHandler handler = new BodyContentHandler();
                Metadata metadata = new Metadata();
                AutoDetectParser parser = new AutoDetectParser();

                parser.parse(input, handler, metadata);
                String content = handler.toString()
                    .replaceAll("\n|\r|\t", " ");

                return getAbsoluteUrls(content);

            } catch (IOException e) {
                System.err.println(e.getStackTrace());
                // could log it
            }

        } catch(Exception e) {
            System.err.println(e.getStackTrace());
            //TODO log it
        }

        return Collections.emptySet();
    }

}
