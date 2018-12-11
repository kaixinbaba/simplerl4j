package org.rl.simple.agent;

import lombok.NonNull;
import org.rl.simple.agent.nn.DeepQLearning;
import org.rl.simple.agent.traditional.QLearningAgent;
import org.rl.simple.agent.traditional.SarsaAgent;
import org.rl.simple.agent.traditional.SarsaExpectedAgent;
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
            } else if (algorithm.equals(Algorithm.SARSA_EXPECTED)) {
                agent = new SarsaExpectedAgent((DispersedEnviroment) enviroment);
            } else if (algorithm.equals(Algorithm.DYNAMIC_PROGRAMMING)) {

            } else if (algorithm.equals(Algorithm.DQN)) {
                agent = new DeepQLearning((DispersedEnviroment) enviroment);
            } else if (algorithm.equals(Algorithm.DPG)) {

            }
            agentCache.put(algorithm, agent);
            return agent;
        }
    }
}
