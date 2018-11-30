package org.rl.simple.agent;

import org.rl.simple.env.Action;
import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;

import java.util.Collections;
import java.util.List;

/**
 */
public class SarsaExpectedAgent extends SarsaAgent {

    public SarsaExpectedAgent(DispersedEnviroment enviroment) {
        super(enviroment);
    }

    public SarsaExpectedAgent(DispersedEnviroment enviroment, double epsilonStart, double epsilonDecay,
                              double epsilonMin, double learningRate, double gamma) {
        super(enviroment, epsilonStart, epsilonDecay, epsilonMin, learningRate, gamma);
    }


    @Override
    protected void updateQtable(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {

        Double qValue = this.qtable.get(state).get(action.getCode());
        // qlearning 取的是下一个状态的最大动作q值 off-policy
        Double afterUpdateQValue = qValue + this.learningRate *
                (reward.getReward() + this.gamma * expectedValue(nextState) - qValue);
        this.qtable.get(state).set(action.getCode(), afterUpdateQValue);
    }

    private double expectedValue(State nextState) {
        int maxActionIndex = getMaxActionIndex(this.qtable.get(nextState));
        double otherActionRate = (1.0D - this.epsilon) / 4;
        double maxActionRate = this.epsilon + otherActionRate;
        List<Double> actions = this.qtable.getOrDefault(nextState, initActionRowList());
        double expectedValue = 0.0D;
        for (int i = 0; i < actions.size(); i++) {
            double currentActionValue = actions.get(i);
            if (i == maxActionIndex) {
                expectedValue += maxActionRate * currentActionValue;
            } else {
                expectedValue += otherActionRate * currentActionValue;
            }
        }
        return expectedValue;
    }
}
