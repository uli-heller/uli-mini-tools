package org.uli.tcpmon;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingProxySender extends SimpleChannelUpstreamHandler {
    Logger logger = LoggerFactory.getLogger(LoggingProxySender.class);
    Logger sendLogger = LoggerFactory.getLogger("SEND");
    private final ClientSocketChannelFactory cf;
    private final String remoteHost;
    private final int remotePort;
    private final MessageFormatter messageFormatter;
    private volatile Channel outboundChannel;

    public LoggingProxySender(ClientSocketChannelFactory cf, String remoteHost, int remotePort, MessageFormatter messageFormatter) {
        logger.info("->");
        this.cf = cf;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.messageFormatter = messageFormatter;
        logger.info("<-");
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("->");
        sendLogger.info("New connection to {}:{} - trying to connect", remoteHost, remotePort);
        // Suspend incoming traffic until connected to the remote host.
        final Channel inboundChannel = e.getChannel();
        inboundChannel.setReadable(false);
        // Start the connection attempt.
        ClientBootstrap cb = new ClientBootstrap(cf);
        cb.getPipeline().addLast("handler", new LoggingProxyReceiver(e.getChannel(), this.messageFormatter));
        ChannelFuture f = cb.connect(new InetSocketAddress(remoteHost, remotePort));
        outboundChannel = f.getChannel();
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection attempt succeeded:
                    // Begin to accept incoming traffic.
                    inboundChannel.setReadable(true);
                    sendLogger.info("New connection to {}:{} - ready", remoteHost, remotePort);
                } else {
                    // Close the connection if the connection attempt has failed.
                    inboundChannel.close();
                    sendLogger.info("New connection to {}:{} - failure -> closing inbound channel", remoteHost, remotePort);
                }
            }
        });
        logger.info("<-");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("->");
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        sendLogger.info("messageReceived, readableBytes={}", msg.readableBytes());
        if (logger.isDebugEnabled()) {
            ByteBuffer bb = msg.toByteBuffer();
            List<String> lines = this.messageFormatter.format(bb.array());
            for (String l : lines) {
                sendLogger.debug(l);
            }
        }
        outboundChannel.write(msg);
        logger.info("<-");
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("->");
        sendLogger.info("channelClosed");
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
        logger.info("<-");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.info("->");
        logger.warn("Exception:", e.getCause());
        sendLogger.info("exceptionCaught", e.getCause());
        closeOnFlush(e.getChannel());
        logger.info("<-");
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        Logger logger = LoggerFactory.getLogger(LoggingProxySender.class);
        logger.info("->");
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
        logger.info("<-");
    }
}
