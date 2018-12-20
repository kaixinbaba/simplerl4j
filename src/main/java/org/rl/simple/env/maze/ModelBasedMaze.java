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
        super(width, height, everyStepSleepTime, needRender, isHumanPlay, isSlippery, unSlipperyProp);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MazeState s = new MazeState(i, j);
                MazeMap map = this.getMap();
                boolean b = map.setState(s);
                List<StateTransitionProbability> list = new ArrayList<>();
                for (Action a : this.actionSpace()) {
                    StepResult step = this.step(a);
                    StateTransitionProbability stp = new StateTransitionProbability();
                    stp.setDone(step.isDone());
                    stp.setNextState(step.getNextState());
                    stp.setReward(step.getReward().getReward());
                    // 这里状态转移概率都为1.0
                    stp.setProp(1.0D);
                    list.add(stp);
                }
                env.put(s, list);
            }
        }
    }

    @Override
    public List<State> getStates() {
        return new ArrayList<>(env.keySet());
    }

    @Override
    public StateTransitionProbability p(State state, int a) {
        return this.env.get(state).get(a);
    }
}
