package com.cs463group.swing_gui;

import com.cs463group.neural_net.utils.Logger;
import com.cs463group.bridge.DataLoader;
import com.cs463group.neural_net.mutation_training.Network;

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

import com.formdev.flatlaf.themes.FlatMacLightLaf;


/**
 *  GuiApp.java
 *  Created on 2/12/2025
 *  Contains all frontend GUI code for neural network visualizer
 */

// TODO: get rid of line 57 post-"maven build" bug

// TODO: IMPLEMENT GRADIENT DESCENT SELECTION ONCE GRADIENT DESCENT BACKEND IS IMPLEMENTED AND/OR CONSOLIDATED
// TODO: implement loading data into model
// TODO: implement decision boundary graph
// TODO: implement prediction
// TODO: link frontend with backend neural network code

// TODO: Integrate backend with frontend - creation of nn, loading of data, training nn, making a prediction
// TODO: Implement loading data into dataLoader and seeing loaded data in data view panel
// TODO: log all important interactions such that they show on console view

public class GuiApp extends JFrame {

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

    // TRACK ALL VALUES OF FIELDS + DATA TO FEED INTO BACKEND
    private Integer inputDimensionality = 0;
    private Integer numOfInputNodes = 0;
    private Integer numOfHiddenNodes = 0;
    private Integer numOfOutputNodes = 0;
    private Integer numOfTrainingCycles = 0;
    private Double learningRate = 0.15;

    // ALLOCATE NEURAL NETWORK OBJECT
    Network neuralNetwork;

    private File previewData;
    private File loadedData;
    private List<List<Double>> inputData;
    private List<List<Double>> expectedAnswers;

    private boolean mutationTrain = false;
    private boolean gradientDescentTrain = false;

    // Create Arraylist containing neuralNetVisualizer layer values
    List<Layer> neuralNetVisualizerLayers;

    // Create logger instance
    static Logger logger = new Logger();

    // Create listeners for all fields
    public GuiApp() {
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

        spinner_trainingCycles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfTrainingCycles = (Integer)spinner_trainingCycles.getValue();
                Logger.log(Logger.LogLevel.DEBUG, "numOfTrainingCycles: " + numOfTrainingCycles, true, false);
            }
        });

        // TODO: implement CREATE button, IMPLEMENT GRADIENT DESCENT METHOD SELECTION ONCE BACKEND IS AVAILABLE
        CREATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // PARAMETER CHECKING
                // check if a training method is selected
                if (!(mutationTrain || gradientDescentTrain)) {
                    // display appropriate error
                    JOptionPane.showMessageDialog(mainPanel, "Malformed Parameter." +
                                    "\nPlease select a training method.",
                            "Creation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check input dimensionality
                if (inputDimensionality < 1) {
                    // display appropriate error
                    JOptionPane.showMessageDialog(mainPanel, "Malformed Parameter." +
                                    "\nInput Dimensionality should be greater than 0. Please note that input dimensionality parameter is contingent on the data you feed the network.",
                            "Creation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if no values are zero
                if (numOfInputNodes < 1 || numOfHiddenNodes < 1 || numOfOutputNodes < 1) {
                    // display appropriate error
                    JOptionPane.showMessageDialog(mainPanel, "Malformed Parameter." +
                                    "\nNumber of nodes for each category should be greater than 0. Please note that number of output nodes is contingent on the data you feed the network.",
                            "Creation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if negativeLearnRate
                if (learningRate < 0.0) {
                    JOptionPane.showMessageDialog(mainPanel, "Malformed Parameter." +
                                    "\nLearning Rate should be greater than zero." +
                                    "\nIdeal range (0.1 - 1)",
                            "Creation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // update view
                neuralNetVisualizerLayers.clear();
                neuralNetVisualizerLayers.add(new Layer(numOfInputNodes));
                neuralNetVisualizerLayers.add(new Layer(numOfHiddenNodes));
                neuralNetVisualizerLayers.add(new Layer(numOfOutputNodes));
                NeuralNetworkVisualizerPanel.updateUI();

                // create neural network with aforementioned parameters
                neuralNetwork = new Network(numOfTrainingCycles, learningRate, inputDimensionality, numOfInputNodes, numOfHiddenNodes, numOfOutputNodes);

                // logger stuff
                String traintype = "";
                if (mutationTrain) {
                    traintype = "mutation";
                } else if (gradientDescentTrain) {
                    traintype = "gradientDescent";
                }

                Logger.log(Logger.LogLevel.INFO, "Neural Network created! Stats listed below.", true, false);
                Logger.log(Logger.LogLevel.INFO,
                        "Training method           : " + traintype
                        + "\nInput Dimensionality        : " + inputDimensionality
                        + "\nNumber of input neurons   : " + numOfInputNodes
                        + "\nNumber of hidden neurons  : " + numOfHiddenNodes
                        + "\nNumber of output neurons  : " + numOfOutputNodes
                        + "\nNumber of training cycles : " + numOfTrainingCycles
                        + "\nLearning Rate             : " + learningRate, false, false);

                // WARNING CHECK
                // check if low training cycle count
                if (numOfTrainingCycles < 100) {
                    // Warn user of low number of training cycles
                    JOptionPane.showMessageDialog(mainPanel, "Low number of training cycles specified." +
                                    "\nThis may result in severe under-performance depending on the dataset.",
                            "Creation Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    // if no warning, notify user it created w/o warnings
                    JOptionPane.showMessageDialog(mainPanel, "Network Created Successfully!"
                                    + "\n\n--- ATTRIBUTES ---\n"
                                    + "\nTraining method: " + traintype
                                    + "\nInput Dimensionality: " + inputDimensionality
                                    + "\nNumber of input neurons: " + numOfInputNodes
                                    + "\nNumber of hidden neurons: " + numOfHiddenNodes
                                    + "\nNumber of output neurons: " + numOfOutputNodes
                                    + "\nNumber of training cycles: " + numOfTrainingCycles
                                    + "\nLearning Rate: " + learningRate,
                            "Creation Notification", JOptionPane.INFORMATION_MESSAGE);
                }




            }
        });

        // TODO: implement TRAIN button, implement parameter checks, and link backend code
        TRAINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(neuralNetwork == null) {
                    JOptionPane.showMessageDialog(mainPanel, "Neural Network does not exist.",
                            "Training Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // TODO: implement PREDICT button, implement parameter checks, and link backend code
        PREDICTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(neuralNetwork == null) {
                    JOptionPane.showMessageDialog(mainPanel, "Neural Network does not exist.",
                            "Prediction Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mutationRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mutationTrain = true;
                gradientDescentTrain = false;
                Logger.log(Logger.LogLevel.DEBUG, "mutationTrainEnabled", true, false);
            }
        });
        gradientDescentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gradientDescentTrain = true;
                mutationTrain = false;
                Logger.log(Logger.LogLevel.DEBUG, "gradientDescentTrainEnabled", true, false);
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

        textField_learnFactor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                learningRate = Double.parseDouble(textField_learnFactor.getText());
                Logger.log(Logger.LogLevel.DEBUG, "learningRate: " + learningRate, true, false);

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


        frame.setContentPane(new GuiApp().mainPanel);
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
        neuralNetVisualizerLayers = new ArrayList<>();
        NeuralNetworkVisualizerPanel = new NeuralNetworkVisualizer(neuralNetVisualizerLayers);

    }
}
