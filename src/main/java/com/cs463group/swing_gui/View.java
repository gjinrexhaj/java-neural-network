package com.cs463group.swing_gui;

import com.cs463group.neural_net.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.formdev.flatlaf.themes.FlatMacLightLaf;


/**
 *  View.java
 *  Created on 2/12/2025
 *  Contains all frontend GUI code for neural network visualizer
 */

// TODO: get rid of line 57 post-"maven build" bug
// TODO: implement loading data into model
// TODO: implement decision boundary graph
// TODO: implement prediction
// TODO: link frontend with backend neural network code

public class View extends JFrame {

    // Define application GUI components
    private JPanel mainPanel;
    private JSplitPane splitplane;
    private JTabbedPane viewsPanel;
    private JPanel trainingMethodPanel;
    private JRadioButton mutationRadioButton;
    private JRadioButton gradientDescentRadioButton;
    private JPanel attributesPanel;
    private JSpinner spinner_inputNodes;
    private JSpinner spinner_hiddenNodes;
    private JSpinner spinner_outputNodes;
    private JButton TRAINButton;
    private JButton PREDICTButton;
    private JButton analysisButton;
    private JButton saveButton;
    private JButton aboutButton;
    private JProgressBar progressBar1;
    private JSpinner spinner_trainingCycles;
    private JTextArea consoleTextArea;
    private JButton CREATEButton;
    private JTree fileTree;
    private JTextField predictionInputTextField;
    private JTextField spinner_learnFactor;
    private JButton loadDataButton;
    private JButton unloadDataButton;
    private JTextPane EXAMPLEWHATYOURDATATextPane;
    private JPanel NeuralNetworkVisualizerPanel;
    private JLabel predictionOutputLabel;
    private JLabel predictionConfidenceLabel;


    // Create logger instance
    static Logger logger = new Logger();

    // Create action and component listeners for all buttons and fields


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
                    Logger.log(Logger.LogLevel.INFO, "Application closed by user.", true, true);
                    Logger.closeLogger();
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

        // add working dir string to bottom of data view
        Logger.log(Logger.LogLevel.INFO, "GUI application fully initialized.", true, false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Populate the console view tab
        consoleTextArea = new JTextArea();
        ConsoleOutputStream console = new ConsoleOutputStream(consoleTextArea);
        System.setOut(new PrintStream(console));

        // Create file chooser gui for training data
        fileTree = new FileTree(System.getProperty("user.dir"));

        // Create neural network visualizer graph window

        // TEMP   TEMP    TEMP
        // define number of neurons, one layer at a time
        List<Layer> layers = new ArrayList<>();
        layers.add(new Layer(2));
        layers.add(new Layer(2));
        layers.add(new Layer(1));
        NeuralNetworkVisualizerPanel = new NeuralNetworkVisualizer(layers);

    }
}
