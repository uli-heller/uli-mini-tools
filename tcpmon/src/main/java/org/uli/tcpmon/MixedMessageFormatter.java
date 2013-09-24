package org.uli.tcpmon;

import java.util.List;

public class MixedMessageFormatter implements MessageFormatter {
    @Override
    public List<String> format(byte[] message) {
        HexDump hexDump = HexDump.builder().build();
        List<String> formatted = hexDump.dumpToList(message);
        return formatted;
    }
}
