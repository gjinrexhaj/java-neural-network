package com.cs462group.swing_gui;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;


/**
 *  ConsoleOutputStream.java
 *  Created on 2/19/2025
 *  Displays logger output text onto GUI widget
 */

public class ConsoleOutputStream extends OutputStream {

    private final JTextArea destination;

    public ConsoleOutputStream (JTextArea destination) {
        if (destination == null) {
            throw new IllegalArgumentException("Destination is null");
        }
        this.destination = destination;

    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        final String text = new String(buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                destination.append(text);
            }
        });
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}
