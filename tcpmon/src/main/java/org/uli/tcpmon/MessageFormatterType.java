package org.uli.tcpmon;

public enum MessageFormatterType {
    MIXED(new MixedMessageFormatter()), TEXT_ONLY(new TextMessageFormatter()), HEX_ONLY(new HexMessageFormatter());
    
    MessageFormatter messageFormatter;
    
    MessageFormatterType(MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    public MessageFormatter getMessageFormatter() {
        return this.messageFormatter;
    }
}
