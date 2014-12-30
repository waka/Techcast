package waka.techcast.internal.rss;

import android.net.Uri;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import waka.techcast.models.Enclosure;
import waka.techcast.models.Feed;
import waka.techcast.models.Item;

public class FeedHandler extends DefaultHandler {
    private Feed feed;
    private Item item;
    private StringBuilder stringBuilder;

    public Feed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() {
        feed = new Feed();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        stringBuilder = new StringBuilder();

        if (qName.equals("item")) {
            item = new Item();
            feed.addItem(item);
        }
        if (item != null && qName.equals("enclosure")) {
            ItemSetter.setEnclosure(item, attributes);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName == null || qName.length() == 0) return;

        if (item == null) {
            // Parse feed elements
            FeedSetter.setValue(feed, qName, stringBuilder.toString());
        } else {
            // Parse item elements
            ItemSetter.setValue(item, qName, stringBuilder.toString());
        }
    }

    private static class FeedSetter {
        public static void setValue(Feed feed, String qName, String value) {
            switch (qName) {
                case "title":
                    feed.setTitle(value);
                    break;
                case "description":
                    feed.setDescription(value);
                    break;
                case "link":
                    feed.setLink(Uri.parse(value));
                    break;
                case "media:thumbnail":
                    feed.setThumbnail(value);
                    break;
                case "media:category":
                    feed.setCategory(value);
                    break;
                case "itunes:author":
                    feed.setAuthor(value);
                    break;
                case "itunes:image":
                    feed.setImage(value);
                    break;
            }
        }
    }

    private static class ItemSetter {
        public static void setValue(Item item, String qName, String value) {
            switch (qName) {
                case "title":
                    item.setTitle(value);
                    break;
                case "description":
                    item.setDescription(value);
                    break;
                case "pubDate":
                    item.setPubDate(value);
                    break;
                case "link":
                    item.setLink(Uri.parse(value));
                    break;
                case "itunes:subtitle":
                    item.setSubTitle(value);
                    break;
                case "itunes:duration":
                    item.setDuration(value);
                    break;
            }
        }

        public static void setEnclosure(Item item, Attributes attributes) {
            String url = attributes.getValue("url");
            String type = attributes.getValue("type");
            int length = Integer.parseInt(attributes.getValue("length"));
            Enclosure enclosure = new Enclosure(Uri.parse(url), type, length);
            item.setEnclosure(enclosure);
        }
    }
}
