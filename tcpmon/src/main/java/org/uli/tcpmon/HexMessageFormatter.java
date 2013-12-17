package org.uli.tcpmon;

import java.util.List;
import org.uli.util.HexDump;

public class HexMessageFormatter implements MessageFormatter {
    @Override
    public List<String> format(byte[] message) {
        HexDump hexDump = HexDump.builder().bytesPerLine(32).dumpHex(true).dumpText(false).build();
        List<String> formatted = hexDump.dumpToList(message);
        return formatted;
    }
}
