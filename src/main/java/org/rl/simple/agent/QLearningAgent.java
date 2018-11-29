package org.rl.simple.agent;

import org.rl.simple.env.Action;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;

public class QLearningAgent implements Agent {

    @Override
    public Action chooseAction(State state) {
        return null;
    }

    @Override
    public void learn(State state, State nextState, Action action, Reward reward, boolean done) {

    }
}
