package org.uli.tcpmon;

import java.nio.ByteBuffer;
import java.util.List;

import org.jboss.netty.buffer.*;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingProxyReceiver extends SimpleChannelUpstreamHandler {

    Logger logger        = LoggerFactory.getLogger(LoggingProxyReceiver.class);
    Logger receiveLogger = LoggerFactory.getLogger("RECV");
    private final Channel inboundChannel;
    private final MessageFormatter messageFormatter;

    LoggingProxyReceiver(Channel inboundChannel, MessageFormatter messageFormatter) {
        logger.info("->");
        receiveLogger.info("New receiving connection");
        this.inboundChannel = inboundChannel;
        this.messageFormatter = messageFormatter;
        logger.info("<-");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("->");
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        receiveLogger.info("messageReceived, readableBytes={}", msg.readableBytes());
        if (logger.isDebugEnabled()) {
            ByteBuffer bb = msg.toByteBuffer();
            List<String> lines = this.messageFormatter.format(bb.array());
            for (String l : lines) {
                receiveLogger.debug(l);
            }
        }
        inboundChannel.write(msg);
        logger.info("<-");
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("->");
        receiveLogger.info("channelClosed");
        closeOnFlush(inboundChannel);
        logger.info("<-");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.info("->");
        logger.warn("Exception:", e.getCause());
        receiveLogger.info("exceptionCaught", e.getCause());
        closeOnFlush(e.getChannel());
        logger.info("<-");
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        Logger logger = LoggerFactory.getLogger(LoggingProxyReceiver.class);
        logger.info("->");
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
        logger.info("<-");
    }
}
