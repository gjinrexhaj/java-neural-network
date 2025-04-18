/**
 *  cDifferentialNetwork.java
 *
 *  Contains an implementation of a parametrized neural network
 *  that uses Gradient Descent training
 *
 *  Created by Colm Duffin
 *
 */


package com.cs463group.neural_net.gradient_descent;

import com.cs463group.neural_net.utils.Functions;
import com.cs463group.neural_net.utils.Logger;
import java.util.ArrayList;
import java.util.List;


public class cDifferentialNetwork {
    private     double                      dLearnRate;
    private     int                         iEpochs;

    //Network layers
    private     int                         iInputLayerSize;
    private     List<cDifferentialNeuron>   nlHiddenLayer;
    private     List<cDifferentialNeuron>   nlOutputLayer;

    public cDifferentialNetwork(int inputSize, int hiddenSize, int outputSize, int numEpochs, double rateOfLearn) {
        this.iEpochs            = numEpochs;
        this.dLearnRate         = rateOfLearn;
        this.iInputLayerSize    = inputSize;

        //Create hidden layer of neurons
        nlHiddenLayer       = new ArrayList<>();
        for (int i = 0; i < hiddenSize; i++) {
            nlHiddenLayer.add(new cDifferentialNeuron(iInputLayerSize));
        }

        //Create output layer of neurons
        nlOutputLayer       = new ArrayList<>();
        for (int i = 0; i < outputSize; i++) {
            nlOutputLayer.add(new cDifferentialNeuron(hiddenSize));
        }

        Logger.log(Logger.LogLevel.INFO, "Differential neural network created with " + iInputLayerSize + " input neurons, " +
                hiddenSize + " hidden neurons, and " + outputSize + " output neurons. LearnRate: " + rateOfLearn + ", epochs: " +
                numEpochs, true, false);
    }

    public List<Double> predict(List<Double> inputs) {
        List<Double> dlHiddenLayerOutputs   = new ArrayList<>();
        List<Double> dlOutputLayerOutputs   = new ArrayList<>();

        for(int n = 0; n<nlHiddenLayer.size(); n++) {
            dlHiddenLayerOutputs.add(nlHiddenLayer.get(n).compute(inputs));
        }

        for(int n = 0; n<nlOutputLayer.size(); n++) {
            dlOutputLayerOutputs.add(nlOutputLayer.get(n).compute(dlHiddenLayerOutputs));
        }

        return dlOutputLayerOutputs;
    }

    public void train(List<List<Double>> data, List<List<Double>> answers) {
        Logger.log(Logger.LogLevel.INFO, "Training started with " + iEpochs + " epochs.", true, false);

        for (int epoch = 0; epoch < iEpochs; epoch++) {
            for (int i = 0; i < data.size(); i++) {
                List<Double> dlInputs       = data.get(i);
                List<Double> dlFinalOutputs = answers.get(i);

                List<Double> dlPredictions  = predict(dlInputs);
                List<Double> dlLoss         = new ArrayList<>();

                for (int j = 0; j < dlPredictions.size(); j++) {
                    dlLoss.add(-2 * (dlFinalOutputs.get(j) - dlPredictions.get(j)));
                }

                adjust(dlLoss, dlInputs);
            }

            if (epoch % 1 == 0) {
                List<List<Double>> dlPredictionsList    = new ArrayList<>();

                for(int i = 0; i<data.size(); i++) {
                    dlPredictionsList.add(  predict(data.get(i)) );
                }

                double loss = Functions.meanSquareLoss(Functions.convertToArray(answers), Functions.convertToArray(dlPredictionsList));
                Logger.log(Logger.LogLevel.INFO, "Epoch " + epoch + " Loss: " + loss, false, false);
            }
        }
        Logger.log(Logger.LogLevel.INFO, "Training completed.", true, false);
    }

    private void adjust(List<Double> outputLoss, List<Double> inputs) {
        List<Double> dlHiddenOutputs    = new ArrayList<>();

        for(int n = 0; n<nlHiddenLayer.size(); n++) {
            dlHiddenOutputs.add(nlHiddenLayer.get(n).compute(inputs));
        }

        List<Double> dlHiddenGradients  = new ArrayList<>();
        for (int i = 0; i < nlHiddenLayer.size(); i++) {
            dlHiddenGradients.add(0.0);
        }

        for (int i = 0; i < nlOutputLayer.size(); i++) {
            cDifferentialNeuron cdnOutputNeuron = nlOutputLayer.get(i);
            double dDerivedOutput = cdnOutputNeuron.getDerivedOutput(dlHiddenOutputs);
            for (int j = 0; j < nlHiddenLayer.size(); j++) {
                dlHiddenGradients.set(j, dlHiddenGradients.get(j) + cdnOutputNeuron.getWeight(j) * dDerivedOutput * outputLoss.get(i));
            }
            cdnOutputNeuron.adjust(outputLoss.get(i), dlHiddenOutputs, dLearnRate);
        }

        for (int i = 0; i < nlHiddenLayer.size(); i++) {
            nlHiddenLayer.get(i).adjust(dlHiddenGradients.get(i), inputs, dLearnRate);
        }
    }

}
