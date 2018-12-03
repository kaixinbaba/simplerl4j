package org.rl.simple.agent;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.rl.simple.env.Action;
import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;
import org.rl.simple.utils.JsonSerilizable;

import java.io.IOException;
import java.util.*;

public class SarsaAgent extends TDAgent {

    @Getter
    protected Map<State, List<Double>> qtable = new HashMap<>();

    protected int actionCount;
    protected double epsilon;
    protected double epsilonDecay;
    protected double epsilonMin;
    protected double learningRate;
    protected double gamma;
    protected Action[] actionSpace;
    protected DispersedEnviroment enviroment;

    public SarsaAgent(DispersedEnviroment enviroment) {
        this(enviroment, 1.0D, 0.1D, 0.0D, 0.1D, 0.9D);
    }

    public SarsaAgent(DispersedEnviroment enviroment, double epsilonStart, double epsilonDecay,
                          double epsilonMin, double learningRate, double gamma) {
        this.enviroment = enviroment;
        this.actionCount = enviroment.actionCount();
        this.actionSpace = enviroment.actionSpace();
        this.epsilon = epsilonStart;
        this.epsilonDecay = epsilonDecay;
        this.epsilonMin = epsilonMin;
        this.learningRate = learningRate;
        this.gamma = gamma;
    }


    @Override
    public Action chooseAction(State state) {
        Action result;
        if (this.qtable.containsKey(state)) {
            if (RandomUtils.nextDouble(0, 1) < this.epsilon) {
                // random choice
                result = this.enviroment.sampleAction();
            } else {
                // max
                List<Double> actionExpectedReward = this.qtable.get(state);
                result = this.actionSpace[this.getMaxActionIndex(actionExpectedReward)];
            }
        } else {
            // put in table
            this.qtable.put(state, initActionRowList());
            // random choice
            result = this.enviroment.sampleAction();
        }
        return result;
    }

    /**
     * 为了防止有多个最大值
     * @param actionExpectedReward
     * @return
     */
    protected int getMaxActionIndex(List<Double> actionExpectedReward) {
        Double max = Collections.max(actionExpectedReward);
        List<Integer> maxIndex = new ArrayList<>(actionExpectedReward.size());
        for (int i = 0; i < actionExpectedReward.size(); i++) {
            Double current = actionExpectedReward.get(i);
            if (max.equals(current)) {
                maxIndex.add(i);
            }
        }
        return maxIndex.get(RandomUtils.nextInt(0, maxIndex.size()));
    }

    protected List<Double> initActionRowList() {
        List<Double> result = new ArrayList<>(this.actionCount);
        for (int i = 0; i < this.actionCount; i++) {
            result.add(0.0D);
        }
        return result;
    }

    @Override
    public void learn(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {
        updateQtable(state, nextState, action, nextAction, reward, done);
        updateEpsilon();
    }

    @Override
    public void save() {
        try {
            JsonSerilizable.serilizableForMap(this.qtable, getSaveFilePath());
        } catch (Exception e) {
            System.out.println("Save error!");
            e.printStackTrace();
        }
    }

    @Override
    public String getSaveFilePath() {
        return "sarsa.json";
    }

    @Override
    public void load() {
        try {
            this.qtable = JsonSerilizable.deserilizableForMapFromFile(getSaveFilePath());
        } catch (Exception e) {
            System.out.println("Load error!");
            e.printStackTrace();
        }
    }

    protected void updateEpsilon() {
        double epsilon = this.epsilon - this.epsilonDecay;
        if (epsilon > this.epsilonMin) {
            this.epsilon = epsilon;
        } else {
            this.epsilon = this.epsilonMin;
        }
    }

    @Override
    protected void updateQtable(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {

        Double qValue = this.qtable.get(state).get(action.getCode());
        // sarsa 更新的就是下次行动的q值 on-policy
        Double afterUpdateQValue = qValue + this.learningRate *
                (reward.getReward() + this.gamma * this.qtable.getOrDefault(nextState, initActionRowList()).get(nextAction.getCode()) - qValue);
        this.qtable.get(state).set(action.getCode(), afterUpdateQValue);
    }
}
