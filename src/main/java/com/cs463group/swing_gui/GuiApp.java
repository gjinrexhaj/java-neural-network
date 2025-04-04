package com.cs463group.swing_gui;

import com.cs463group.bridge.DataLoader;
import com.cs463group.neural_net.gradient_descent.cDifferentialNetwork;
import com.cs463group.neural_net.utils.Logger;
import com.cs463group.neural_net.mutation_training.Network;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.jthemedetecor.OsThemeDetector;


/**
 *  GuiApp.java
 *  Created on 2/12/2025
 *  Contains all frontend GUI code for neural network visualizer
 */


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
    private JButton aboutButton;
    private JSpinner spinner_trainingCycles;
    private JTextArea consoleTextArea;
    private JButton CREATEButton;
    private JTree fileTree;
    private JTextField predictionInputTextField;
    private JTextField textField_learnFactor;
    private JButton loadDataButton;
    private JButton unloadDataButton;
    private JTextPane loadedDataView;
    private JPanel NeuralNetworkVisualizerPanel;
    private JLabel predictionOutputLabel;
    private JButton clearConsoleButton;
    private JLabel netattrLabel;
    private JLabel netTrainInfoLabel;
    private JLabel predictionInputLabel;

    // TRACK ALL VALUES OF FIELDS + DATA TO FEED INTO BACKEND
    private Integer numOfInputNodes = 0;
    private Integer numOfHiddenNodes = 0;
    private Integer numOfOutputNodes = 0;
    private Integer numOfTrainingCycles = 0;
    private Double learningRate = 1.0;

    int trainedEpochs = 0;

    // ALLOCATE NEURAL NETWORK OBJECT
    Network mutNeuralNetwork;
    cDifferentialNetwork difNeuralNetwork;

    // Data to be loaded into network
    List<List<Double>> LoadedFile = new ArrayList<>();
    List<List<Double>> LoadedData = new ArrayList<>();
    List<List<Double>> LoadedAnswers = new ArrayList<>();

    List<Double> PredictionInput = new ArrayList<>();

    private boolean dataLoaded = false;
    private static boolean debugMode;

    static String osName;
    static boolean isLinux;
    Color blue = new Color(0, 122, 255);

    private boolean mutationTrain = false;
    private boolean gradientDescentTrain = false;
    private boolean neuralNetworkCreated = false;

    private String filePath;
    static int counter = 0;

    // Create Arraylist containing neuralNetVisualizer layer values
    List<Layer> neuralNetVisualizerLayers;

    // Create logger instance
    static Logger logger = new Logger();

    // Create listeners for all fields
    public GuiApp() {

        spinner_inputNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfInputNodes = (Integer)spinner_inputNodes.getValue();

                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfInputNodes: " + numOfInputNodes, true, false);
            }
        });
        spinner_hiddenNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfHiddenNodes = (Integer)spinner_hiddenNodes.getValue();
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfHiddenNodes: " + numOfHiddenNodes, true, false);
            }
        });
        spinner_outputNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfOutputNodes = (Integer)spinner_outputNodes.getValue();
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfOutputNodes: " + numOfOutputNodes, true, false);
            }
        });

        spinner_trainingCycles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfTrainingCycles = (Integer)spinner_trainingCycles.getValue();
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfTrainingCycles: " + numOfTrainingCycles, true, false);
            }
        });

        // Create neural network with specified parameters
        CREATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // confirmation dialog if neural network already exists
                if(neuralNetworkCreated == true) {
                    int confirm = JOptionPane.showOptionDialog(
                            mainPanel, "Are you sure you'd like to create a new Neural Network? This will delete and replace the current network, undoing all training.",
                            "Creation Confirmation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (confirm == 0) {
                        // continue
                    } else {
                        return;
                    }
                }

                // PARAMETER CHECKING
                // check if a training method is selected
                if (!(mutationTrain || gradientDescentTrain)) {
                    // display appropriate error
                    JOptionPane.showMessageDialog(mainPanel, "Malformed Parameter." +
                                    "\nPlease select a training method.",
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
                                    "\nLearning Rate should be greater than zero.",
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
                // if mutation network, set # input nodes = to input nodes
                if (gradientDescentTrain) {
                    neuralNetworkCreated = true;
                    mutNeuralNetwork = null;
                    difNeuralNetwork = new cDifferentialNetwork(numOfInputNodes, numOfHiddenNodes, numOfOutputNodes, numOfTrainingCycles, learningRate);
                } else if (mutationTrain) {
                    neuralNetworkCreated = true;
                    difNeuralNetwork = null;
                    mutNeuralNetwork = new Network(numOfTrainingCycles, numOfInputNodes, numOfInputNodes, numOfHiddenNodes, numOfOutputNodes);
                }

                // logger stuff
                String traintype = "";
                if (mutationTrain) {
                    traintype = "mutation";
                } else if (gradientDescentTrain) {
                    traintype = "gradientDescent";
                }

                Logger.log(Logger.LogLevel.INFO, "Neural Network created! Stats listed below.", true, false);
                Logger.log(Logger.LogLevel.INFO,
                        "   Training method           : " + traintype
                        + "\n   Number of input neurons   : " + numOfInputNodes
                        + "\n   Number of hidden neurons  : " + numOfHiddenNodes
                        + "\n   Number of output neurons  : " + numOfOutputNodes
                        + "\n   Number of training cycles : " + numOfTrainingCycles
                        + "\n   Learning Rate             : " + learningRate, false, false);

                counter++;
                netattrLabel.setForeground(blue);
                netattrLabel.setText("Network #" + counter + " | numInputs: " + numOfInputNodes + " |" +
                        " numHidden " + numOfHiddenNodes + " | numOutputs: " + numOfOutputNodes + " | numEpochs: " +
                        numOfTrainingCycles + " | learningRate: " + learningRate + " | trainType: " + traintype);
                netTrainInfoLabel.setForeground(Color.BLACK);
                trainedEpochs = 0;
                netTrainInfoLabel.setText("[Untrained]");

                // WARNING CHECK
                // check if low training cycle count
                if ((mutationTrain) && (numOfTrainingCycles < 100)) {
                    // Warn user of low number of training cycles
                    JOptionPane.showMessageDialog(mainPanel, "Low number of training cycles specified for mutation network." +
                                    "\nThis may result in severe under-performance depending on the dataset. Recommended range for" +
                                    " mutation network is 100 - 1000",
                            "Creation Warning", JOptionPane.WARNING_MESSAGE);
                } else if((!mutationTrain) && (numOfTrainingCycles > 100)) {
                    // Warn user of low number of training cycles
                    JOptionPane.showMessageDialog(mainPanel, "High number of training cycles specified for gradient descent network." +
                                    "\nThis may result in severe over-fitting depending on the dataset. Recommended range for" +
                                    " gradient descent network is 10 - 200",
                            "Creation Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    // if no warning, notify user it created w/o warnings
                    JOptionPane.showMessageDialog(mainPanel, "Network Created Successfully without warnings!"
                                    + "\n\n--- ATTRIBUTES ---"
                                    + "\nTraining method: " + traintype
                                    + "\nNumber of input neurons: " + numOfInputNodes
                                    + "\nNumber of hidden neurons: " + numOfHiddenNodes
                                    + "\nNumber of output neurons: " + numOfOutputNodes
                                    + "\nNumber of training cycles: " + numOfTrainingCycles
                                    + "\nLearning Rate: " + learningRate,
                            "Creation Notification", JOptionPane.INFORMATION_MESSAGE);
                }




            }
        });

        TRAINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(neuralNetworkCreated == false) {
                    JOptionPane.showMessageDialog(mainPanel, "Neural Network does not exist.",
                            "Training Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if data hs been loaded in network
                if (!dataLoaded) {
                    JOptionPane.showMessageDialog(mainPanel, "Data has not been loaded.",
                            "Training Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // check if it's a mutation or gradient descent network
                trainedEpochs += numOfTrainingCycles;
                netTrainInfoLabel.setForeground(blue);
                netTrainInfoLabel.setText("Trained with " + trainedEpochs + " epochs");

                if (mutationTrain) {
                    mutNeuralNetwork.mutationTrain(LoadedData, LoadedAnswers);
                    JOptionPane.showMessageDialog(mainPanel, "Network has successfully undergone mutation training" +
                                    " for " + numOfTrainingCycles + " training cycles.",
                            "Training Notification", JOptionPane.INFORMATION_MESSAGE);
                } else if (gradientDescentTrain) {
                    difNeuralNetwork.train(LoadedData, LoadedAnswers);
                    JOptionPane.showMessageDialog(mainPanel, "Network has successfully undergone gradient descent training" +
                                    " for " + numOfTrainingCycles + " training cycles.",
                            "Training Notification", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        PREDICTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(neuralNetworkCreated == false) {
                    JOptionPane.showMessageDialog(mainPanel, "Neural Network does not exist.",
                            "Prediction Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PredictionInput.clear();
                String text = predictionInputTextField.getText();
                List<String> temp = new ArrayList<>();
                temp.addAll(Arrays.asList(text.split(",")));
                PredictionInput = convertToDoubleList(temp);

                List<Double> prediction = new ArrayList<>();

                if (mutationTrain) {
                    prediction = mutNeuralNetwork.predict(PredictionInput);
                } else if (gradientDescentTrain) {
                    prediction = difNeuralNetwork.predict(PredictionInput);
                }

                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "Predicting! Input: " + PredictionInput + " | Prediction: " + prediction, false, true);

                predictionOutputLabel.setText("Prediction: " + prediction);
            }

        });

        mutationRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mutationTrain = true;
                gradientDescentTrain = false;
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "mutationTrainEnabled", true, false);
            }
        });

        gradientDescentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gradientDescentTrain = true;
                mutationTrain = false;
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "gradientDescentTrainEnabled", true, false);
            }
        });

        // clears textview and unloads data
        unloadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // clear textview
                dataLoaded = false;
                loadedDataView.setText("");

                // Unload data from neural network
                LoadedFile.clear();
                LoadedData.clear();
                LoadedAnswers.clear();

            }
        });

        // Displays data in textview and loads into data lists
        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (!neuralNetworkCreated) {
                    JOptionPane.showMessageDialog(mainPanel, "Neural Network does not exist.",
                            "Loading Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // display data in textView
                StringBuilder fileContents = new StringBuilder();
                String line;

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(filePath));
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Failed to load " + filePath + ". File doesn't exist or" +
                                    " you're attempting to load a directory.",
                            "Loading Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(e);
                }
                while (true) {
                    try {
                        if ((line = reader.readLine()) == null) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    fileContents.append(line).append("\n");
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                loadedDataView.setText(fileContents.toString());
                loadedDataView.setEditable(false); // Make it read-only


                // Load data into neural net
                dataLoaded = true;

                LoadedFile = DataLoader.loadData(filePath);
                LoadedData = DataLoader.separateInputs(LoadedFile, numOfInputNodes);
                LoadedAnswers = DataLoader.seperateAnswers(LoadedFile, numOfOutputNodes);

                JOptionPane.showMessageDialog(mainPanel, filePath + " loaded successfully!\n"
                        + "# Inputs: " + numOfInputNodes
                        + "\n# Outputs: " + numOfOutputNodes,
                        "Loading Notification", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Retrieve file path of selection in JTree
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                filePath = "";

                TreePath treepath = treeSelectionEvent.getPath();
                Object elements[] = treepath.getPath();
                // i = 0 to include topmost-dir in path, i = 1 to exclude
                for (int i = 1, n = elements.length; i < n; i++) {

                    // "\\" for windows
                    // "/" for linux
                    if (isLinux) {
                        filePath += elements[i] + "/";
                    } else {
                        filePath += elements[i] + "\\";
                    }
                }

                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "filePath = " + filePath, true, false);
            }
        });


        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Java Neural Network [Version 0.9 - DEV]" +
                        "\nA neural network engine and frontend GUI implemented in pure Java." +
                        "\nhttps://github.com/gjinrexhaj/java-neural-network\n" +
                        "\nDevelopers: " +
                        "\nGjin Rexhaj" +
                        "\nColm Duffin" +
                        "\nIbrahim Elamin" +
                        "\nGerti Gjini", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // update value of PredictionInput in accordance with textfield
        textField_learnFactor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                learningRate = Double.parseDouble(textField_learnFactor.getText());
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "learningRate: " + learningRate, true, false);

            }
        });

        // clear console view window
        clearConsoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "clearConsoleButton clicked", true, false);

                consoleTextArea.setText("");
            }
        });
        predictionInputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                predictionInputLabel.setText("Textfield Input: [" + predictionInputTextField.getText().toString() + "]");
            }
        });
    }

    // Helper method to convert string list into double list
    public static List<Double> convertToDoubleList(List<String> stringList) {
        List<Double> doubleList = new ArrayList<>();
        for (String str : stringList) {
            try {
                doubleList.add(Double.parseDouble(str));
            } catch (NumberFormatException e) {
                System.err.println("Error converting string to double: " + str);
                // Handle the exception as needed, e.g., skip the element, use a default value, etc.
            }
        }
        return doubleList;
    }



    // Crate and initialize application
    public static void main(String[] args) {

        // set window decoration for linux, and window theming
        osName = System.getProperty("os.name").toLowerCase();
        isLinux = osName.startsWith("linux");

        if (isLinux) {
            // enable custom window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        // set custom theming
        Font customFont = new Font("Arial", Font.PLAIN, 14);



        // Initially set color
        final OsThemeDetector detector = OsThemeDetector.getDetector();
        if (detector.isDark()) {
            FlatMacDarkLaf.setup();
        } else {
            FlatMacLightLaf.setup();
        }

        // Dynamically switch color
        detector.registerListener(isDark -> {
            SwingUtilities.invokeLater(() -> {
                if (isDark) {
                    // The OS switched to a dark theme
                    if (debugMode)
                        Logger.log(Logger.LogLevel.DEBUG, "OS Dark mode enabled, isDark: " + isDark, true, false);
                    FlatMacDarkLaf.setup();
                    FlatLaf.updateUI();
                } else {
                    // The OS switched to a light theme
                    if (debugMode)
                        Logger.log(Logger.LogLevel.DEBUG, "OS Light mode enabled, isDark: " + isDark, true, false);
                    FlatMacLightLaf.setup();
                    FlatLaf.updateUI();
                }
            });
        });


        UIManager.put("defaultFont", customFont);

        // Prompt user if they'd like to use debug mode
        int debugConfirm = JOptionPane.showOptionDialog(
                null, "Would you like to launch the program in debug mode?" +
                        " Debug mode will create more verbose console logs and files.",
                "Startup Mode Prompt", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (debugConfirm == 0) {
            debugMode = true;
            Logger.log(Logger.LogLevel.DEBUG, "ENTERING DEBUG MODE", true, false);
        } else {
            debugMode = false;
        }

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
                        null, "Are you sure you'd like to exit the application?" +
                        "\nNetworks aren't saved locally, all work will be lost.",
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
