package com.cooper.nwemail.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * A POJO representing an XML RSSFeed
 */
@Root(name = "rss", strict = false)
public class RSSFeed {

    @Element
    private Channel channel;

    @Attribute
    private String version;

    public Channel getChannel() {
        return channel;
    }
}
