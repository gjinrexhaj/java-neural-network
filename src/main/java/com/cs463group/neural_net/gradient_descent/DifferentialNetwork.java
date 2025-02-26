package com.cs463group.neural_net.gradient_descent;

import com.cs463group.neural_net.utils.Functions;
import com.cs463group.neural_net.utils.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class DifferentialNetwork {

    // define network attrbiutes
    double learnRate;
    int epochs;

    // constructor
    public DifferentialNetwork(int numEpochs, double rateOfLearn) {
        epochs = numEpochs;
        learnRate = rateOfLearn;
        Logger.log(Logger.LogLevel.INFO, "Differential neural network with " + epochs + " epochs and learnRate: " + learnRate + " has been successfuly created.", true, true);
    }

    // TODO: programmatically make is so networks are dynamically configurable
    // in this case, 2 hidden, 1 output neuron
    DifferentialNeuron nHidden1 = new DifferentialNeuron();
    DifferentialNeuron nHidden2 = new DifferentialNeuron();
    DifferentialNeuron nOutput = new DifferentialNeuron();


    // prediction method
    public Double predict(Double input1, Double input2) {
        return nOutput.compute(nHidden1.compute(input1,input2), nHidden2.compute(input1,input2));
    }

    // differentiation training
    public void train(List<List<Double>> data, List<Double> answers) {
        Logger.log(Logger.LogLevel.INFO, "Differential training has begun with " + epochs + " specified training cycles.", true, true);
        for (int epoch = 0; epoch < epochs; epoch++) {

            for (int i = 0; i < data.size(); i++) {
                double in1 = data.get(i).get(0);
                double in2 = data.get(i).get(1);
                double loss = -2 * (answers.get(i) - this.predict(in1, in2)); // Derivative of the loss for this answer
                this.adjust(loss, in1, in2);
            }

            // Log the trainer every 10 epochs
            if (epoch % 10 == 0) {
                List<Double> predictions = data.stream().map( item -> this.predict(item.get(0), item.get(1)) ).collect( Collectors.toList() );
                Double loss = Functions.meanSquareLoss(answers, predictions);
                Logger.log(Logger.LogLevel.INFO, "Epoch " + epoch + "    Pred: " + predictions + "     Loss: "+ loss, false, true);
            }
        }
        Logger.log(Logger.LogLevel.INFO, "Differential network training has finished.", true, true);
    }

    // recursive adjustment method used in differentiation training
    public void adjust(Double loss, Double in1, Double in2) {
        Double o1W1 = nOutput.getWeight1();  Double o1W2 = nOutput.getWeight2();

        Double h1Output = nHidden1.compute(in1, in2);
        Double h2Output = nHidden2.compute(in1, in2);

        Double derivedOutput = nOutput.getDerivedOutput(h1Output, h2Output);

        Double derivedH1 = nHidden1.getDerivedOutput(in1, in2);
        Double derivedH2 = nHidden2.getDerivedOutput(in1, in2);

        nHidden1.adjust(
                learnRate * loss * (o1W1 * derivedOutput) * (in1 * derivedH1),
                learnRate * loss * (o1W1 * derivedOutput) * (in2 * derivedH1),
                learnRate * loss * (o1W1 * derivedOutput) * derivedH1);

        nHidden2.adjust(
                learnRate * loss * (o1W2 * derivedOutput) * (in1 * derivedH2),
                learnRate * loss * (o1W2 * derivedOutput) * (in2 * derivedH2),
                learnRate * loss * (o1W2 * derivedOutput) * derivedH2);

        nOutput.adjust(
                learnRate * loss * h1Output * derivedOutput,
                learnRate * loss * h2Output * derivedOutput,
                learnRate * loss * derivedOutput);
    }
}
