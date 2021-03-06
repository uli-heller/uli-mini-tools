//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.uli.forwardproxy;

import org.eclipse.jetty.proxy.ConnectHandler;
//import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ForwardProxyServer
{
    public static void main(String[] args) throws Exception
    {
        ForwardProxyProperties fpp = ForwardProxyProperties.getInstance();
        Server server = new Server(fpp.getProxyPort());

        // Setup proxy handler to handle CONNECT methods
        ConnectHandler proxy = new ConnectHandler();
        server.setHandler(proxy);

        // Setup proxy servlet
        ServletContextHandler context = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);
        ServletHolder proxyServlet = new ServletHolder(ForwardProxyServlet.class);
        //proxyServlet.setInitParameter("blackList", "www.eclipse.org");
        context.addServlet(proxyServlet, "/*");

        server.start();
    }

}
