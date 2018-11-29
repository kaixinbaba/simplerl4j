package org.rl.simple.main;

import org.rl.simple.env.Action;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;
import org.rl.simple.env.maze.Maze;

public class TestEnv {

    public static void main(String[] args) {
        System.out.println("hello world");
        Maze maze = new Maze(6, 6, 200L);
        while (true) {
            State state = maze.reset();
            while (true) {
                maze.render();
                Action action = maze.sampleAction();
                StepResult step = maze.step(action);
                System.out.println("reward : " + step.getReward().getReward());
                if (step.isDone()) {
                    System.out.println("done");
                    break;
                }
            }
        }
    }
}
