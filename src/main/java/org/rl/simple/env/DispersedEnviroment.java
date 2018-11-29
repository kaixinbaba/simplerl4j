package org.rl.simple.env;


public interface DispersedEnviroment extends Enviroment {

    int actionCount();

    int stateCount();

    Action[] actionSpace();
}
