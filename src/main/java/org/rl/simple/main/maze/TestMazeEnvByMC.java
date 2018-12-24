package org.rl.simple.main.maze;

import org.rl.simple.agent.Agent;
import org.rl.simple.agent.AgentFactory;
import org.rl.simple.agent.traditional.MCAgent;
import org.rl.simple.env.Action;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;
import org.rl.simple.env.maze.Maze;
import org.rl.simple.env.maze.MazeState;
import org.rl.simple.utils.FIFOSimpleQueue;

import java.io.IOException;

import static org.rl.simple.common.enums.Algorithm.MONTE_CARLO;
import static org.rl.simple.common.enums.Algorithm.Q_LEARNING;

@SuppressWarnings("all")
public class TestMazeEnvByMC {

    public static void main(String[] args) {
        int width = 4;
        int height = 4;
        int minStepCount = width + height - 2;
        double allowError = 0.5D;
        long sleepTime = 50L;
        boolean needRender = true;
        boolean isHumanPlay = false;
        boolean isSlippery = false;
        double unSlipperyProp = 0.7D;
        ///////////////////////////////////////////
        // 初始化 环境
        Maze maze = new Maze(width, height, sleepTime, needRender, isHumanPlay, isSlippery, unSlipperyProp);
        MCAgent agent = (MCAgent) AgentFactory.get(MONTE_CARLO, maze, true);
        try {
            agent.load();
            System.out.println("load success!!!");
        } catch (IOException e) {
            System.out.println("load failed!!!");
        }
        agent.printQTable();
        int howLongToAve = 5;
        FIFOSimpleQueue<Integer> lastFiveEpisode = new FIFOSimpleQueue<>(howLongToAve);
        int maxEpisode = 100;
        int episode = 0;
        while (episode < maxEpisode) {
            State state = maze.reset();
            double totalReward = 0.0D;
            int stepCount = 0;
            while (true) {
                maze.render();
                Action action = agent.chooseAction(state);
                StepResult stepResult = maze.step(action);
//                agent.learn(state, stepResult.getNextState(), action, null, stepResult.getReward(),
//                        stepResult.isDone());
                double reward = stepResult.getReward().getReward();
                agent.storeEpisode((MazeState) state, action, reward);
                totalReward += reward;
                if (stepResult.isDone()) {
                    System.out.println(reward == 10.0D
                            ? "Win!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" : "You lose");
                    System.out.println(String.format("Episode : %s, Total reward : %s Use total step count : %s",
                            episode, totalReward, stepCount));
                    agent.learn();
                    if (stepResult.isWin()) {
                        // add
                        lastFiveEpisode.add(stepCount);
                        // average
                        double ave = lastFiveEpisode.stream().reduce((i1, i2) -> i1 + i2).get() / howLongToAve;
                        if (lastFiveEpisode.size() == howLongToAve && ave - minStepCount <= allowError) {
                            try {
                                agent.save();
                                System.out.println("save success!!!");
                                System.exit(0);
                            } catch (Exception e) {
                                System.out.println("save failed!!!");
                                break;
                            }
                        }
                    } else {
                        lastFiveEpisode.clear();
                    }
                    episode++;
                    break;
                }
                state = stepResult.getNextState();
                stepCount++;
            }
        }
        return;
    }
}
