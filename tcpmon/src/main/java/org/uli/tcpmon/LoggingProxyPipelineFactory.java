package org.uli.tcpmon;

import static org.jboss.netty.channel.Channels.*;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;

public class LoggingProxyPipelineFactory implements ChannelPipelineFactory {
    private final ClientSocketChannelFactory cf;
    private final String remoteHost;
    private final int remotePort;
    private final MessageFormatter messageFormatter;

    public LoggingProxyPipelineFactory(ClientSocketChannelFactory cf, String remoteHost, int remotePort, MessageFormatter messageFormatter) {
        this.cf = cf;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.messageFormatter = messageFormatter;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = pipeline(); // Note the static import.
        p.addLast("handler", new LoggingProxySender(cf, remoteHost, remotePort, messageFormatter));
        return p;
    }
}
