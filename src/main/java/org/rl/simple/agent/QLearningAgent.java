package org.rl.simple.agent;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.rl.simple.env.Action;
import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;

import java.util.*;

public class QLearningAgent extends TDAgent {

    @Getter
    private Map<State, List<Double>> qtable = new HashMap<>();

    private int actionCount;
    private double epsilon;
    private double epsilonDecay;
    private double epsilonMin;
    private double learningRate;
    private double gamma;
    private Action[] actionSpace;
    private DispersedEnviroment enviroment;

    public QLearningAgent(DispersedEnviroment enviroment) {
        this(enviroment, 1.0D, 0.005D, 0.05D, 0.01D, 0.9D);
    }

    public QLearningAgent(DispersedEnviroment enviroment, double epsilonStart, double epsilonDecay,
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
    private int getMaxActionIndex(List<Double> actionExpectedReward) {
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

    private List<Double> initActionRowList() {
        List<Double> result = new ArrayList<>(this.actionCount);
        for (int i = 0; i < this.actionCount; i++) {
            result.add(0.0D);
        }
        return result;
    }

    @Override
    public void learn(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {
        Double qValue = this.qtable.get(state).get(action.getCode());
        // 最重要的公式
        Double afterUpdateQValue = qValue + this.learningRate *
                (reward.getReward() + this.gamma * Collections.max(this.qtable.getOrDefault(nextState, initActionRowList())) - qValue);
        this.qtable.get(state).set(action.getCode(), afterUpdateQValue);
        double epsilon = this.epsilon - this.epsilonDecay;
        if (epsilon > this.epsilonMin) {
            this.epsilon = epsilon;
        } else {
            this.epsilon = this.epsilonMin;
        }
//        System.out.println(this.qtable);
    }
}
