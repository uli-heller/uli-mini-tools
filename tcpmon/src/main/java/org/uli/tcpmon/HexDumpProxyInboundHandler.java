package org.uli.tcpmon;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HexDumpProxyInboundHandler extends SimpleChannelUpstreamHandler {
    Logger logger = LoggerFactory.getLogger(HexDumpProxyInboundHandler.class);
    private final ClientSocketChannelFactory cf;
    private final String remoteHost;
    private final int remotePort;
    private volatile Channel outboundChannel;

    public HexDumpProxyInboundHandler(ClientSocketChannelFactory cf, String remoteHost, int remotePort) {
        logger.info("->");
        this.cf = cf;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        logger.info("<-");
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("->");
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
        logger.info("<-");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("->");
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        if (logger.isDebugEnabled()) {
            logger.debug(" : >>> " + ChannelBuffers.hexDump(msg));
        }
        outboundChannel.write(msg);
        logger.info("<-");
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("->");
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
        logger.info("<-");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.info("->");
        logger.warn("Exception:", e.getCause());
        closeOnFlush(e.getChannel());
        logger.info("<-");
    }

    private static class OutboundHandler extends SimpleChannelUpstreamHandler {
        Logger logger = LoggerFactory.getLogger(OutboundHandler.class);
        private final Channel inboundChannel;

        OutboundHandler(Channel inboundChannel) {
            logger.info("->");
            this.inboundChannel = inboundChannel;
            logger.info("<-");
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            logger.info("->");
            ChannelBuffer msg = (ChannelBuffer) e.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug(" : <<< " + ChannelBuffers.hexDump(msg));
            }
            inboundChannel.write(msg);
            logger.info("<-");
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            logger.info("->");
            closeOnFlush(inboundChannel);
            logger.info("<-");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            logger.info("->");
            logger.warn("Exception:", e.getCause());
            closeOnFlush(e.getChannel());
            logger.info("<-");
        }
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        Logger logger = LoggerFactory.getLogger(HexDumpProxyInboundHandler.class);
        logger.info("->");
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
        logger.info("<-");
    }
}
