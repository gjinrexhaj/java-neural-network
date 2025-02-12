package com.cs463group.swing_gui;

import com.cs463group.neural_net.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.formdev.flatlaf.themes.FlatMacLightLaf;


public class View extends JFrame {



    // Define application GUI components
    private JPanel mainPanel;
    private JSplitPane splitplane;
    private JTabbedPane viewsPanel;
    private JPanel trainingMethodPanel;
    private JRadioButton mutationRadioButton;
    private JRadioButton gradientDescentRadioButton;
    private JPanel attributesPanel;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;
    private JButton TRAINButton;
    private JButton RUNButton;
    private JButton analysisButton;
    private JButton saveButton;
    private JButton aboutButton;

    // Create logger instance
    static Logger logger = new Logger();

    // Create and initialize application
    public static void main(String[] args) {

        // set custom theming
        FlatMacLightLaf.setup();
        Font newFont = new Font("Arial", Font.PLAIN, 14);
        UIManager.put("defaultFont", newFont);

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
