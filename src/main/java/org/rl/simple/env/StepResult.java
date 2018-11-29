package org.rl.simple.env;

import lombok.Data;
import org.rl.simple.env.maze.MazeState;

@Data
public class StepResult {

    private boolean done;

    private MazeState nextState;

    private Reward reward;

    private Info info;

}
