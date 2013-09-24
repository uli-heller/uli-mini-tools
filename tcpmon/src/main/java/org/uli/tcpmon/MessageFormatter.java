package org.uli.tcpmon;

import java.util.List;

public interface MessageFormatter {
    public List<String> format(byte[] message);
}
