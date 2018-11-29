package org.rl.simple.env;

import lombok.Data;

@Data
public class StepResult {

    private boolean done;

    private State nextState;

    private Reward reward;

    private Info info;

}
