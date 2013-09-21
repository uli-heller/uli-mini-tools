/**
 * 
 */
package org.uli;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author uli
 *
 */
public class ForwardProxyProperties {

    private final Logger myLogger = LoggerFactory.getLogger(ForwardProxyProperties.class);
    
    private final static String FORWARD_PROXY_PROPERTIES = "forward-proxy.properties";
    private final static String PARENT_PROXY_HOST = "parentProxyHost";
    private final static String PARENT_PROXY_PORT = "parentProxyPort";
    private final static String REPLACE_HEADERS = "replaceHeaders";
    private final static String PROXY_PORT="proxyPort";

    private Properties properties;
    private String parentProxyHost = null;
    private int parentProxyPort = 0;
    private int proxyPort = 0;
    private List<Header> headers = new LinkedList<Header>();
    
    /**
     * 
     */
    private ForwardProxyProperties() {
        super();
        initProperties();
    }
    
    public String getParentProxyHost() {
        return this.parentProxyHost;
    }

    public int getParentProxyPort() {
        return this.parentProxyPort;
    }

    public int getPoxyPort() {
        return this.proxyPort;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    private final void loadProperties(Properties p, InputStream is, final String label) {
        try {
            p.load(is);
        } catch (IOException e) {
            myLogger.warn("Unable to load properties from {}", label);
        }
    }

    private final int getIntProperty(Properties p, String name, int deflt) {
        int result;
        String s = p.getProperty(name);
        if (s == null) {
            result = deflt;
        } else {
            try {
                result = Integer.parseInt(s);
            } catch (Exception e) {
                result = deflt;
            }
        }
        return result;
    }

    private final String getStringProperty(Properties p, String name, String deflt) {
        String result = p.getProperty(name, deflt);
        return result.trim();
    }

    private final void initProperties() {
        InputStream is = this.getClass().getResourceAsStream("/" + FORWARD_PROXY_PROPERTIES);
        this.properties = new Properties();
        loadProperties(this.properties, is, "classpath:/"+FORWARD_PROXY_PROPERTIES);
        loadProperties(this.properties, is, FORWARD_PROXY_PROPERTIES);
        this.parentProxyHost = getStringProperty(this.properties, PARENT_PROXY_HOST, "");
        this.parentProxyPort = getIntProperty(this.properties, PARENT_PROXY_PORT, -1);
        this.proxyPort = getIntProperty(this.properties, PROXY_PORT, -1);
        String headersString = this.properties.getProperty(REPLACE_HEADERS);
        if (headersString != null && headersString.trim().length() > 0) {
            String[] headerNames = headersString.split(",");
            for (String headerName : headerNames) {
                String headerValue = this.properties.getProperty(headerName.trim(), "");
                if (headerValue.trim().length() > 0) {
                    Header h = new Header(headerName, headerValue);
                    this.headers.add(h);
                } else {
                    myLogger.warn("No value defined for header {} -> ignored", headerName);
                }
            }
        }
    }
    
    final class Header {
        final String name;
        final String value;
        
        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public Header(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
    }
    
    private static class Holder {
        static ForwardProxyProperties instance = new ForwardProxyProperties();
    }
    
    public final static ForwardProxyProperties getInstance() {
        return Holder.instance;
    }
}
