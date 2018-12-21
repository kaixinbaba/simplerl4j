package org.rl.simple.env.maze;

import org.rl.simple.env.Action;
import org.rl.simple.env.ModelBasedDispersedEnviroment;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型已知迷宫的环境
 */
public class ModelBasedMaze extends Maze implements ModelBasedDispersedEnviroment {

    private Map<State, List<StateTransitionProbability>> env = new HashMap<>();

    public ModelBasedMaze(int width, int height, long everyStepSleepTime, boolean needRender, boolean isHumanPlay, boolean isSlippery, double unSlipperyProp) {
        super(width, height, everyStepSleepTime, needRender, isHumanPlay, isSlippery, unSlipperyProp, true);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MazeState s = new MazeState(i, j);
                List<StateTransitionProbability> list = new ArrayList<>();
                if (i == 0 && j == 0) {
                    // 0, 0
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(1, 0), -3.0D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 1), -0.1D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
                    env.put(s, list);
                } else if (i == 0 && j == 1) {
                    // 0, 1
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(1, 1), -10.0D, true));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 1), -0.1D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 1), -0.1D, false));

                    env.put(s, list);
                } else if (i == 1 && j == 0) {
                    // 1, 0
                    list.add(new StateTransitionProbability(1.0D, new MazeState(1, 0), -0.1D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(1, 0), -0.1D, false));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(1, 1), -10.0D, true));
                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
                    env.put(s, list);
                } else {
                    // 1, 1
//                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
//                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
//                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
//                    list.add(new StateTransitionProbability(1.0D, new MazeState(0, 0), -0.1D, false));
                }
            }
        }
        System.out.println(env);
    }

    @Override
    public List<State> getStates() {
        return new ArrayList<>(env.keySet());
    }

    @Override
    public StateTransitionProbability p(State state, int a) {
        List<StateTransitionProbability> stateTransitionProbabilities = this.env.get(state);
        if (stateTransitionProbabilities == null) {
            return null;
        }
        return stateTransitionProbabilities.get(a);
    }

    @Override
    public boolean isDoneState(MazeState state) {
        return this.getMap().isDone(state.getX(), state.getY());
    }

    @Override
    public State reset() {
        return this.getMap().reset(true);
    }
}
