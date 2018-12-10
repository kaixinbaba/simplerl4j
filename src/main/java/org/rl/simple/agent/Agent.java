package org.rl.simple.agent;

import org.rl.simple.env.Action;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;

import java.io.IOException;

public interface Agent {

    Action chooseAction(State state);

    void learn(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done);

    void save() throws IOException;

    void load() throws IOException;

    String getSaveFilePath();

}
