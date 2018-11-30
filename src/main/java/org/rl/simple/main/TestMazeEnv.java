package org.rl.simple.main;

import org.rl.simple.agent.Agent;
import org.rl.simple.agent.QLearningAgent;
import org.rl.simple.env.Action;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;
import org.rl.simple.env.maze.Maze;

import java.util.Arrays;

public class TestMazeEnv {

    public static void main(String[] args) {
        Maze maze = new Maze(4, 4, 50L);
        Agent agent = new QLearningAgent(maze);
        int maxEpisode = 100;
        int episode = 0;
        while (episode < maxEpisode) {
            State state = maze.reset();
            double totalReward = 0.0D;
            int stepCount = 0;
            while (true) {
                maze.render();
//                Action action = maze.sampleAction();
                Action action = agent.chooseAction(state);
                StepResult stepResult = maze.step(action);
                agent.learn(state, stepResult.getNextState(), action, null, stepResult.getReward(),
                        stepResult.isDone());
                totalReward += stepResult.getReward().getReward();
                if (stepResult.isDone()) {
                    System.out.println(String.format("Episode : %s, Total reward : %s", episode, totalReward));
                    System.out.println(String.format("Use total step count : %s", stepCount));
                    episode++;
                    break;
                }
//                if (count % 20 == 0) {
//                    System.out.println(((QLearningAgent) agent).getQtable());
//                }
                state = stepResult.getNextState();
                stepCount++;
            }
        }
    }
}
