package org.rl.simple.env;


import lombok.Data;

import java.util.List;

public interface ModelBasedDispersedEnviroment extends DispersedEnviroment {


    List<State> getStates();


    StateTransitionProbability p(State state, int a);


    @Data
    class StateTransitionProbability {

        private Double prop;
        private State nextState;
        private Double reward;
        private boolean done;

    }
}
