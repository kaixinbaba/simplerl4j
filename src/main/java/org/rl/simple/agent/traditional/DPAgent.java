package org.rl.simple.agent.traditional;

import org.rl.simple.agent.Agent;
import org.rl.simple.env.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DPAgent implements Agent {

    private static final Double THETA = 0.000001D;
    private final Map<State, Double> stateValue = new HashMap<>();
    private static final Double GAMMA = 1.0D;

    public DPAgent(ModelBasedDispersedEnviroment enviroment) {
        Map<State, List<Double>> policy = initPolicy(enviroment);
        while (true) {
            // 策略迭代
        }
    }

    private Map<State, Double> evalStateValue(Map<State, List<Double>> policy, ModelBasedDispersedEnviroment enviroment) {
        Map<State, Double> v = new HashMap<>();
        while (true) {
            Double delta = 0.0D;
            for (State s : policy.keySet()) {
                Double value = 0.0D;
                List<Double> props = policy.get(s);
                for (int i = 0; i < props.size() - 1; i++) {
                    Double prop = props.get(i);
                    ModelBasedDispersedEnviroment.StateTransitionProbability p = enviroment.p(s, i);
                    State nextState = p.getNextState();
                    Double tranProp = p.getProp();
                    Double reward = p.getReward();
                    value += tranProp * prop * (reward + GAMMA * v.getOrDefault(nextState, 0.0D));
                }
                delta = Math.max(delta, Math.abs(v.get(s) - value));
                v.put(s, value);
            }
            if (delta < THETA) {
                break;
            }
        }
        return v;
    }

    private Map<State, List<Double>> initPolicy(ModelBasedDispersedEnviroment enviroment) {
        Map<State, List<Double>> policy = new HashMap<>();
        Double probability = 1.0 / enviroment.actionCount();
        for (State s : enviroment.getStates()) {
            List<Double> actionProp = new ArrayList<>();
            for (int i = 0; i < enviroment.actionCount(); i++) {
                actionProp.add(probability);
            }
            policy.put(s, actionProp);
        }
        return policy;
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

    }
}
