package org.rl.simple.agent;

import lombok.NonNull;
import org.rl.simple.common.enums.Algorithm;
import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Enviroment;

import java.util.HashMap;
import java.util.Map;

public class AgentFactory {

    private static Map<Algorithm, Agent> agentCache = new HashMap<>();


    public static Agent get(@NonNull Algorithm algorithm, Enviroment enviroment) {
        if (agentCache.containsKey(algorithm)) {
            return agentCache.get(algorithm);
        } else {
            Agent agent = null;
            if (algorithm.equals(Algorithm.Q_LEARNING)) {
                agent = new QLearningAgent((DispersedEnviroment) enviroment);
            } else if (algorithm.equals(Algorithm.SARSA)) {
                agent = new SarsaAgent((DispersedEnviroment) enviroment);
            } else if (algorithm.equals(Algorithm.DYNAMIC_PROGRAMMING)) {

            } else if (algorithm.equals(Algorithm.DQN)) {

            } else if (algorithm.equals(Algorithm.DPG)) {

            }
            return agent;
        }
    }
}
