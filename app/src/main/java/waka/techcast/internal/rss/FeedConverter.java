package waka.techcast.internal.rss;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import waka.techcast.models.Feed;

public class FeedConverter {
    public static Feed convert(String str) {
        Feed feed = null;
        try {
            feed = read(str);
        } catch (IOException | SAXException ignored) {
        }
        return feed;
    }

    private static Feed read(String source) throws SAXException, IOException {
        return read(new ByteArrayInputStream(source.getBytes()));
    }

    private static Feed read(InputStream stream) throws SAXException, IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            FeedHandler handler = new FeedHandler();
            InputSource input = new InputSource(stream);

            reader.setContentHandler(handler);
            reader.parse(input);

            return handler.getFeed();
        } catch (ParserConfigurationException e) {
            throw new SAXException();
        }
    }
}
