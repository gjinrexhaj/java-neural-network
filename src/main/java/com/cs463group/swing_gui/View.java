package com.cs463group.swing_gui;

import com.cs463group.neural_net.utils.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;


/**
 *  View.java
 *  Created on 2/12/2025
 *  Contains all frontend GUI code for neural network visualizer
 */

// TODO: get rid of line 57 post-"maven build" bug

// TODO: link neural net visualizer to network attribute parameters and render on "CREATE" button
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
    private JTextField textField_learnFactor;
    private JButton loadDataButton;
    private JButton unloadDataButton;
    private JTextPane EXAMPLEWHATYOURDATATextPane;
    private JPanel NeuralNetworkVisualizerPanel;
    private JLabel predictionOutputLabel;
    private JLabel predictionConfidenceLabel;
    private JSpinner spinner_inputDimensionality;

    // TRACK ALL VALUES OF FIELDS
    private Integer inputDimensionality;
    private Integer numOfInputNodes = 0;
    private Integer numOfHiddenNodes = 0;
    private Integer numOfOutputNodes = 0;
    private Integer numOfTrainingCycles = 0;
    private File previewData;
    private File loadedData;
    private boolean mutationTrain = false;
    private boolean gradientDescentTrain = false;

    // Create Arraylist containing neuralNetVisualizer layer values
    List<Layer> neuralNetVisualizerLayers;

    // Create logger instance
    static Logger logger = new Logger();

    // Create listeners for all fields
    public View() {
        spinner_inputDimensionality.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                inputDimensionality = (Integer) spinner_inputDimensionality.getValue();
                Logger.log(Logger.LogLevel.DEBUG, "inputDimensionality: " + inputDimensionality, true, false);
            }
        });
        spinner_inputNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfInputNodes = (Integer)spinner_inputNodes.getValue();
                Logger.log(Logger.LogLevel.DEBUG, "numOfInputNodes: " + numOfInputNodes, true, false);
            }
        });
        spinner_hiddenNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfHiddenNodes = (Integer)spinner_hiddenNodes.getValue();
                Logger.log(Logger.LogLevel.DEBUG, "numOfHiddenNodes: " + numOfHiddenNodes, true, false);
            }
        });
        spinner_outputNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfOutputNodes = (Integer)spinner_outputNodes.getValue();
                Logger.log(Logger.LogLevel.DEBUG, "numOfOutputNodes: " + numOfOutputNodes, true, false);
            }
        });

        // TODO: implement training cycles textfield param
        spinner_trainingCycles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfTrainingCycles = (Integer)spinner_trainingCycles.getValue();
                Logger.log(Logger.LogLevel.DEBUG, "numOfTrainingCycles: " + numOfTrainingCycles, true, false);
            }
        });

        // TODO: implement CREATE button, implement parameter checks, and link backend code
        CREATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO: Implement param checking and error handling


                // update view
                neuralNetVisualizerLayers.clear();
                neuralNetVisualizerLayers.add(new Layer(numOfInputNodes));
                neuralNetVisualizerLayers.add(new Layer(numOfHiddenNodes));
                neuralNetVisualizerLayers.add(new Layer(numOfOutputNodes));
                NeuralNetworkVisualizerPanel.updateUI();
            }
        });

        // TODO: implement TRAIN button, implement parameter checks, and link backend code
        TRAINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        // TODO: implement PREDICT button, implement parameter checks, and link backend code
        PREDICTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        mutationRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        gradientDescentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        unloadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {

            }
        });
        analysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        //TODO: Implement saving current frame as image
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showSaveDialog(null);
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Java Neural Network [Version 0.8 - DEV]" +
                        "\nA neural network engine and frontend GUI implemented in pure Java." +
                        "\nhttps://github.com/gjinrexhaj/java-neural-network\n" +
                        "\nDevelopers: " +
                        "\nGjin Rexhaj" +
                        "\nGerti Gjini" +
                        "\nIbrahim Elamin" +
                        "\nColm Duffin", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }



    // Create and initialize application
    public static void main(String[] args) {

        // set window decoration for linux, and window theming
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isLinux = osName.startsWith("linux");

        if (isLinux) {
            // enable custom window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }



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
        /*List<Layer>*/ neuralNetVisualizerLayers = new ArrayList<>();
//        neuralNetVisualizerLayers.add(new Layer(2));
//        neuralNetVisualizerLayers.add(new Layer(2));
//        neuralNetVisualizerLayers.add(new Layer(1));
//        // TEMP  TEMP     TEMP
        NeuralNetworkVisualizerPanel = new NeuralNetworkVisualizer(neuralNetVisualizerLayers);

    }
}
