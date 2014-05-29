/**
 * 
 */
package org.uli.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


/**
 * @author uli
 *
 */
public class FileToByteArrayTest {

    @Test
    public void test() throws IOException {
        FileToByteArray ftba = new FileToByteArray(this.getClass().getResourceAsStream("/fileToByteArrayTest.txt"));
        assertArrayEquals(new byte[] { '1', '2', '3'}, ftba.getBytes());
        assertEquals("-", ftba.getFilename());
    }
}
