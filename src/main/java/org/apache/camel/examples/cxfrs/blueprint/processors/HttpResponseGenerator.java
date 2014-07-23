package org.apache.camel.examples.cxfrs.blueprint.processors;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Variant;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

/**
 * Generate an Http Response based on Message.Headers such as
 * CamelHttpResponseCode
 * Content-Type
 * Link, rel
 * ETag
 * Allow
 * Content-Encoding
 * Expires
 * Content-Language
 * Last-Modified
 * Variant
 * Location
 * 
 * @author Lelan-j
 * 
 */
public class HttpResponseGenerator implements Processor {

    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        Integer status = message.getHeader(Exchange.HTTP_RESPONSE_CODE,
                Integer.class);
        String type = message.getHeader(Exchange.CONTENT_TYPE, String.class);
        Object entity = message.getBody();
        String link = message.getHeader("Link", String.class);
        String rel = message.getHeader("rel", String.class);
        String tag = message.getHeader("ETag", String.class);
        String allow = message.getHeader("Allow", String.class);
        String encoding = message.getHeader(Exchange.CONTENT_ENCODING,
                String.class);
        Date expires = message.getHeader("Expires", Date.class);
        String language = message.getHeader("Content-Language", String.class);
        Date lastModified = message.getHeader("Last-Modified", Date.class);
        Variant variant = message.getHeader("Variant", Variant.class);
        String location = message.getHeader("Location", String.class);
        ResponseBuilder responseBuilder = new ResponseBuilderImpl();
        if (status != null) {
            responseBuilder.status(status);
        }
        if (type != null) {
            responseBuilder.type(type);
        }
        if (entity != null) {
            responseBuilder.entity(entity);
        }
        if (link != null && rel != null) {
            responseBuilder.link(link, rel);
        }
        if (tag != null) {
            responseBuilder.tag(tag);
        }
        if (allow != null) {
            responseBuilder.allow(allow);
        }
        if (encoding != null) {
            responseBuilder.encoding(encoding);
        }
        if (expires != null) {
            responseBuilder.expires(expires);
        }
        if (language != null) {
            responseBuilder.language(language);
        }
        if (lastModified != null) {
            responseBuilder.lastModified(lastModified);
        }
        if (variant != null) {
            responseBuilder.variant(variant);
        }
        if (location != null) {
            responseBuilder.location(new URI(location));
        }
        exchange.getIn().setBody(responseBuilder.build(), Response.class);
    }

}
