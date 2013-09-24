package org.uli.tcpmon;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;

public class TextDumpProxyInboundHandler extends SimpleChannelUpstreamHandler {
    private final ClientSocketChannelFactory cf;
    private final String remoteHost;
    private final int remotePort;
    private volatile Channel outboundChannel;
    public TextDumpProxyInboundHandler(ClientSocketChannelFactory cf, String remoteHost, int remotePort) {
        this.cf = cf;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }
    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        // Suspend incoming traffic until connected to the remote host.
        final Channel inboundChannel = e.getChannel();
        inboundChannel.setReadable(false);
        // Start the connection attempt.
        ClientBootstrap cb = new ClientBootstrap(cf);
        cb.getPipeline().addLast("handler", new OutboundHandler(e.getChannel()));
        ChannelFuture f = cb.connect(new InetSocketAddress(remoteHost, remotePort));
        outboundChannel = f.getChannel();
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection attempt succeeded:
                    // Begin to accept incoming traffic.
                    inboundChannel.setReadable(true);
                } else {
                    // Close the connection if the connection attempt has failed.
                    inboundChannel.close();
                }
            }
        });
    }
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        System.out.println(">>> " + msg.toString(Charset.defaultCharset()));
        outboundChannel.write(msg);
    }
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        closeOnFlush(e.getChannel());
    }
    private static class OutboundHandler extends SimpleChannelUpstreamHandler {
        private final Channel inboundChannel;
        OutboundHandler(Channel inboundChannel) {
            this.inboundChannel = inboundChannel;
        }
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            ChannelBuffer msg = (ChannelBuffer) e.getMessage();
            System.out.println("<<< " + msg.toString(Charset.defaultCharset()));
            inboundChannel.write(msg);
        }
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            closeOnFlush(inboundChannel);
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            e.getCause().printStackTrace();
            closeOnFlush(e.getChannel());
        }
    }
    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
