package org.rl.simple.agent;

import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Enviroment;

import java.util.HashMap;
import java.util.Map;

public class AgentFactory {

    private static Map<String, Agent> agentCache = new HashMap<>();


    public static Agent get(String code, Enviroment enviroment) {
        if (agentCache.containsKey(code)) {
            return agentCache.get(code);
        } else {
            Agent agent = null;
            if (code.equalsIgnoreCase("q-learning")) {
                agent = new QLearningAgent((DispersedEnviroment) enviroment);
            } else if (code.equalsIgnoreCase("sarsa")) {
                agent = new SarsaAgent((DispersedEnviroment) enviroment);
            } else if (code.equalsIgnoreCase("dp")) {

            } else if (code.equalsIgnoreCase("dqn")) {

            } else if (code.equalsIgnoreCase("dpg")) {

            }
        }
        return null;
    }
}
