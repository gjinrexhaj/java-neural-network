/**
 *  DataLoader.java
 *  Created on 3/11/2025
 *  Defines an adaptive and flexible data loader, which reads and formats data from
 *  a csv file such that the backend can interact with it.
 *
 *  Copyright (C) 2025  Gjin Rexhaj
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */


package com.cs463group.bridge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DataLoader {

    // main method test fixture
    public static void main(String[] args) {

        // Tweak parameters based on neural net attributes
        int inputDimensionality = 4;
        int numOfOutputNeurons = 2;

        List<List<Double>> Records = new ArrayList<>();
        //Records = loadData("test-data/weight-height-gender/4-entries/source-data.csv");
        Records = loadData("src/main/java/com/cs463group/bridge/DataLoaderTest.csv");

        System.out.println();
        System.out.println("INPUT DIMEN: " + inputDimensionality);
        System.out.println("NUM OUTNEUR: " + numOfOutputNeurons);
        System.out.println("RECORD SIZE: " + Records.size());

        boolean isOk = isMalformed(Records, inputDimensionality, numOfOutputNeurons);
        List<List<Double>> DataInputs = separateInputs(Records, inputDimensionality);
        List<List<Double>> DataAnswers = seperateAnswers(Records, numOfOutputNeurons);

        System.out.println("malformed:   " + isOk);
        System.out.println("Records:     " + Records);
        System.out.println("dataInputs:  " + DataInputs);
        System.out.println("dataAnswers: " + DataAnswers);
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


    // Returns true if input is ok, returns false if not.
    public static boolean isMalformed(List<List<Double>> fileContents, int inputDimensionality, int numOfOutputNeurons) {

        if (fileContents.getFirst().size() == numOfOutputNeurons + inputDimensionality) {
            return false;
        } else {
            return true;
        }
    }



    public static List<List<Double>> separateInputs(List<List<Double>> fileContents, int inputDimensionality) {
        List<List<Double>> inputs = new ArrayList<>();

        for(int i = 0; i < fileContents.size(); i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < fileContents.get(i).size(); j++) {
                if (j < inputDimensionality) {
                    row.add(fileContents.get(i).get(j));
                } else if (j > inputDimensionality) {
                    break;
                } else {
                    inputs.add(row);
                }
            }
        }
        return inputs;
    }

    public static List<List<Double>> seperateAnswers(List<List<Double>> fileContents, int numOfOutputNeurons) {
        List<List<Double>> answers = new ArrayList<>();

        for(int i = 0; i < fileContents.size(); i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < fileContents.get(i).size(); j++) {
                if (j > fileContents.get(i).size()-1 - numOfOutputNeurons) {
                    row.add(fileContents.get(i).get(j));
                }
            }
            answers.add(row);
        }
        return answers;
    }
}
