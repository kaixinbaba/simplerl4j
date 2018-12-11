package org.rl.simple.agent.traditional;

import org.rl.simple.agent.Agent;
import org.rl.simple.env.Action;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;

public abstract class TDAgent implements Agent {

    protected abstract void updateQtable(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done);
}
