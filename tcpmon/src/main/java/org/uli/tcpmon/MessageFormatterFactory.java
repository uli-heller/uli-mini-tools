package org.uli.tcpmon;

public class MessageFormatterFactory {
    static public MessageFormatter getMessageFormatter(MessageFormatterType messageFormatterType) {
        return messageFormatterType.getMessageFormatter();
    }
}
