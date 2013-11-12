package org.uli.littleproxy;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

public class LittleProxyServer {
    public static void main(String[] args) {
	HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
	    .withPort(8080)
	    .start();
    }
}
