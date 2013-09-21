/**
 * 
 */
package org.uli;

import javax.servlet.ServletException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ProxyConfiguration;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author uli
 *
 */
public class ForwardProxyServlet extends ProxyServlet {

    private final Logger myLogger = LoggerFactory.getLogger(ForwardProxyServlet.class);
    
    private String proxyHost = "whatever";
    private int proxyPort = 8080;
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public ForwardProxyServlet() {
        super();
    }
    
    @Override
    protected HttpClient createHttpClient() throws ServletException {
        myLogger.debug("-> createHttpClient()");
        try {
            HttpClient httpClient = super.createHttpClient();
            ProxyConfiguration proxyConfig = new ProxyConfiguration(proxyHost, proxyPort);
            httpClient.setProxyConfiguration(proxyConfig);
            return httpClient;
        } finally {
            myLogger.debug("<- createHttpClient()");
        }
    }
    
    @Override
    protected HttpClient newHttpClient() {
        myLogger.debug("-> newHttpClient()");
        HttpClient httpClient = super.newHttpClient();
        myLogger.debug("<- newHttpClient()");
        return httpClient;
    }

}
