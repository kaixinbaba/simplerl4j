package org.rl.simple.agent.nn;

import org.rl.simple.agent.Agent;
import org.rl.simple.env.Action;
import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;

import java.io.IOException;

public class DeepQLearning implements Agent {


    public DeepQLearning(DispersedEnviroment enviroment) {
    }

    public DeepQLearning(DispersedEnviroment enviroment, double epsilonStart, double epsilonDecay,
                      double epsilonMin, double learningRate, double gamma) {
    }

    @Override
    public Action chooseAction(State state) {
        return null;
    }

    @Override
    public void learn(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {

    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public String getSaveFilePath() {
        return null;
    }

    @Override
    public void printQTable() {
        return;
    }
}
