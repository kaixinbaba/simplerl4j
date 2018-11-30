package org.rl.simple.main.maze;

import org.rl.simple.agent.Agent;
import org.rl.simple.agent.AgentFactory;
import org.rl.simple.agent.QLearningAgent;
import org.rl.simple.common.enums.Algorithm;
import org.rl.simple.env.Action;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;
import org.rl.simple.env.maze.Maze;

import static org.rl.simple.common.enums.Algorithm.Q_LEARNING;

@SuppressWarnings("all")
public class TestMazeEnvByQleaning {

    public static void main(String[] args) {
        Maze maze = new Maze(3, 3, 50L);
        Agent agent = AgentFactory.get(Q_LEARNING, maze);
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
                double reward = stepResult.getReward().getReward();
                totalReward += reward;
                if (stepResult.isDone()) {
                    System.out.println(reward == 10.0D
                            ? "Win!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" : "You lose");
                    System.out.println(String.format("Episode : %s, Total reward : %s Use total step count : %s",
                            episode, totalReward, stepCount));
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
