package com.cs463group.neural_net;


import com.cs463group.neural_net.gradient_descent.DifferentialNetwork;
import com.cs463group.neural_net.mutation_training.MutationNetwork;
import com.cs463group.neural_net.utils.Logger;

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

public class App {

    public static void main(String[] args) {

        List<List<Double>> data = new ArrayList<List<Double>>();

        Logger.log(Logger.LogLevel.INFO, "CLI application has been launched", true);


        // TODO: figure out why gradient descent training doesn't work with this particular data
        // ( "data must pivot on 0") -> may need to reformat data

        // prototpye data      weight, height - Works with mutation training
        data.add(Arrays.asList(115.0, 66.0));
        data.add(Arrays.asList(175.0, 78.0));
        data.add(Arrays.asList(205.0, 72.0));
        data.add(Arrays.asList(120.0, 67.0));
        // prototype answers: 1.0: female, 0.0: male
        List<Double> answers = Arrays.asList(1.0,0.0,0.0,1.0);


        // create and mutation train networks with 500 and 1000 epochs respectively
        MutationNetwork mutationTestEpoch500 = new MutationNetwork(500);
        MutationNetwork mutationTestEpoch1000 = new MutationNetwork(1000);

        mutationTestEpoch500.train(data, answers);
        mutationTestEpoch1000.train(data, answers);

        // create and differentiation train networks with 500 and 1000 epochs respectively
        //DifferentialNetwork diffTestEpoch500 = new DifferentialNetwork(500, 0.15);
        //DifferentialNetwork diffTestEpoch1000 = new DifferentialNetwork(1000, 0.15);

        //diffTestEpoch500.train(data, answers);
        //diffTestEpoch1000.train(data, answers);



        // TODO: Implement printing out a network's results at the class level, calculated with Network.predict
        System.out.println("------------MUTATION TRAINING RESULTS-------------");
        System.out.println(String.format("  male, 167, 73: network500learn1: %.10f | network1000learn1: %.10f", mutationTestEpoch500.predict(167.0, 73.0), mutationTestEpoch1000.predict(167.0, 73.0)));
        System.out.println(String.format("female, 105, 67: network500learn1: %.10f | network1000learn1: %.10f", mutationTestEpoch500.predict(105.0, 67.0), mutationTestEpoch1000.predict(105.0, 67.0)));
        System.out.println(String.format("female, 120, 72: network500learn1: %.10f | network1000learn1: %.10f", mutationTestEpoch500.predict(120.0, 72.0), mutationTestEpoch1000.predict(120.0, 72.0)));
        System.out.println(String.format("  male, 143, 67: network500learn1: %.10f | network1000learn1: %.10f", mutationTestEpoch500.predict(143.0, 67.0), mutationTestEpoch1000.predict(120.0, 72.0)));
        System.out.println(String.format("  male, 130, 66: network500learn1: %.10f | network1000learn1: %.10f", mutationTestEpoch500.predict(130.0, 66.0), mutationTestEpoch1000.predict(130.0, 66.0)));


        /*
        System.out.println("------------DIFFERENTIATION TRAINING RESULTS-------------");
        System.out.println(String.format("  male, 167, 73: network500learn1: %.10f | network1000learn1: %.10f", diffTestEpoch500.predict(167.0, 73.0), diffTestEpoch1000.predict(167.0, 73.0)));
        System.out.println(String.format("female, 105, 67: network500learn1: %.10f | network1000learn1: %.10f", diffTestEpoch500.predict(105.0, 67.0), diffTestEpoch1000.predict(105.0, 67.0)));
        System.out.println(String.format("female, 120, 72: network500learn1: %.10f | network1000learn1: %.10f", diffTestEpoch500.predict(120.0, 72.0), diffTestEpoch1000.predict(120.0, 72.0)));
        System.out.println(String.format("  male, 143, 67: network500learn1: %.10f | network1000learn1: %.10f", diffTestEpoch500.predict(143.0, 67.0), diffTestEpoch1000.predict(120.0, 72.0)));
        System.out.println(String.format("  male, 130, 66: network500learn1: %.10f | network1000learn1: %.10f", diffTestEpoch500.predict(130.0, 66.0), diffTestEpoch1000.predict(130.0, 66.0)));
         */

        Logger.closeLogger();
    }
}