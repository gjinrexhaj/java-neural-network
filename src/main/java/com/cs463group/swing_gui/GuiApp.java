package com.cs463group.swing_gui;

import com.cs463group.bridge.DataLoader;
import com.cs463group.neural_net.gradient_descent.cDifferentialNetwork;
import com.cs463group.neural_net.utils.Logger;
import com.cs463group.neural_net.mutation_training.Network;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jthemedetecor.OsThemeDetector;


/**
 * GuiApp.java
 * Created on 2/12/2025
 * Contains all frontend GUI code for neural network visualizer
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

        $$$setupUI$$$();
        spinner_inputNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfInputNodes = (Integer) spinner_inputNodes.getValue();

                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfInputNodes: " + numOfInputNodes, true, false);
            }
        });
        spinner_hiddenNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfHiddenNodes = (Integer) spinner_hiddenNodes.getValue();
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfHiddenNodes: " + numOfHiddenNodes, true, false);
            }
        });
        spinner_outputNodes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfOutputNodes = (Integer) spinner_outputNodes.getValue();
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfOutputNodes: " + numOfOutputNodes, true, false);
            }
        });

        spinner_trainingCycles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                numOfTrainingCycles = (Integer) spinner_trainingCycles.getValue();
                if (debugMode)
                    Logger.log(Logger.LogLevel.DEBUG, "numOfTrainingCycles: " + numOfTrainingCycles, true, false);
            }
        });

        // Create neural network with specified parameters
        CREATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // confirmation dialog if neural network already exists
                if (neuralNetworkCreated == true) {
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
                } else if ((!mutationTrain) && (numOfTrainingCycles > 100)) {
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
                if (neuralNetworkCreated == false) {
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
                if (neuralNetworkCreated == false) {
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
        frame.setSize(1280, 720);

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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 10, 10, 10), -1, -1));
        mainPanel.setEnabled(true);
        splitplane = new JSplitPane();
        splitplane.setDividerLocation(900);
        splitplane.setDividerSize(10);
        splitplane.setDoubleBuffered(false);
        splitplane.setEnabled(true);
        mainPanel.add(splitplane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        viewsPanel = new JTabbedPane();
        splitplane.setLeftComponent(viewsPanel);
        viewsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "View Panel", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        viewsPanel.addTab("Network view", panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setToolTipText("");
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Network graph", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel2.add(NeuralNetworkVisualizerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        viewsPanel.addTab("Training Data view", panel3);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(null, "Select data", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        fileTree.putClientProperty("JTree.lineStyle", "");
        panel4.add(fileTree, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loadDataButton = new JButton();
        loadDataButton.setBackground(new Color(-16745729));
        loadDataButton.setForeground(new Color(-1));
        loadDataButton.setText("Load Data");
        panel5.add(loadDataButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unloadDataButton = new JButton();
        unloadDataButton.setText("Unload Data");
        panel5.add(unloadDataButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Supported file type: .csv");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(null, "Loaded data", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel6.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        loadedDataView = new JTextPane();
        loadedDataView.setEditable(false);
        Font loadedDataViewFont = this.$$$getFont$$$("Source Code Pro", -1, -1, loadedDataView.getFont());
        if (loadedDataViewFont != null) loadedDataView.setFont(loadedDataViewFont);
        loadedDataView.setText("EXAMPLE - WHAT YOUR DATA SHOULD LOOK LIKE (for InputDimensionality = 2 and NumOutputNeurons = 1 in this case):\n\nINPUT 1        INPUT 2        ANSWER\nHeight (cm), Weight (kg), Gender (0-male, 1-female)\n\n-----------------example data begin-----------------\nheight,weight,gender\n172, 66, 0\n182, 76, 0\n176, 57, 1\netc 1\netc 2\netc ...\netc n\n------------------example data end------------------\n\nLOAD DATA BY SELECTING A .csv FILE VIA FILE TREE\nON THE LEFT, AND CLICKING \"Load Data\"\n");
        scrollPane1.setViewportView(loadedDataView);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        viewsPanel.addTab("Prediction view", panel7);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel8.setBorder(BorderFactory.createTitledBorder(null, "Input", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        predictionInputTextField = new JTextField();
        predictionInputTextField.setText("");
        panel8.add(predictionInputTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel8.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Enter input as numbers seperated by commas (eg: 142, 23)");
        panel8.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("The number of entries should be equal to the number of input");
        panel8.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("nodes that exist in the generated neural network. After entering");
        panel8.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("the input, click \"PREDICT\" to generate a prediction from the input.");
        panel8.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        predictionInputLabel = new JLabel();
        Font predictionInputLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, predictionInputLabel.getFont());
        if (predictionInputLabelFont != null) predictionInputLabel.setFont(predictionInputLabelFont);
        predictionInputLabel.setText("Textfield Input: [null]");
        panel8.add(predictionInputLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel9, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel9.setBorder(BorderFactory.createTitledBorder(null, "Output", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        predictionOutputLabel = new JLabel();
        Font predictionOutputLabelFont = this.$$$getFont$$$(null, Font.BOLD, 22, predictionOutputLabel.getFont());
        if (predictionOutputLabelFont != null) predictionOutputLabel.setFont(predictionOutputLabelFont);
        predictionOutputLabel.setForeground(new Color(-16745729));
        predictionOutputLabel.setHorizontalAlignment(0);
        predictionOutputLabel.setHorizontalTextPosition(0);
        predictionOutputLabel.setText("Prediction: [dne]");
        panel9.add(predictionOutputLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        viewsPanel.addTab("Console view", panel10);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel10.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(null, "Console Output", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        consoleTextArea.setEditable(false);
        consoleTextArea.setEnabled(true);
        Font consoleTextAreaFont = this.$$$getFont$$$("Source Code Pro", Font.PLAIN, 14, consoleTextArea.getFont());
        if (consoleTextAreaFont != null) consoleTextArea.setFont(consoleTextAreaFont);
        consoleTextArea.setToolTipText("");
        scrollPane2.setViewportView(consoleTextArea);
        clearConsoleButton = new JButton();
        clearConsoleButton.setEnabled(true);
        clearConsoleButton.setHideActionText(false);
        clearConsoleButton.setHorizontalAlignment(0);
        clearConsoleButton.setText("Clear Console");
        clearConsoleButton.setToolTipText("Log entries will not be removed from log_file.txt\n");
        panel10.add(clearConsoleButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitplane.setRightComponent(panel11);
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Control Panel", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, null));
        trainingMethodPanel = new JPanel();
        trainingMethodPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(trainingMethodPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        trainingMethodPanel.setBorder(BorderFactory.createTitledBorder(null, "Training method", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        mutationRadioButton = new JRadioButton();
        mutationRadioButton.setText("Mutation");
        trainingMethodPanel.add(mutationRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        trainingMethodPanel.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        gradientDescentRadioButton = new JRadioButton();
        gradientDescentRadioButton.setText("Gradient descent");
        trainingMethodPanel.add(gradientDescentRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        attributesPanel = new JPanel();
        attributesPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(attributesPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        attributesPanel.setBorder(BorderFactory.createTitledBorder(null, "Network attributes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        spinner_inputNodes = new JSpinner();
        attributesPanel.add(spinner_inputNodes, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        attributesPanel.add(spacer3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        spinner_hiddenNodes = new JSpinner();
        attributesPanel.add(spinner_hiddenNodes, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinner_outputNodes = new JSpinner();
        attributesPanel.add(spinner_outputNodes, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Input nodes");
        attributesPanel.add(label6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Hidden nodes");
        attributesPanel.add(label7, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Output nodes");
        attributesPanel.add(label8, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinner_trainingCycles = new JSpinner();
        attributesPanel.add(spinner_trainingCycles, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Training cycles");
        attributesPanel.add(label9, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Learn factor");
        attributesPanel.add(label10, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField_learnFactor = new JTextField();
        textField_learnFactor.setHorizontalAlignment(11);
        textField_learnFactor.setText("1.0");
        textField_learnFactor.setToolTipText("");
        attributesPanel.add(textField_learnFactor, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panel12, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
        panel12.setBorder(BorderFactory.createTitledBorder(null, "Actions", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        PREDICTButton = new JButton();
        PREDICTButton.setBackground(new Color(-16745729));
        PREDICTButton.setEnabled(true);
        PREDICTButton.setForeground(new Color(-1));
        PREDICTButton.setText("PREDICT");
        PREDICTButton.setToolTipText("Use neural network to make a prediction based on an input");
        panel12.add(PREDICTButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        TRAINButton = new JButton();
        TRAINButton.setText("TRAIN");
        TRAINButton.setToolTipText("Train the neural network using loaded training data in accordance with the training method");
        panel12.add(TRAINButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CREATEButton = new JButton();
        CREATEButton.setText("CREATE");
        CREATEButton.setToolTipText("Create a neural network using network attribute parameters");
        panel12.add(CREATEButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setRollover(false);
        toolBar1.putClientProperty("JToolBar.isRollover", Boolean.FALSE);
        mainPanel.add(toolBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        aboutButton = new JButton();
        aboutButton.setText("About");
        toolBar1.add(aboutButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        netattrLabel = new JLabel();
        Font netattrLabelFont = this.$$$getFont$$$(null, -1, -1, netattrLabel.getFont());
        if (netattrLabelFont != null) netattrLabel.setFont(netattrLabelFont);
        netattrLabel.setHorizontalAlignment(0);
        netattrLabel.setHorizontalTextPosition(0);
        netattrLabel.setText("[Active network attributes will show up here once created]");
        toolBar1.add(netattrLabel);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator2);
        netTrainInfoLabel = new JLabel();
        netTrainInfoLabel.setText("");
        toolBar1.add(netTrainInfoLabel);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(gradientDescentRadioButton);
        buttonGroup.add(mutationRadioButton);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
