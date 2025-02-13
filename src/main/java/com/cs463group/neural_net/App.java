package com.cs463group.neural_net;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  App.java
 *  Created on 2/10/2025
 *  Launching point of the program. Commandline variant
 */

// TODO: List requirements for GUI frontend
// TODO: Implement custom neural network specifications -> number of neurons at every layer
// TODO: Implement gradient-descent based training method

public class App {

    public static void main(String[] args) {

        List<List<Integer>> data = new ArrayList<List<Integer>>();

        Logger.log(Logger.LogLevel.INFO, "CLI application has been launched", true);

        // prototpye data      weight, height
        data.add(Arrays.asList(115, 66));
        data.add(Arrays.asList(175, 78));
        data.add(Arrays.asList(205, 72));
        data.add(Arrays.asList(120, 67));
        // prototype answers: 1.0: female, 0.0: male
        List<Double> answers = Arrays.asList(1.0,0.0,0.0,1.0);


        // create and train networks with 500 and 1000 epochs respectively
        Network testEpoch500 = new Network(500);
        Network testEpoch1000 = new Network(1000);

        // training is currently implemented via mutation
        testEpoch500.train(data, answers);
        testEpoch1000.train(data, answers);


        // TODO: Implement printing out a network's results at the class level, calculated with Network.predict
        System.out.println("");
        System.out.println(String.format("  male, 167, 73: network500learn1: %.10f | network1000learn1: %.10f", testEpoch500.predict(167, 73), testEpoch1000.predict(167, 73)));
        System.out.println(String.format("female, 105, 67: network500learn1: %.10f | network1000learn1: %.10f", testEpoch500.predict(105, 67), testEpoch1000.predict(105, 67)));
        System.out.println(String.format("female, 120, 72: network500learn1: %.10f | network1000learn1: %.10f", testEpoch500.predict(120, 72), testEpoch1000.predict(120, 72)));
        System.out.println(String.format("  male, 143, 67: network500learn1: %.10f | network1000learn1: %.10f", testEpoch500.predict(143, 67), testEpoch1000.predict(120, 72)));
        System.out.println(String.format("  male, 130, 66: network500learn1: %.10f | network1000learn1: %.10f", testEpoch500.predict(130, 66), testEpoch1000.predict(130, 66)));

        Logger.closeLogger();
    }
}