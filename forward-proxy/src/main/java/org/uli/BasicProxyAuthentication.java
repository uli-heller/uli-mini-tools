/**
 * 
 */
package org.uli;

import java.net.URI;

import org.eclipse.jetty.client.api.Authentication;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.Attributes;
import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author uli
 *
 */
public class BasicProxyAuthentication implements Authentication {
    Logger myLogger = LoggerFactory.getLogger(getClass());
    String user;
    String password;

    BasicProxyAuthentication(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jetty.client.api.Authentication#matches(java.lang.String, java.net.URI, java.lang.String)
     */
    @Override
    public boolean matches(String type, URI uri, String realm) {
        return true;
    }

    @Override
    public Result authenticate(Request request, ContentResponse response, HeaderInfo headerInfo, Attributes context)
    {
        myLogger.debug("-> request={}, response={}, headerInfo={}", request, response, headerInfo);
        String value = getValue(user, password);
        Result result = new BasicProxyResult(headerInfo.getHeader(), request.getURI(), value);
        myLogger.debug("<- {}", result);
        return result;
    }

    private final String getValue(String user, String password) {
        String encoding = StringUtil.__ISO_8859_1;
        String value = "Basic " + B64Code.encode(user + ":" + password, encoding);
        return value;
    }

    public void apply(Request request) {
        String value = getValue(user, password);
        request.header(HttpHeader.PROXY_AUTHORIZATION/*"Proxy-Authorization"*/, value);
    }

    private static class BasicProxyResult implements Result
    {
        Logger myLogger = LoggerFactory.getLogger(getClass());
        private final HttpHeader header;
        private final URI uri;
        private final String value;

        public BasicProxyResult(HttpHeader header, URI uri, String value)
        {
            this.header = header;
            this.uri = uri;
            this.value = value;
        }

        @Override
        public URI getURI()
        {
            return uri;
        }

        @Override
        public void apply(Request request)
        {
            myLogger.debug("-> header={}, value={}", header, value);
            request.header(header, value);
            myLogger.debug("<-");
        }

        @Override
        public String toString()
        {
            return String.format("Basic proxy authentication result for %s", uri);
        }
    }

}
