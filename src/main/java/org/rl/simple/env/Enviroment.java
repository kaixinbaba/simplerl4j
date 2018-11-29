package org.rl.simple.env;

public interface Enviroment {

    State reset();

    Action sampleAction();

    StepResult step(Action action);

    void render();
}
