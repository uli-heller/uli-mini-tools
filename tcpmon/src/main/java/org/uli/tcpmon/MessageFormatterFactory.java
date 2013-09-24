package org.uli.tcpmon;

public class MessageFormatterFactory {
    
    static public MessageFormatter getMessageFormatter(MessageFormatterType messageFormatterType) {
        MessageFormatter result;
        if (messageFormatterType == MessageFormatterType.TEXT_ONLY) {
            result = new TextMessageFormatter();
        } else if (messageFormatterType == MessageFormatterType.HEX_ONLY) {
            result = new HexMessageFormatter();
        } else {
            result = new MixedMessageFormatter();
        }
        return result;
    }
}
