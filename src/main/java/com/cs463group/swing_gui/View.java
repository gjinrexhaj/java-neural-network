package com.cs463group.swing_gui;

import com.cs463group.neural_net.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class View extends JFrame {

    // Define application GUI components
    private JPanel mainPanel;
    private JSplitPane splitplane1;
    private JScrollPane consoleScrollPane;
    private JTabbedPane tabbedPane;

    // Create logger instance
    static Logger logger = new Logger();

    // Create and initialize application
    public static void main(String[] args) {

        // set attributes
        JFrame frame = new JFrame("Neural Network GUI Frontend");
        frame.setContentPane(new View().mainPanel);
        frame.pack();
        frame.setSize(1280,720);

        // override onclose function - prompt user for confirmation upon closing
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setEnabled(false);
                int confirm = JOptionPane.showOptionDialog(
                        null, "Are you sure you'd like to exit the application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    logger.log(Logger.LogLevel.DEBUG, "Application closed by user.");
                    logger.closeLogger();
                    System.exit(0);
                } else {
                    frame.setEnabled(true);
                }
            }
        };
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(exitListener);
        frame.setVisible(true);
        // center window
        frame.setLocationRelativeTo(null);

        logger.log(Logger.LogLevel.DEBUG, "Application initialized.");

    }




}
