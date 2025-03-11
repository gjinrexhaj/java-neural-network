package com.cs463group.bridge;

// TODO: Implement a dataLoader in bridge that loads data in a parametrized fashion

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  DataLoader.java
 *  Created on 3/11/2025
 *  Defines an adaptive and flexible data loader, which reads and formats data from
 *  a csv or plaintext file such that the backend can interact with it.
 */

public class DataLoader {

    // temp main method to test functionality
    public static void main(String[] args) {


        List<List<Double>> records = new ArrayList<>();
        records = loadData("test-data/weight-height-gender/4-entries/Source-data.csv");
        System.out.println("RECORD SIZE: " + records.size());
        for(int i = 0; i < records.size(); i++){
            System.out.println(records.get(i));
        }

        //dataInputs = seperateInputs(records);
        //dataAnswers = seperateAnswers(records);
    }

    // static method to load values from file into 2d string list
    public static List<List<Double>> loadData(String filepath) {
        List<List<String>> fileContents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                fileContents.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // omit header
        fileContents.removeFirst();

        // convert values into Double
        List<List<Double>> formattedData = fileContents.stream()
                .map(line -> line.stream().map(Double::parseDouble).collect(Collectors.toList()))
                .toList();

        return formattedData;
    }

    //public static

    // draft: MAKE BELOW CODE ADAPTIVE - use num of input datapoint and num of output neurons as parameters for
    // data size and handling
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
}
