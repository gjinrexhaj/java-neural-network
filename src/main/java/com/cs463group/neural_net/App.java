package com.cs463group.neural_net;


import com.cs463group.neural_net.mutation_training.Network;
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
        Logger.log(Logger.LogLevel.INFO, "CLI application has been launched", true, true);

        // create the network
        Network testnet = new Network(1000,2, 3, 2, 1);

        // create and load 2d arraylist with data and separate 1d arraylist to hold answers
        List<List<Double>> data = new ArrayList<>();
        List<Double> answers = new ArrayList<>();

        // TESTER CODE: manually load test data into data list and each entry's corresponding answer in answer list
        data.add(Arrays.asList(115.0, 66.0));
        data.add(Arrays.asList(175.0, 78.0));
        data.add(Arrays.asList(205.0, 72.0));
        data.add(Arrays.asList(120.0, 67.0));

        answers.add(1.0);
        answers.add(0.0);
        answers.add(0.0);
        answers.add(1.0);

        // CAN ALSO LOAD DATA IN PROGRAMMATICALLY FROM TXT FILE LIKE SO
        // TODO: Implement a dataLoader in Utils that loads data in a parametrized fashion
        // for this use case, the loaded dataset below is too large
        /*
        Scanner scan;
        File file = new File("test-data/weight-height-gender/Source-Data.txt");
        try {
            scan = new Scanner(file);
            scan.useDelimiter("[,\\n]");
            int i = 0;

            while (scan.hasNext()) {
                i++;
                String token = scan.next();
                if (i == 1) {
                    // data add 1 and next
                    String nextToken = scan.next();
                    data.add(Arrays.asList(Double.parseDouble(token), Double.parseDouble(nextToken)));
                    System.out.print(i + ": ");
                    System.out.println(token + ", " + nextToken);
                } else if (i == 2) {
                    // do nothing
                    System.out.print(i + ": ");
                    answers.add(Double.parseDouble(token));
                    System.out.println(token);
                    i = 0;
                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
         */

        // train the network with aforementioned data and answers
        testnet.train(data, answers);

        // manually load inputs for prediction
        List<Double> input1 = List.of(167.0, 73.0);
        List<Double> input2 = List.of(105.0, 67.0);
        List<Double> input3 = List.of(120.0, 72.0);
        List<Double> input4 = List.of(143.0, 67.0);
        List<Double> input5 = List.of(130.0, 66.0);

        // log output of neural net against expected outputs: for this data, 0 is male and 1 is female
        System.out.println(input1 + " | Expected output: 0 | Actual output: " + testnet.predict(input1));
        System.out.println(input2 + " | Expected output: 1 | Actual output: " + testnet.predict(input2));
        System.out.println(input3 + " | Expected output: 1 | Actual output: " + testnet.predict(input3));
        System.out.println(input4 + " | Expected output: 0 | Actual output: " + testnet.predict(input4));
        System.out.println(input5 + " | Expected output: 0 | Actual output: " + testnet.predict(input5));

        Logger.closeLogger();
    }
}