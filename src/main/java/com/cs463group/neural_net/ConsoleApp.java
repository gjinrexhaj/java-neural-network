package com.cs463group.neural_net;


import com.cs463group.neural_net.gradient_descent.cDifferentialNetwork;
import com.cs463group.neural_net.mutation_training.Network;
import com.cs463group.neural_net.utils.Logger;
import com.cs463group.bridge.DataLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  App.java
 *  Created on 2/10/2025
 *  Launching point of the program. Commandline variant
 */

public class ConsoleApp {

    public static void main(String[] args) {
        Logger.log(Logger.LogLevel.INFO, "CLI application has been launched", true, true);

        // create the networks - specify seperate parameters for mutation vs gd net
        int mut_numEpochs = 1000;
        int mut_numDataInputs = 2;
        int mut_numInputNeurons = 2;
        int mut_numHiddenNeurons = 2;
        int mut_numOutputNeurons = 1;
        double mut_learnRate = 0.5;

        int dif_numEpochs = 100;
        int dif_numDataInputs = 2;
        int dif_numInputNeurons = 2;
        int dif_numHiddenNeurons = 2;
        int dif_numOutputNeurons = 1;
        double dif_learnRate = 0.1;

        Network testnet = new Network(mut_numEpochs, mut_learnRate, mut_numDataInputs, mut_numInputNeurons, mut_numHiddenNeurons, mut_numOutputNeurons);
        cDifferentialNetwork mm_testNetDIFF = new cDifferentialNetwork(dif_numInputNeurons, dif_numHiddenNeurons, dif_numOutputNeurons, dif_numEpochs, dif_learnRate);
        cDifferentialNetwork zs_testNetDIFF = new cDifferentialNetwork(dif_numInputNeurons, dif_numHiddenNeurons, dif_numOutputNeurons, dif_numEpochs, dif_learnRate);

        // create and load 2d arraylist with data and separate 1d arraylist to hold answers - non normalized
        List<List<Double>> raw_loadedFile = new ArrayList<>();
        List<List<Double>> raw_data = new ArrayList<>();
        List<List<Double>> raw_answers = new ArrayList<>();

        List<List<Double>> mm_nrm_loadedFile = new ArrayList<>();
        List<List<Double>> mm_nrm_data = new ArrayList<>();
        List<List<Double>> mm_nrm_answers = new ArrayList<>();

        List<List<Double>> zs_nrm_loadedFile = new ArrayList<>();
        List<List<Double>> zs_nrm_data = new ArrayList<>();
        List<List<Double>> zs_nrm_answers = new ArrayList<>();

        raw_loadedFile = DataLoader.loadData("test-data/weight-height-gender/4-entries/Source-data.csv");
        raw_data = DataLoader.separateInputs(raw_loadedFile, mut_numDataInputs);
        raw_answers = DataLoader.seperateAnswers(raw_loadedFile, mut_numOutputNeurons);

        mm_nrm_loadedFile = DataLoader.loadData("test-data/weight-height-gender/4-entries/MinMax-normalized.csv");
        mm_nrm_data = DataLoader.separateInputs(mm_nrm_loadedFile, dif_numDataInputs);
        mm_nrm_answers = DataLoader.seperateAnswers(mm_nrm_loadedFile, dif_numOutputNeurons);

        zs_nrm_loadedFile = DataLoader.loadData("test-data/weight-height-gender/4-entries/ZScore-normalized.csv");
        zs_nrm_data = DataLoader.separateInputs(zs_nrm_loadedFile, dif_numDataInputs);
        zs_nrm_answers = DataLoader.seperateAnswers(zs_nrm_loadedFile, dif_numOutputNeurons);


        // train the networks with aforementioned data and answers (use normalized for gradient descent)
        testnet.mutationTrain(raw_data, raw_answers);
        mm_testNetDIFF.train(mm_nrm_data, mm_nrm_answers);
        zs_testNetDIFF.train(zs_nrm_data, zs_nrm_answers);


        // manually load inputs for prediction - must be normalized if using GD, else use raw
        List<Double> raw_input1 = List.of(167.0, 73.0);
        List<Double> raw_input2 = List.of(105.0, 67.0);
        List<Double> raw_input3 = List.of(120.0, 72.0);
        List<Double> raw_input4 = List.of(143.0, 67.0);
        List<Double> raw_input5 = List.of(130.0, 66.0);

        // MinMax normalized inputs
        List<Double> mm_nrm_input1 = List.of(1.0, 1.0);
        List<Double> mm_nrm_input2 = List.of(0.0, 0.14286);
        List<Double> mm_nrm_input3 = List.of(0.24194, 0.85714);
        List<Double> mm_nrm_input4 = List.of(0.6129, 0.14286);
        List<Double> mm_nrm_input5 = List.of(0.40323, 0.0);

        // Zscore normalized inputs
        List<Double> zs_nrm_input1 = List.of(1.6143, 1.38013);
        List<Double> zs_nrm_input2 = List.of(-1.32942, -0.69007);
        List<Double> zs_nrm_input3 = List.of(-0.61723, 1.0351);
        List<Double> zs_nrm_input4 = List.of(0.47479, -0.69007);
        List<Double> zs_nrm_input5 = List.of(-0.14244, -1.0351);


        String mut_netAtr = new String("epochs: " + mut_numEpochs + " | numInputNeurons: " + mut_numInputNeurons + " | numHiddenNeurons: " + mut_numHiddenNeurons + " | numOutputNeurons: " + mut_numOutputNeurons + " | learnRate: " + mut_learnRate);
        String dif_netAtr = new String("epochs: " + dif_numEpochs + " | numInputNeurons: " + dif_numInputNeurons + " | numHiddenNeurons: " + dif_numHiddenNeurons + " | numOutputNeurons: " + dif_numOutputNeurons + " | learnRate: " + dif_learnRate);

        System.out.println(" ---- MUTATION NETWORK RESULTS ---- ");
        System.out.println(mut_netAtr);
        System.out.println(raw_input1 + " | Expected output: 0 | Actual output: " + testnet.predict(raw_input1));
        System.out.println(raw_input2 + " | Expected output: 1 | Actual output: " + testnet.predict(raw_input2));
        System.out.println(raw_input3 + " | Expected output: 1 | Actual output: " + testnet.predict(raw_input3));
        System.out.println(raw_input4 + " | Expected output: 0 | Actual output: " + testnet.predict(raw_input4));
        System.out.println(raw_input5 + " | Expected output: 0 | Actual output: " + testnet.predict(raw_input5));


        System.out.println(" ---- DIFFERENTIAL NETWORK RESULTS ( MIN-MAX NORMALIZED) ---- ");
        System.out.println(dif_netAtr);
        System.out.println(raw_input1 + " | Expected output: 0 | Actual output: " + mm_testNetDIFF.predict(mm_nrm_input1));
        System.out.println(raw_input2 + " | Expected output: 1 | Actual output: " + mm_testNetDIFF.predict(mm_nrm_input2));
        System.out.println(raw_input3 + " | Expected output: 1 | Actual output: " + mm_testNetDIFF.predict(mm_nrm_input3));
        System.out.println(raw_input4 + " | Expected output: 0 | Actual output: " + mm_testNetDIFF.predict(mm_nrm_input4));
        System.out.println(raw_input5 + " | Expected output: 0 | Actual output: " + mm_testNetDIFF.predict(mm_nrm_input5));

        System.out.println(" ---- DIFFERENTIAL NETWORK RESULTS ( Z-SCORE NORMALIZED ) ---- ");
        System.out.println(dif_netAtr);
        System.out.println(raw_input1 + " | Expected output: 0 | Actual output: " + zs_testNetDIFF.predict(zs_nrm_input1));
        System.out.println(raw_input2 + " | Expected output: 1 | Actual output: " + zs_testNetDIFF.predict(zs_nrm_input2));
        System.out.println(raw_input3 + " | Expected output: 1 | Actual output: " + zs_testNetDIFF.predict(zs_nrm_input3));
        System.out.println(raw_input4 + " | Expected output: 0 | Actual output: " + zs_testNetDIFF.predict(zs_nrm_input4));
        System.out.println(raw_input5 + " | Expected output: 0 | Actual output: " + zs_testNetDIFF.predict(zs_nrm_input5));


        Logger.closeLogger();
    }
}