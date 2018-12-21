package org.rl.simple.env;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.rl.simple.env.maze.MazeState;

import java.util.List;

public interface ModelBasedDispersedEnviroment extends DispersedEnviroment {


    List<State> getStates();


    StateTransitionProbability p(State state, int a);

    boolean isDoneState(MazeState state);

    @Data
    @AllArgsConstructor
    class StateTransitionProbability {

        private Double prop;
        private State nextState;
        private Double reward;
        private boolean done;

    }
}
