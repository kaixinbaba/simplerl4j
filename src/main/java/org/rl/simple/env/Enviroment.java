package org.rl.simple.env;

import org.rl.simple.env.maze.MazeState;

public interface Enviroment {

    MazeState reset();

    Action sampleAction();

    StepResult step(Action action);

    void render();
}
