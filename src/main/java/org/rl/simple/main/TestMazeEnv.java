package org.rl.simple.main;

import org.rl.simple.env.Action;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;
import org.rl.simple.env.maze.Maze;

public class TestMazeEnv {

    public static void main(String[] args) {
        Maze maze = new Maze(6, 6, 100L);
        int maxEpisode = 10;
        int episode = 0;
        while (episode < maxEpisode) {
            State state = maze.reset();
            double totalReward = 0.0D;
            while (true) {
                maze.render();
                Action action = maze.sampleAction();
                StepResult stepResult = maze.step(action);
                totalReward += stepResult.getReward().getReward();
                if (stepResult.isDone()) {
                    System.out.println(String.format("Episode : %s, Total reward : %s", episode, totalReward));
                    episode++;
                    break;
                }
                state = stepResult.getNextState();
            }
        }
    }
}
