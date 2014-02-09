package org.uli.littleproxy;

import java.util.Queue;

import io.netty.handler.codec.http.HttpRequest;

import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.ChainedProxyManager;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

public class LittleProxyServer {

    public static void main(String[] args) {
        HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
                    .withPort(8888)
                    .start();
    }

    public static void main1(String[] args) {
        ChainedProxyManager chainProxyManager = new ChainedProxyManager() {
            public void onCommunicationError(String hostAndPort) {
                // TODO Auto-generated method stub
            }

            public String getChainProxy(HttpRequest httpRequest) {
                return "proxy.mycompany.org:3128";
            }

            @Override
            public void lookupChainedProxies(HttpRequest httpRequest, Queue<ChainedProxy> chainedProxies) {
                // TODO Auto-generated method stub
                
            }
        };
        
        HttpFiltersSource filtersSource = new HttpFiltersSourceAdapter();
        HttpProxyServer proxyServer = DefaultHttpProxyServer.bootstrap()
                    .withPort(8080)
                    .withChainProxyManager(chainProxyManager)
                    .withFiltersSource(filtersSource)
                    .start();
    }
    
    static class myFiltersSource extends HttpFiltersSourceAdapter {
        @Override
        public HttpFilters filterRequest(HttpRequest originalRequest) {
            return new myFilters(originalRequest);
        }
    }
    
    static class myFilters extends HttpFiltersAdapter {
        public myFilters(HttpRequest originalRequest) {
            super(originalRequest);
        }
    }
}
